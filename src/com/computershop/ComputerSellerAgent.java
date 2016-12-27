package com.computershop;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ComputerSellerAgent extends Agent {
	private static final long serialVersionUID = 1L;
	
	private final String SERVICE_TYPE = "Computer-Selling";
	private final String SERVICE_NAME = "JADE-Computer-Trading";
	private final String NOT_AVAILABLE = "Not-Available";
	
	private List<Computer> computers;
	private Map<AID, AtomicInteger> clients;
	private ComputerSellerGUI sellerGUI;
	
	//private ComputerSellerAgent sellerAgent;
	private ComputerSellerAgent sellerAgent;
	
	protected void setup() {
		clients = new HashMap<AID, AtomicInteger>();
		computers = new ArrayList<Computer>();
		
		sellerGUI = new ComputerSellerGUI(this);
		sellerGUI.show();

		sellerAgent = sellerGUI.sellerAgent;
		
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType(SERVICE_TYPE);
		serviceDescription.setName(SERVICE_NAME);		
		
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		dfAgentDescription.setName(getAID());
		dfAgentDescription.addServices(serviceDescription);
		
		try {
			DFService.register(this, dfAgentDescription);
		}
		catch(FIPAException fipaException) {
			fipaException.printStackTrace();
		}
		
		addBehaviour(new OfferRequestsServer());
		addBehaviour(new OfferRefusalsServer());
		addBehaviour(new OfferAcceptanceServer());
	}
	
	protected void takeDown() {
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		sellerGUI.dispose();
	}
	
	public void addComputer(final Computer computer) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {				
				computers.add(computer);
			}
		});
	}
	
	/* parse message to get computer details */
	private Computer createComputerFromMessage(String message) {
		String parts[] = message.split(":");		
		
		String cpuType = parts[0];
		String gpuType = parts[1];
		int ramCapacity = Integer.parseInt(parts[2]);
		int memCapacity = Integer.parseInt(parts[3]);
		boolean hasSSD = false;
		if(parts[4].equalsIgnoreCase("yes")) {
			 hasSSD = true;
		}
		
		int price = Integer.parseInt(parts[5]);
		
		/* create computer with received details*/
		return new Computer(cpuType, gpuType, ramCapacity, memCapacity, hasSSD, price);
		
	}
	
	/*add behaviours */
	
	/* computer offers --> proposal or refusal*/
	private class OfferRequestsServer extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void action() {
			MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage aclMessage = sellerAgent.receive(messageTemplate);
			
			if(aclMessage != null) {
				String message = aclMessage.getContent();				
				Computer computer = createComputerFromMessage(message);
				
				/* create reply message */
				ACLMessage replyMessage = aclMessage.createReply();
				
				if(computer != null) {
					replyMessage.setPerformative(ACLMessage.PROPOSE);
					replyMessage.setContent(String.valueOf(computer.getPrice()));
				}
				else {
					replyMessage.setPerformative(ACLMessage.REFUSE);
					replyMessage.setContent(NOT_AVAILABLE);
				}
				
				sellerAgent.send(replyMessage);
			}
			else {
				block();
			}
		}		
	}
	
	
	/* refuse offer */
	private class OfferRefusalsServer extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
			ACLMessage aclMessage = sellerAgent.receive(messageTemplate);
			
			if(aclMessage != null) {
				String message = aclMessage.getContent();
				Computer computer = createComputerFromMessage(message);
				
				if(computer != null) {
					computer.incrementRefusals();
					sellerGUI.incrementRefusalsForComputer(computer.getCpuType(), computer.getRamCapacity(), computer.getMemCapacity(), computer.getPrice());
				}
			}
			else {
				block();
			}
		}
	}
	
	/* accept offer */
	private class OfferAcceptanceServer extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage aclMessage = sellerAgent.receive(messageTemplate);
			
			if(aclMessage != null) {
				String message = aclMessage.getContent();
				Computer computer = createComputerFromMessage(message);
				
				ACLMessage replyMessage = aclMessage.createReply();
				
				if(computer != null) {
					/* get client */
					AID aid = aclMessage.getSender();
					if(!clients.containsKey(aid)) {
						clients.put(aid, new AtomicInteger(1));
					}
					else {
						clients.get(aid).incrementAndGet();
					}
					
					/* increment sales */
					computer.incrementSalesAndDecreaseStock();
					sellerGUI.incrementSalesAndDecreaseStockForComputer(computer.getCpuType(), computer.getRamCapacity(), computer.getMemCapacity(), computer.getPrice());
					
					replyMessage.setPerformative(ACLMessage.INFORM);
				}
				else {
					replyMessage.setPerformative(ACLMessage.FAILURE);
					replyMessage.setContent(NOT_AVAILABLE);					
				}
				
				sellerAgent.send(replyMessage);
			}
			else {
				block();
			}
		}
	}
	
}

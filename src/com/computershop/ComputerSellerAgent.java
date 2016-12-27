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
	
	private List<Computer> computers;
	private Map<AID, AtomicInteger> clients;
	private ComputerSellerGUI sellerGUI;
	
	protected void setup() {
		clients = new HashMap<AID, AtomicInteger>();
		computers = new ArrayList<Computer>();
		
		sellerGUI = new ComputerSellerGUI(this);
		sellerGUI.show();

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

			@Override
			public void action() {				
				computers.add(computer);
			}
		});
	}
	
	/*add behaviours*/
	
	
	
	
}

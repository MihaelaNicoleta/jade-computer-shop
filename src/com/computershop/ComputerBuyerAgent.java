package com.computershop;

import java.util.*;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class ComputerBuyerAgent extends Agent
{
	private static final long serialVersionUID = 1L;
	
	private static final String CONV_SELL_ID = "Computer-Selling";
	
	private String targetCpu;
	private int targetRamCap;
	private int targetMemCap;
	private int maxPrice;
	
	private ComputerBuyerGUI buyerGUI;
	private ComputerBuyerAgent thisAgent;
	
	private AID[] sellerAgents;
	private Map<String, Boolean> accepted;
	List<AID> proposalsList;
	List<Integer> priceList;
	
	private boolean finished;
	
	class RequestPerformer extends SimpleBehaviour
	{
		private static final long serialVersionUID = 1L;
		
		private static final String CONV_TRADE_ID = "JADE-Computer-Trading";
		
		private int repliesCount = 0;
		private int acceptanceCount = 0;
		private int resultsCount = 0;
		
		private MessageTemplate msgTempl;
		
		Steps step = Steps.RequestAllSellerOffers;
		
		@Override
		public void action() 
		{
			String search = targetCpu + ":" + targetRamCap + ":" + targetMemCap + ":" + maxPrice;
			ACLMessage reply ;
			
			switch(this.step)
			{
				case RequestAllSellerOffers:
				{
					ACLMessage cfpMsg = new ACLMessage(ACLMessage.CFP);
					for(int i = 0; i < sellerAgents.length; i++)
					{
						cfpMsg.addReceiver(sellerAgents[i]);
					}
					cfpMsg.setContent(search);
					cfpMsg.setConversationId(CONV_TRADE_ID);
					cfpMsg.setReplyWith("cpf" + System.currentTimeMillis());
					thisAgent.send(cfpMsg);
					
					msgTempl = MessageTemplate.and(MessageTemplate.MatchConversationId(CONV_TRADE_ID),
							MessageTemplate.MatchInReplyTo(cfpMsg.getReplyWith()));
					
					this.step = Steps.ReceiveOffers;
				}
				break; // end of RequestAllSellerOffers
				
				case ReceiveOffers://1
				{
					reply = thisAgent.receive(msgTempl);
					
					if(reply != null)
					{
						 if(reply.getPerformative() == ACLMessage.PROPOSE)
						 {
							 int price = Integer.parseInt(reply.getContent());
							 proposalsList.add(reply.getSender());
							 priceList.add(price);
							 buyerGUI.addProposal(reply.getSender().getLocalName(), price);
						 }
						 repliesCount++;
						 if(repliesCount >= sellerAgents.length)
						 {
							 step = Steps.RequestToBuy;
							 buyerGUI.endProposals();
							 if(proposalsList.size() == 0)
							 {
								 step = Steps.Done;
								 return;
							 }
						 }
					}
					else 
					{
						block();
					}
				}
				break;//end of ReceiveOffers
				
				case RequestToBuy:
				{
					if(!finished){
						return;
					}
					
					boolean accept = false;
					boolean reject = false;
					
					ACLMessage orderAccept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					ACLMessage orderReject = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
					
					for(AID aid : proposalsList)
					{
						if(accepted.get(aid.getLocalName()))
						{
							orderAccept.addReceiver(aid);
							accept = true;
							acceptanceCount++;
							System.out.println("Accepted: " + acceptanceCount + " proposal(s)");
						}
						else
						{
							orderReject.addReceiver(aid);
							reject = true;
						}
					}
					
					orderAccept.setContent(search);
					orderAccept.setConversationId(CONV_TRADE_ID);
					orderAccept.setReplyWith("order" + System.currentTimeMillis());
					
					if(accept)
					{
						thisAgent.send(orderAccept);
					}else if(reject) 
					{
						orderReject.setContent(search);
						orderReject.setConversationId(CONV_TRADE_ID);
						orderReject.setReplyWith("order" + System.currentTimeMillis());
						thisAgent.send(orderReject);
					}else {
						step = Steps.Done;
						return;
					}
					msgTempl = MessageTemplate.and(
							MessageTemplate.MatchConversationId(CONV_TRADE_ID), 
							MessageTemplate.MatchInReplyTo(orderAccept.getReplyWith()));
					
				}					
				break; //end of RequestToBuy
				
				case ReceviveSellACK:
				{
					reply = thisAgent.receive(msgTempl);
					if(reply != null)
					{
						if(reply.getPerformative() == ACLMessage.INFORM)
						{
							buyerGUI.receiveResult(reply.getSender().getLocalName(), "SUCCESS");
						}
						else
						{
							buyerGUI.receiveResult(reply.getSender().getLocalName(), "FAILURE");	
						}
						resultsCount++;
						if(resultsCount == acceptanceCount)
						{
							step = Steps.Done;
						}
					}else
					{
						block();
					}
				}
				break; // end of ReceviveSellACK
				case Done:
					//do nothing
				break;
				default://posible useless
				break;
			
			}
		}

		@Override
		public boolean done() 
		{
			if((this.step == Steps.RequestToBuy && proposalsList.size() == 0 && finished) ||
				(this.step == Steps.Done))
			{
				return true;	
			}
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void setup() 
	{
		thisAgent = this;
		buyerGUI = new ComputerBuyerGUI(thisAgent);
		buyerGUI.show();
	}
	
	public void finish(final Map<String, Boolean> acceptMap)
	{
		System.out.println("finish");
		
		addBehaviour(new OneShotBehaviour() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void action() {
				System.out.println("finised");
				thisAgent.accepted = acceptMap;
				finished = true;
			}
		});
	}
	
	public void startCFP(final String searchCpuType,final int searchRamCap,final int searchMemCap,final int price)
	{
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void action() {
				targetCpu = searchCpuType;
				targetRamCap = searchRamCap;
				targetMemCap = searchMemCap;
				maxPrice = price;
				accepted  = new HashMap<String, Boolean>();
				proposalsList = new ArrayList<>();
				priceList = new ArrayList<>();
				finished = false;
				
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription serviceDescription = new ServiceDescription();
				serviceDescription.setType(CONV_SELL_ID);
				template.addServices(serviceDescription);
				try{
					DFAgentDescription[] result = DFService.search(thisAgent, template);
					sellerAgents = new AID[result.length];
					for(int i = 0; i< result.length; i++)
					{
						sellerAgents[i] = result[i].getName();
					}
				}catch (FIPAException e) {
					// TODO: handle exception
				}
				thisAgent.addBehaviour(new RequestPerformer());
			}
		});
	}
	
	protected void takeDown()
	{
		buyerGUI.setVisible(false);
		buyerGUI.dispose();
	}
}

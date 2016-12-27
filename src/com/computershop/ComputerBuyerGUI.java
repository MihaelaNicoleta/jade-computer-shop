package com.computershop;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.EventQueue;


public class ComputerBuyerGUI extends JFrame{

	private static final long serailVersionUID = 1L;
	
	private ComputerBuyerAgent buyerAgent;
	private List<String> sellerNames = new ArrayList();
	private List<Integer> sellerPrices =  new ArrayList<>();
	private List<JCheckBox> sellerChxBx = new ArrayList<>();
	private List<JLabel> sellerLabels = new ArrayList<>();
	
	private JPanel proposalsPanel;
	private JButton searchButton;
	private JButton buyButton;
	
	public ComputerBuyerGUI(ComputerBuyerAgent agent) {
		super(agent.getLocalName());
		
		buyerAgent = agent;
		
		JPanel panel = new JPanel(new GridLayout());
		JTextField cpuTxtF = new JTextField();
		JTextField ramTxtF = new JTextField();
		JTextField memTxtF = new JTextField();
		JTextField isSSDTxtF = new JTextField();
		JTextField gpuTxtF = new JTextField();
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				searchButton.setEnabled(false);
				proposalsPanel.removeAll();
				initProposals();
				sellerNames.clear();
				sellerPrices.clear();
				sellerLabels.clear();
				sellerChxBx.clear();
				pack();
				//TODO:
				//buyerAgent.startCFP(searchCpuType, searchRamCap, searchMemCap, price);
			}
		});
		panel.add(searchButton);
		
		buyButton = new JButton("BUY!");
		buyButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				buyButton.setEnabled(false);
				Map<String, Boolean> accepted = new HashMap<>();
				for(int i = 0; i< sellerNames.size(); i++)
				{
					accepted.put(sellerNames.get(i), 
							sellerChxBx.get(i).isSelected());
				}
				buyerAgent.finish(accepted);
			}
		});
		buyButton.setEnabled(false);
		panel.add(buyButton);
		getContentPane().add(panel, BorderLayout.NORTH);
		
		proposalsPanel = new JPanel(new GridLayout(0, 4));
		initProposals();
		getContentPane().add(proposalsPanel, BorderLayout.CENTER);
		
		addWindowListener(new WindowListener() 
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				buyerAgent.doDelete();
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		setResizable(false);
	}

	@SuppressWarnings("deprecation")
	public void show()
	{
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2 ;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth()/2, centerY - getHeight() / 2);
		super.show();
	}
	
	protected void initProposals() 
	{
		proposalsPanel.add(new JLabel("Seller Name"));
		proposalsPanel.add(new JLabel("Seller Price"));
		proposalsPanel.add(new JLabel("Seller Add to Cart"));
		proposalsPanel.add(new JLabel("Result info"));
		
	}

	public void addProposal(String name, int price) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				sellerNames.add(name);
				sellerPrices.add(price);
				proposalsPanel.add(new JLabel(name));
				proposalsPanel.add(new JLabel(Integer.toString(price)));
				JCheckBox checkBox = new JCheckBox();
				sellerChxBx.add(checkBox);
				proposalsPanel.add(checkBox);
				JLabel label = new JLabel();
				sellerLabels.add(label);
				proposalsPanel.add(label);
				pack();
			}
		});
	}

	public void endProposals() 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				buyButton.setEnabled(true);
				
			}
		});
		
	}

	public void endResults()
	{
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				searchButton.setEnabled(true);
				buyButton.setEnabled(false);
			}
		});
	}
	
	public void receiveResult(String name, String info) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				for(int i = 0; i < sellerNames.size(); i++)
				{
					if(sellerNames.get(i).equals(name))
					{
						sellerLabels.get(i).setText(info);
					}
				}
				
			}
		});
		
	}

}

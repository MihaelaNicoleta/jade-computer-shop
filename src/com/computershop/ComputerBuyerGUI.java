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
import java.awt.GridBagLayout;


public class ComputerBuyerGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private ComputerBuyerAgent buyerAgent;
	private List<String> sellerNames = new ArrayList<>();
	private List<Integer> sellerPrices =  new ArrayList<>();
	private List<JCheckBox> sellerChxBx = new ArrayList<>();
	private List<JLabel> sellerLabels = new ArrayList<>();
	
	private JPanel proposalsPanel;
	private JButton searchButton;
	private JButton buyButton;
	
	public ComputerBuyerGUI(ComputerBuyerAgent agent) {
		super(agent.getLocalName());
		
		buyerAgent = agent;
		
		JPanel panel = new JPanel(new GridBagLayout());
		JTextField cpuTxtF = new JTextField(15);
		JTextField ramTxtF = new JTextField(5);
		JTextField memTxtF = new JTextField(5);
		JTextField priceTxtF = new JTextField(7);
		
		panel.add(new JLabel("Processor"));
		panel.add(cpuTxtF);
		
		panel.add(new JLabel("RAM capacity (GB)"));
		panel.add(ramTxtF);
		
		panel.add(new JLabel("Memory (GB)"));
		panel.add(memTxtF);
		
		panel.add(new JLabel("Max Price"));
		panel.add(priceTxtF);
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//searchButton.setEnabled(false);
				proposalsPanel.removeAll();
				initProposals();
				sellerNames.clear();
				sellerPrices.clear();
				sellerLabels.clear();
				sellerChxBx.clear();
				pack();
				buyerAgent.startCFP(cpuTxtF.getText(), 
						Integer.parseInt(ramTxtF.getText()), 
						Integer.parseInt(memTxtF.getText()),
						Integer.parseInt(priceTxtF.getText()));
				
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
					accepted.put(sellerNames.get(i), sellerChxBx.get(i).isSelected());
				}
				buyerAgent.finish(accepted);
			}
		});
		buyButton.setEnabled(false);
		panel.add(buyButton);
		getContentPane().add(panel, BorderLayout.NORTH);
		
		proposalsPanel = new JPanel(new GridLayout(0, 10));
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
			public void windowActivated(WindowEvent e) {			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowOpened(WindowEvent e) {}
			
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
	
	private void initProposals() 
	{
		proposalsPanel.add(new JLabel("Add to Cart"));
		proposalsPanel.add(new JLabel("Seller"));
		proposalsPanel.add(new JLabel("Price"));
		proposalsPanel.add(new JLabel("CPU type"));
		proposalsPanel.add(new JLabel("GPU type"));
		proposalsPanel.add(new JLabel("RAM Cap"));
		proposalsPanel.add(new JLabel("Memory cap"));
		proposalsPanel.add(new JLabel("SSD"));		
		proposalsPanel.add(new JLabel("Stock"));
		proposalsPanel.add(new JLabel("Result info"));		
	}

	public void addProposal(String name, String[] content) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				sellerNames.add(name);
				sellerPrices.add(Integer.parseInt(content[5]));
				JCheckBox checkBox = new JCheckBox();
				sellerChxBx.add(checkBox);
				proposalsPanel.add(checkBox);
				
				proposalsPanel.add(new JLabel(name));
				proposalsPanel.add(new JLabel(content[5]));
				
				proposalsPanel.add(new JLabel(content[0]));
				proposalsPanel.add(new JLabel(content[1]));
				proposalsPanel.add(new JLabel(content[2]));
				proposalsPanel.add(new JLabel(content[3]));
				proposalsPanel.add(new JLabel(content[4]));
				proposalsPanel.add(new JLabel(content[6]));
				
				
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

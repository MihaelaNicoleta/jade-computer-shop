package com.computershop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

public class ComputerSellerGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private ComputerSellerAgent sellerAgent;
	private ComputerTableModel computerTableModel;
	
	private JTextField cpuField, gpuField, ramField, memoryField, ssdField, stockField, priceField;
	
	
	ComputerSellerGUI(ComputerSellerAgent sellerAgent) {
		super(sellerAgent.getLocalName());		
		this.sellerAgent = sellerAgent;
		
		/* Add GUI elements */
		JPanel jPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		jPanel.add(new JLabel("CPU:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		cpuField = new JTextField(15);
		jPanel.add(cpuField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		jPanel.add(new JLabel("GPU:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gpuField = new JTextField(15);
		jPanel.add(gpuField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		jPanel.add(new JLabel("RAM:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		ramField = new JTextField(15);
		jPanel.add(ramField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		jPanel.add(new JLabel("Memmory:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		memoryField = new JTextField(15);
		jPanel.add(memoryField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		jPanel.add(new JLabel("Has SSD:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		ssdField = new JTextField(15);
		jPanel.add(ssdField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		jPanel.add(new JLabel("Stock:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		stockField = new JTextField(15);
		jPanel.add(stockField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		jPanel.add(new JLabel("Price:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		priceField = new JTextField(15);
		jPanel.add(priceField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 10, 0);
		
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {				
					String cpuType = cpuField.getText().trim();
					String gpuType = gpuField.getText().trim();
					String ramCapacityString = ramField.getText().trim();
					String memCapacityString = memoryField.getText().trim();
					String hasSSDString = ssdField.getText().trim();
					String stockString = stockField.getText().trim();
					String priceString = priceField.getText().trim();
					
					boolean hasSSD = false;
					if(hasSSDString.equalsIgnoreCase("yes")) {
						hasSSD = true;
					}
					
					int price = Integer.parseInt(priceString);
					int ramCapacity = Integer.parseInt(ramCapacityString);
					int memCapacity = Integer.parseInt(memCapacityString);
					int stock = Integer.parseInt(stockString);	
					
					cpuField.setText("");
					gpuField.setText("");
					ramField.setText("");
					memoryField.setText("");
					ssdField.setText("");
					stockField.setText("");
					priceField.setText("");
					
					Computer computer = new Computer(cpuType, gpuType, ramCapacity, memCapacity, hasSSD, price);
					computer.setStock(stock);
					
					sellerAgent.addComputer(computer);
					
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(ComputerSellerGUI.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		jPanel.add(addButton, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 0);
		
		computerTableModel = new ComputerTableModel();
		JTable jTable = new JTable(computerTableModel);
		
		jTable.getTableHeader().setReorderingAllowed(false);
		jTable.getTableHeader().setResizingAllowed(false);		

		JScrollPane jScrollPane = new JScrollPane(jTable);
		Dimension preferredSize = new Dimension();
		preferredSize.setSize(900, 100);
		jScrollPane.setPreferredSize(preferredSize);
		jPanel.add(jScrollPane, gbc);
		
		getContentPane().add(jPanel, BorderLayout.CENTER);
		
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sellerAgent.doDelete();
			}
		} );
		
		setResizable(true);
	}
	
	public void show() { 
		
	}	
	
	public void addComputer(final Computer computer) { 
		
	}
	
	public void incrementSalesAndDecreaseStockForComputer(final String cpuType, final int ramCapacity, final int memCapacity, final int price) {
		
	}
	
	public void incrementRefusalsForComputer(final String cpuType, final int ramCapacity, final int memCapacity, final int price) {
		
	}
	
	
}

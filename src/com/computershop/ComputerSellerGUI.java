package com.computershop;

import javax.swing.JFrame;

public class ComputerSellerGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private ComputerSellerAgent sellerAgent;
	
	
	ComputerSellerGUI(ComputerSellerAgent sellerAgent) {
		super(sellerAgent.getLocalName());
		
		this.sellerAgent = sellerAgent;
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

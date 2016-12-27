package com.computershop;

import java.util.Collection;

public class Computer {

	private String cpuType;
	private int ramCapacity;
	private boolean hasSSD;
	private int memCapacity;
	private String gpuType;
	
	private int stock;
	private int refusals;
	private int sales;
	private int price;
	
	/*********************************************getter and setter**/
	public String getCpuType() {
		return cpuType;
	}
	public void setCpuType(String cpuType) {
		this.cpuType = cpuType;
	}
	public int getRamCapacity() {
		return ramCapacity;
	}
	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}
	public boolean isHasSSD() {
		return hasSSD;
	}
	public void setHasSSD(boolean hasSSD) {
		this.hasSSD = hasSSD;
	}
	public int getMemCapacity() {
		return memCapacity;
	}
	public void setMemCapacity(int memCapacity) {
		this.memCapacity = memCapacity;
	}
	public String getGpuType() {
		return gpuType;
	}
	public void setGpuType(String gpuType) {
		this.gpuType = gpuType;
	}
	
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	/*constructor*/
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getRefusals() {
		return refusals;
	}
	public void setRefusals(int refusals) {
		this.refusals = refusals;
	}
	public int getSales() {
		return sales;
	}
	public void setSales(int sales) {
		this.sales = sales;
	}
	/*********************************************end getter and setter*/
	public void incrementSalesAndDecreaseStock()
	{
		this.sales++;
		this.stock--;
	}
	
	public void incrementRefusals() 
	{
		this.refusals++;
	}
	
	/*constructor*/
	public Computer(String cpuType, String gpuType, int ramCapacity, int memCapacity, 
			boolean hasSSD, int price)
	{
		this.cpuType = cpuType;
		this.gpuType = gpuType;
		this.ramCapacity = ramCapacity;
		this.memCapacity = memCapacity;
		this.hasSSD = hasSSD;
		this.price = price;
		
		this.sales = 0;
		this.refusals = 0;
	}
	
	public static Computer findComputer(Collection<Computer> computers, String cpuType, int ramCapacity, int memCapacity, int price) {
		
		for(Computer pc : computers)
		{
			if(pc.stock > 0)
			{
				if(pc.cpuType.equalsIgnoreCase(cpuType) && 
				   pc.ramCapacity >= ramCapacity &&
				   pc.memCapacity >= memCapacity &&
				   pc.price <= price)
				{
					return pc;
				}
			}
		}
		
		return null;
	}
}

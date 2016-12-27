package com.computershop;

import java.util.List;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ComputerTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<Computer> computers = new ArrayList<Computer>();
	
	public static final String[] COLUMN_NAMES = {
			"No", "CPU Type", "GPU Type", "RAM Capacity", "Memory Capacity", "Has SSD?", "Price"};

	public static final int COLUMN_COUNT = COLUMN_NAMES.length;
	public static final int NO_COLUMN = 0;
	public static final int CPU_COLUMN = 1;
	public static final int GPU_COLUMN = 2;
	public static final int RAM_COLUMN = 3;
	public static final int MEMORY_COLUMN = 4;
	public static final int SSD_COLUMN = 5;
	public static final int PRICE_COLUMN = 6;
	public static final int STOCK_COLUMN = 7;
	public static final int SALES_COLUMN = 8;
	public static final int REFUSALS_COLUMN = 9;
	
	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public int getRowCount() {
		return computers.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Computer computer = computers.get(row);
		
		switch(column) {		
			case NO_COLUMN: {
				return row + 1;
			}
			case CPU_COLUMN: {
				return computer.getCpuType();
			}
			case GPU_COLUMN: {
				return computer.getGpuType();
			}
			case RAM_COLUMN: {
				return computer.getRamCapacity();
			}
			case MEMORY_COLUMN: {
				return computer.getMemCapacity();
			}
			case SSD_COLUMN: {
				return computer.getHasSSD();
			}
			case STOCK_COLUMN: {
				return computer.getStock();
			}
			case PRICE_COLUMN: {
				return computer.getPrice();
			}
			case SALES_COLUMN: {
				return computer.getSales();
			}
			case REFUSALS_COLUMN: {
				return computer.getRefusals();
			}		
		}	
		return null;		
	}
	
	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}
	
	public void addComputer(Computer computer) {
		computers.add(computer);
		fireTableDataChanged();
	}
	
	public void incrementSalesAndDecreaseStockForComputer(final String cpuType, final int ramCapacity, final int memCapacity, final int price) {
		Computer computer = Computer.findComputer(computers, cpuType, ramCapacity, memCapacity, price);
		
		if(computer != null) {
			computer.incrementSalesAndDecreaseStock();
			fireTableDataChanged();
		}
		
	}
	
	public void incrementRefusalsForComputer(final String cpuType, final int ramCapacity, final int memCapacity, final int price) {
		Computer computer = Computer.findComputer(computers, cpuType, ramCapacity, memCapacity, price);
		
		if(computer != null) {
			computer.incrementRefusals();
			fireTableDataChanged();
		}
	}
	
}

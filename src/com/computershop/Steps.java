package com.computershop;

public enum Steps {
	RequestAllSellerOffers (0),
	ReceiveOffers  (1),
	RequestToBuy 	(2),
	ReceviveSellACK (3),
	Done			(4);
	
	
	private int step;
	
	Steps(int step){
		this.step = step;
	}
	
}

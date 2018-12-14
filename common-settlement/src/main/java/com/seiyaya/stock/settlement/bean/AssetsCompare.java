package com.seiyaya.stock.settlement.bean;

import lombok.Data;

@Data
public class AssetsCompare {
	
	private int accountId;
	private String marketId;
	private String stockCode;
	private int totalQty;
	
	private double freezedBalance;
	private double currentBalance;
}

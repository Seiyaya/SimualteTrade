package com.seiyaya.common.bean;

import lombok.Data;

@Data
public class Bargain {
	
	private int bargainId;
	private int orderId;
	private int accountId;
	private String execDate;
	private String execTime;
	private String marketId;
	private String stockCode;
	private String stockName;
	private String tradeType;
	private int execQty;
	private double execPrice;
	private double stapTax;
	private double commission;
	private double fare;
	private double totalBalance;
	private String remark;
	private double transferFree;
	private double totalFare;
	private int holdId;
	
	/*************非持久化属性***************/
	private String tradeTypeName;
	private String tradeStatus;
	private double orderBalance;
	
	public boolean isSell() {
		return EnumValue.TRADE_TYPE_1.equals(tradeType);
	}
	
	public boolean isBuy() {
		return EnumValue.TRADE_TYPE_0.equals(tradeType);
	}
}

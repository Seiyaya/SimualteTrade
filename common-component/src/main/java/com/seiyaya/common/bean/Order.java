package com.seiyaya.common.bean;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class Order {
	
	public static final String TRADE_TYPE_BUY = "0";
	
	public static final String TRADE_TYPE_SELL = "1";
	
	public static final String LIMITPRICE = "limitprice";
	
	public static final String MARKETPRICE = "marketprice";
	
	private int orderId;
	private int accountId;
	private String orderDate;
	private String orderTime;
	private String marketId;
	private String stockCode;
	private String stockName;
	private String tradeType;
	private int orderQty;
	private double orderPrice;
	private double orderBalance;
	private double totalBalance;
	private double stapTax =0 ;
	private double fare = 0;
	private double transferFare = 0;
	private double commission = 0;
	private double totalFare = 0;
	private String orderType = LIMITPRICE;
	private String tradeStatus;
	private String dealFlag;
	private int matchNo = 0;
	private String stockType;
	
	
	/**************非持久化属性*******************/
	private String tradeTypeName;
	private String tradeStatusName;
	
	public boolean isSell() {
		return TRADE_TYPE_SELL.equals(tradeType);
	}
	
	public boolean isBuy() {
		return TRADE_TYPE_BUY.equals(tradeType);
	}
	
	public boolean isLimitPrice() {
		return LIMITPRICE.equals(orderType);
	}
	
	public boolean isMarketPrice() {
		return MARKETPRICE.equals(orderType);
	}
	
	public double calcTotalBalance(){
		if(isBuy()) {
			return orderBalance + totalFare;
		}else if(isSell()) {
			return orderBalance - totalFare;
		}
		return 0;
	}
	
	public boolean validate() {
		return accountId!=0 && 
				StringUtils.isNotBlank(marketId) && 
				StringUtils.isNotBlank(stockCode) && 
				orderQty>0 &&
				orderPrice>0 &&
				StringUtils.isNotBlank(tradeType);
	}

	public Order(int accountId, String marketId, String stockCode) {
		this.accountId = accountId;
		this.marketId = marketId;
		this.stockCode = stockCode;
	}
	
	public Order() {
	}
}

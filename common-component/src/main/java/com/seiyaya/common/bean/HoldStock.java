package com.seiyaya.common.bean;

import lombok.Data;

/**
 * 持仓
 * @author Seiyaya
 *
 */
@Data
public class HoldStock {
	private int holdId;
	private int accountId;
	private String marketId;
	private String stockCode;
	private int totalQty;
	private int currentQty;
	private double costPrice;
	private double holdPrice;
	private String createDate;
	private String updateDate;
	private String remark;
	private String version;
	
	/****************非持久化属性******************/
	private String stockName;
	private double yesterday;
	private double marketBalance;
	private double profit;
	private double profitRate;
	private double dayPercentage;
	private double nowPrice;
	private double position;
}

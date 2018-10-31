package com.seiyaya.common.bean;

import lombok.Data;

@Data
public class Stock {
	private String stockCode;
	private String marketId;
	private double nowPrice;
	private double dayPercentage;
	private String tradeDate;
	private String stockName;
	private String remark;
	private double limitDown;
	private double limitUp;
	private double open;
	private double yesterday;
	private String stockType;
	private double high;
	private double low;
	private double up;//涨跌
	private String pyName;//拼音name
	private String issuspend;//是否停牌（0退市1停牌 2非停牌3未上市4.暂停上市）
}

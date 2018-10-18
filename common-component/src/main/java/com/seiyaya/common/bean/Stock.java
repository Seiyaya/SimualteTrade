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
}

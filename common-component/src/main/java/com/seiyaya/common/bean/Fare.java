package com.seiyaya.common.bean;


import lombok.Data;

@Data
public class Fare {
	
	//股票类型
	private String stockType;
	//费率类型
	private String fareType;
	//费率值
	private double fareValue;
}

package com.seiyaya.common.bean;

import lombok.Data;

@Data
public class HQStatus {
	private int stockNum;
	private String hqInitDate;
	private String dbfTime;
	private String hqServerTime;
	private String hkHQTime;
}

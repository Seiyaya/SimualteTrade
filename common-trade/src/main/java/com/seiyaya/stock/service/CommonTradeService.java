package com.seiyaya.stock.service;

public interface CommonTradeService {
	
	public String querySysConfigValue(String key);
	public boolean isSettlementTime();
}

package com.seiyaya.stock.service;

public interface CommonTradeService {
	
	/**
	 * 查询系统配置
	 * @param key
	 * @return
	 */
	public String querySysConfigValue(String key);
	
	/**
	 * 判断是否在清算时间
	 * @return
	 */
	public boolean isSettlementTime();
	
	/**
	 * 修改系统配置
	 * @param settleFlag
	 * @param string
	 */
	public void updateSysConfig(String settleFlag, String string);
}

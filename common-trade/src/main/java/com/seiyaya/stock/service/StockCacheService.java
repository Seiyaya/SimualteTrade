package com.seiyaya.stock.service;

import com.seiyaya.common.bean.Stock;

/**
 * 
 * @author Seiyaya
 *
 */
public interface StockCacheService {
	
	/**
	 * 初始化基础数据
	 */
	public void initConfigCache();
	
	/**
	 * 初始化股票缓存
	 */
	public void initStockCache();

	public Stock getStockByKey(String key);

	/**
	 * 是否是交易日
	 * @param nowDate
	 * @return
	 */
	public boolean isTradeDate(String nowDate);

	/**
	 * 获取交易的手续费
	 * @param commission
	 * @return
	 */
	public double getTradeFare(String commission);
}

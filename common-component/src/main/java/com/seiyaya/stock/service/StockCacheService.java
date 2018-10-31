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

	/**
	 * 根据key获取股票信息
	 * @param key
	 * @return
	 */
	public Stock getStockByKey(String key);

	/**
	 * 是否是交易日
	 * @param nowDate
	 * @return
	 */
	public boolean isTradeDate(String nowDate);

	/**
	 * 今天是否是交易日
	 * @return
	 */
	public boolean isTradeDate();
	
	/**
	 * 获取交易的手续费
	 * @param commission
	 * @return
	 */
	public double getTradeFare(String commission);
	
	/**
	 * 获取系统配置
	 * @param key
	 * @return
	 */
	public String getSysConfig(String key);

	/**
	 * 下载行业数据并入库
	 */
	public void downloadIndustryInfo();

	/**
	 * 下载指数信息入库
	 */
	public void downloadExponentInfo();
}

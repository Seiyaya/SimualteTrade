package com.seiyaya.stock.service;

import java.util.List;

import com.seiyaya.common.bean.Stock;

/**
 * 
 * @author Seiyaya
 *
 */
public interface StockCacheService {
	
	public static final String BEAN_NAME = "common.commponent.StockCacheService";
	
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
	 * 获取股票信息列表
	 * @return
	 */
	public List<Stock> getStockKeyList();

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
	public double getTradeFare(String commission,String fareType);
	
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

	/**
	 * 是否初始化成功
	 * @return
	 */
	public boolean isInitSuccess();

	/**
	 * 添加股票信息到数据库
	 * @param stockKeyList
	 */
	public void addStockToDb(List<Stock> stockKeyList);

	/**
	 *  当月分红数据下载
	 * @param downloadDate
	 * @param today
	 */
	public void downloadCurrentBonus(String downloadDate, String today);

	/**
	 * 历史分红数据
	 * @param firstdayByMonth
	 * @param downloadDate
	 */
	public void downloadHistBonusData(String firstdayByMonth, String downloadDate);

	/**
	 * 获取枚举值
	 * @param itemType
	 * @param itemValue
	 * @return
	 */
	public String getEnumValue(String itemType, String itemValue);

	/**
	 * 根据股票代码获取股票信息，主要是可交易的
	 * @param stockCode
	 * @return
	 */
	public Stock getStockByCode(String stockCode);

	/**
	 * 获取五档信息
	 * @param marketId
	 * @param stockCode
	 * @return
	 */
	public Stock getStockFiveByKey(String marketId, String stockCode);
}

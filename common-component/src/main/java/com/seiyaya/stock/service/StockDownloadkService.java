package com.seiyaya.stock.service;

public interface StockDownloadkService {
	
	/**
	 * 将雪球下载的股票信息入库
	 * @param stockCode
	 * @return
	 */
	public int insertFundInfoByXueQiu(String stockCode);
	
	/**
	 * 将雪球下载的股票信息入库
	 * @param stockCode
	 * @return
	 */
	public int insertFundInfoByXueQiu(String[] stockCode);
	
	/**
	 * 将雪球下载的股票信息入库
	 * @param stockCode
	 * @return
	 */
	public int insertStockInfoByXueQiu();
}

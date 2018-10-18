package com.seiyaya.common.http;

import java.util.List;

import com.seiyaya.common.bean.Stock;

/**
 * 接口行情访问接口，可以配置雪球、同花顺等的行情
 * @author Seiyaya
 *
 */
public interface HQUtils {
	
	/**
	 * 根据类型获取股票信息
	 * @param type
	 * @return
	 */
	public List<Stock> getStockInfoListByType(String type);
	
	/**
	 * 获取股票信息列表
	 * @return
	 */
	public List<Stock> getStockInfoList();
	
	/**
	 * 获取近一年的基金信息
	 * @param stockCode
	 * @return
	 */
	public List<Stock> getFundInfo(String stockCode);
}

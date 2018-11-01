package com.seiyaya.common.http;

import java.util.List;

import com.seiyaya.common.bean.HQStatus;
import com.seiyaya.common.bean.Industry;
import com.seiyaya.common.bean.Stock;

/**
 * 接口行情访问接口，可以配置雪球、同花顺等的行情
 * @author Seiyaya
 *
 */
public interface HQUtils {
	
	/**
	 * 获取股票信息列表
	 * @return
	 */
	public List<Stock> getStockInfoList();

	/**
	 * 下载行业数据
	 * @return
	 */
	public List<Industry> getIndustryList();

	/**
	 * 根据marketId1:stockCode1|marketId2:stockCode2获取股票信息
	 * @param exponentList
	 * @return
	 */
	public List<Stock> getStockInfo(String exponentList);

	/**
	 * 获取行情的状态
	 * @return
	 */
	public HQStatus getStatus();
}

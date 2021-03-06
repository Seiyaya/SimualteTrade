package com.seiyaya.stock.task;

import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * stock相关定时任务
 * @author Seiyaya
 *
 */
@Slf4j
public class StockTask {
	
	/**
	 * 更新行情
	 */
	public void updateStockHQ() {
		StockCacheService cacheService = SpringUtils.getBean(StockCacheService.BEAN_NAME, StockCacheService.class);
		log.info("======初始化行情 start======");
		cacheService.initStockCache();
		log.info("======初始化行情 end========");
	}
	
	public void addStockDB() {
		StockCacheService cacheService = SpringUtils.getBean(StockCacheService.BEAN_NAME, StockCacheService.class);
		log.info("======初始化行情 start======");
		cacheService.initStockCache();
		if(!cacheService.isInitSuccess()) {
			return ;
		}
		cacheService.addStockToDb(cacheService.getStockKeyList());
		log.info("======初始化行情 end========");
	}
}

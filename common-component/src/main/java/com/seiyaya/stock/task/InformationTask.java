package com.seiyaya.stock.task;

import java.util.List;

import com.seiyaya.common.bean.Industry;
import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * 资讯相关任务
 * @author Seiyaya
 *
 */
@Slf4j
public class InformationTask {
	
	/**
	 * 更新行业信息
	 */
	public void updateIndustryInfo() {
		log.info("======初始化行业信息 start======");
		StockCacheService cacheService = SpringUtils.getBean("", StockCacheService.class);
		cacheService.downloadIndustryInfo();
		log.info("======初始化行业信息 end========");
	}
	
	/**
	 * 更新指数信息
	 */
	public void updateExponentInfo() {
		log.info("======初始化指数信息 start======");
		StockCacheService cacheService = SpringUtils.getBean("", StockCacheService.class);
		cacheService.downloadExponentInfo();
		log.info("======初始化指数信息 end========");
	}
}

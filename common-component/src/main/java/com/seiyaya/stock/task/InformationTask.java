package com.seiyaya.stock.task;

import java.util.Date;

import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.common.utils.DateUtils;
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
	
	StockCacheService cacheService = SpringUtils.getBean(StockCacheService.BEAN_NAME, StockCacheService.class);
	
	/**
	 * 更新行业信息
	 */
	public void updateIndustryInfo() {
		log.info("======初始化行业信息 start======");
		cacheService.downloadIndustryInfo();
		log.info("======初始化行业信息 end========");
	}
	
	/**
	 * 更新指数信息
	 */
	public void updateExponentInfo() {
		log.info("======初始化指数信息 start======");
		cacheService.downloadExponentInfo();
		log.info("======初始化指数信息 end========");
	}
	
	/**
	 * 下载分红数据
	 */
	public void updateBonus() {
		String today = DateUtils.formatNowDate();
		String downloadDate = cacheService.getSysConfig(SystemConfig.BONUS_LAST_DATE);
		String downmonth = downloadDate.substring(0, 6);
		if(today.contains(downloadDate)) {
			if(today.equals(downmonth)) {
				log.warn("{}日已下载过数据，不用再下载",today);
				return;
			}
			//当月
			cacheService.downloadCurrentBonus(downloadDate,today);
		}else {
			//次月
			cacheService.downloadCurrentBonus(downloadDate,today);
			cacheService.downloadHistBonusData(DateUtils.getFirstdayByMonth(new Date()),downloadDate);
		}
	}
}

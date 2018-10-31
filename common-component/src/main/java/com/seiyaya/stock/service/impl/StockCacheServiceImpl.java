package com.seiyaya.stock.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seiyaya.common.bean.Industry;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.common.http.CompanyHQUtils;
import com.seiyaya.common.http.HQUtils;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.stock.mapper.SystemConfigMapper;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的缓存方式，内存缓存
 * 
 * @author Seiyaya
 *
 */
@Service
@Slf4j
public class StockCacheServiceImpl implements StockCacheService {
	
	@Autowired
	private SystemConfigMapper systemConfigMapper;
	
	private HQUtils hqUtils;
	
	//非交易日
	private static Set<String> holiDaySet = new HashSet<>();

	//系统配置
	private static Map<String, String> defaultConfig = new HashMap<>();
	
	//股票信息
	private static Map<String,Stock>    stockKeyMap  = new ConcurrentHashMap<>();
	
	//股票信息
	private static List<Stock>   stockKeyList = new ArrayList<>();

	@Override
	@PostConstruct
	public void initConfigCache() {
		initSystemConfigCache();
		initHolidayCache();
		hqUtils = new CompanyHQUtils(defaultConfig.get(SystemConfig.HQ_URL),this);
		initStockCache();
	}
	
	private void initHolidayCache() {
		List<String> holidayList = systemConfigMapper.queryHolidayList();
		if(holidayList!=null && !holidayList.isEmpty()) {
			holiDaySet.clear();
			log.info("非交易日项大小:{}",holidayList.size());
			holidayList.forEach((holiday) -> {
				holiDaySet.add(holiday);
			});
		}else {
			log.error("非交易日列表为空，请检查t_sim_date表是否有数据非交易日的数据");
		}
	}

	private void initSystemConfigCache() {
		List<SystemConfig> configList = systemConfigMapper.querySystemConfigList();
		if(configList!=null && !configList.isEmpty()) {
			defaultConfig.clear();
			log.info("系统配置项大小:{}",configList.size());
			configList.forEach((config) -> {
				defaultConfig.put(config.getKey(), config.getValue());
			});
		}else {
			log.error("系统配置为空，请检查t_sim_sys_config表是否有数据");
		}
	}

	@Override
	public void initStockCache() {
		List<Stock> stockInfoList = hqUtils.getStockInfoList();
		if(stockInfoList!=null && !stockInfoList.isEmpty()) {
			List<Stock> stockList = new ArrayList<>();
			Map<String,Stock> stockMap = new ConcurrentHashMap<>();
			stockInfoList.forEach((stock) -> {
				stockList.add(stock);
				stockMap.put(stock.getMarketId()+stock.getStockCode(), stock);
			});
			Map<String,Stock> tmpMap = stockKeyMap;
			List<Stock> tmpList = stockKeyList;
			stockKeyList = stockList;
			stockKeyMap = stockMap;
			tmpMap.clear();
			tmpList.clear();
		}else {
			log.error("股票信息数据为空，请确认{}出入参是否变更",hqUtils.getClass());
		}
	}

	@Override
	public Stock getStockByKey(String key) {
		return stockKeyMap.get(key);
	}

	@Override
	public boolean isTradeDate(String nowDate) {
		return !holiDaySet.contains(nowDate);
	}
	
	@Override
	public boolean isTradeDate() {
		return isTradeDate(DateUtils.formatNowDate());
	}

	@Override
	public double getTradeFare(String commission) {
		return Double.parseDouble(defaultConfig.get(commission));
	}

	@Override
	public String getSysConfig(String key) {
		return defaultConfig.get(key);
	}

	@Override
	public void downloadIndustryInfo() {
		List<Industry> industry = hqUtils.getIndustryList();
		systemConfigMapper.deleteIndustry();
		systemConfigMapper.batchAddIndustry(industry);
	}

	@Override
	public void downloadExponentInfo() {
		if(!isTradeDate()) {
			log.info("非交易日，不需要采集指数信息");
			return ;
		}
		String exponentList = getSysConfig(SystemConfig.EXPONENT_LIST);
		if(StringUtils.isEmpty(exponentList)) {
			log.error("没有配置需要采集的指数信息");
			return ;
		}
		log.info("采集的指数信息:{}",exponentList);
		List<Stock> stockList = hqUtils.getStockInfo(exponentList);
		systemConfigMapper.batchAddExponent(stockList);
	}
}

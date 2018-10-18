package com.seiyaya.stock.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.common.http.XueQiuUtils;
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
	
	@Autowired
	private XueQiuUtils xueQiuUtils;
	
	//非交易日
	private static Set<String> holiDaySet = new HashSet<String>();

	//系统配置
	private static Map<String, String> defaultConfig = new HashMap<>();
	
	//股票信息
	private static Map<String,Stock>    stockKeyMap  = new ConcurrentHashMap<String,Stock>();
	
	//股票信息
	private static List<Stock>   stockKeyList = new ArrayList<Stock>();

	@Override
	public void initConfigCache() {
		initSystemConfigCache();
		initHolidayCache();
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
		List<Stock> stockInfoList = xueQiuUtils.getStockInfoList();
		if(stockInfoList!=null && !stockInfoList.isEmpty()) {
			log.info("股票信息项大小:{}",stockInfoList.size());
			stockKeyList.clear();
			stockKeyMap.clear();
			stockInfoList.forEach((stock) -> {
				stockKeyList.add(stock);
				stockKeyMap.put(stock.getMarketId()+stock.getStockCode(), stock);
			});
		}else {
			log.error("股票信息数据为空，请确认{}出入参是否变更",xueQiuUtils.getClass());
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
	public double getTradeFare(String commission) {
		return Double.parseDouble(defaultConfig.get(commission));
	}
}

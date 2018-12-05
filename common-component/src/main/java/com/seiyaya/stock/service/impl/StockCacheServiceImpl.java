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

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.Fare;
import com.seiyaya.common.bean.FreeRate;
import com.seiyaya.common.bean.HQStatus;
import com.seiyaya.common.bean.Industry;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.common.http.CompanyHQUtils;
import com.seiyaya.common.http.HQUtils;
import com.seiyaya.common.http.InformationDownload;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.stock.mapper.StockMapper;
import com.seiyaya.stock.mapper.SystemConfigMapper;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的缓存方式，内存缓存
 * 
 * @author Seiyaya
 *
 */
@Service(value = StockCacheService.BEAN_NAME)
@Slf4j
public class StockCacheServiceImpl implements StockCacheService {
	
	@Autowired
	private SystemConfigMapper systemConfigMapper;
	
	@Autowired
	private StockMapper stockMapper;
	
	private HQUtils hqUtils;
	
	//非交易日
	private static Set<String> holiDaySet = new HashSet<>();

	//系统配置
	private static Map<String, String> defaultConfig = new HashMap<>();
	
	//费率信息
	private static Map<String,Double> fareConfig = new HashMap<>();
	
	//股票信息, market_id+stock_code为key   直接从行情获取
	private static Map<String,Stock>    stockKeyMap  = new ConcurrentHashMap<>();
	
	//数据库的股票信息  直接从数据库获取 key是stock_code
	private static Map<String,Stock>    stockCodeMap  = new ConcurrentHashMap<>();
	
	//股票信息
	private static List<Stock>   stockKeyList = new ArrayList<>();
	
	//数据字典项   trade_type:{0:证券买入,1:证券卖出}
	private static Map<String,Map<String,String>> enumValue = new HashMap<>();

	@Override
	public List<Stock> getStockKeyList() {
		return stockKeyList;
	}
	
	@Override
	@PostConstruct
	public void initConfigCache() {
		initSystemConfigCache();
		initHolidayCache();
		hqUtils = new CompanyHQUtils(defaultConfig.get(SystemConfig.HQ_URL),this);
		initStockCache();
		initFareCache();
		initEnumCache();
		initDBStock();
	}
	
	/**
	 * 初始化数据库的股票信息到内存
	 */
	private void initDBStock() {
		List<Stock> stockList = stockMapper.queryStockList();
		Map<String,Stock> stockMap  = new ConcurrentHashMap<>();
		if(stockList!=null && !stockList.isEmpty()) {
			stockList.forEach((stock) -> {
				stockMap.put(stock.getStockCode(), stock);
			});
			Map<String, Stock> tmp = stockCodeMap;
			stockCodeMap = stockMap;
			tmp.clear();
		}else {
			log.error("DB没有相应的股票信息");
		}
	}

	/**
	 * 初始化数据字典缓存
	 */
	private void initEnumCache() {
		List<DBParam> list = systemConfigMapper.queryEnumValue();
		if(list!=null && !list.isEmpty()) {
			Map<String,Map<String,String>> parentMap = new HashMap<>();
			list.forEach((param) -> {
				Map<String, String> sonMap = parentMap.get(param.getString("enum_key"));
				if(sonMap == null || sonMap.isEmpty()) {
					sonMap = new HashMap<>();
					sonMap.put(param.getString("item_value"), param.getString("item_name"));
					parentMap.put(param.getString("enum_key"), sonMap);
				}else {
					sonMap.put(param.getString("item_value"), param.getString("item_name"));
				}
			});
			Map<String, Map<String, String>> tmp = enumValue;
			enumValue = parentMap;
			tmp.clear();
		}
	}

	/**
	 * 初始化费率
	 */
	private void initFareCache() {
		List<Fare> list = systemConfigMapper.queryFareList();
		if(list!=null && !list.isEmpty()) {
			log.info("费率配置项大小:",list.size());
			list.forEach((fare) -> {
				fareConfig.put(fare.getStockType()+":"+fare.getFareType(), fare.getFareValue());
			});
		}else {
			log.info("未配置费率信息");
		}
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
	public double getTradeFare(String commission,String fareType) {
		Double commissionValue = fareConfig.get(commission);
		if(commissionValue == null) {
			if(FreeRate.COMMISSION.equals(fareType)) {
				double value = Double.parseDouble(defaultConfig.get(SystemConfig.DEFAULT_COMMISSION));
				return value;
			}
			
			if(FreeRate.STAPTAX.equals(fareType)) {
				return Double.parseDouble(defaultConfig.get(SystemConfig.DEFAULT_STAPTAX));
			}
			
			if(FreeRate.TRANSFER_FREE.equals(fareType)) {
				return Double.parseDouble(defaultConfig.get(SystemConfig.DEFAULT_TRANSFER_FREE));
			}
			return 0;
		}
		return commissionValue;
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

	@Override
	public boolean isInitSuccess() {
		HQStatus status = hqUtils.getStatus();
		String date = DateUtils.formatNowDate();
		if(!date.equals(status.getHqInitDate()) && !isTradeDate(date)) {
			log.error("行情为初始化，上次初始化的日期:{}",status.getHqInitDate());
			return false;
		}
		return true;
	}

	@Override
	public void addStockToDb(List<Stock> stockKeyList) {
		String date = DateUtils.formatNowDate();
		int num = stockMapper.getStockInfoNum(date);
		if(num <= 0) {
			log.info("{}日初始化，股票信息初始化大小:{}",date,stockKeyList.size());
			stockMapper.deleteStockInfo();
			String now = DateUtils.formatNowDate();
			stockKeyList.forEach((row) -> {
				row.setTradeDate(now);
			});
			stockMapper.insertStockInfoList(stockKeyList);
		}else {
			log.info("{}已经初始化过，不用再初始化",date);
		}
	}

	@Override
	public void downloadCurrentBonus(String downloadDate, String today) {
		int count = DateUtils.getDaysByTwoDate(downloadDate,today);
		while(count >= 0) {
			String down = DateUtils.getNDate(downloadDate, -1);
			log.error(down);
			InformationDownload.checkBonusUrl(down);
			count--;
		}
	}

	@Override
	public void downloadHistBonusData(String firstdayByMonth, String downloadDate) {
	}

	@Override
	public String getEnumValue(String itemType, String itemValue) {
		return enumValue.get(itemType).get(itemValue);
	}

	@Override
	public Stock getStockByCode(String stockCode) {
		return stockCodeMap.get(stockCode);
	}

	@Override
	public Stock getStockFiveByKey(String marketId, String stockCode) {
		//TODO:
		return null;
	}
}

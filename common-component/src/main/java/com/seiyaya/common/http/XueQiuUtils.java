package com.seiyaya.common.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.utils.DateUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class XueQiuUtils implements HQUtils{
	
	@Autowired
	private HttpUtils httpUtils;
	
	public static final String FUND_URL = "https://fund.xueqiu.com/dj/open/fund/growth/";
	
	public static final String FUND_NAME_URL = "https://xueqiu.com/stock/industry/stockList.json";
	
	public static final String FUND_NAME_URL_2 = "https://xueqiu.com/S/";
	
	public static final String STOCK_URL = "https://xueqiu.com/stock/cata/stocklist.json";
	
	public static final String STOCK_URL_2 = "https://xueqiu.com/stock/quote_order.json";
	
	@Override
	public List<Stock> getStockInfoListByType(String type){
		val params = new HashMap<String,Object>();
		params.put("page", 1);
		params.put("size", 99999);
		params.put("exchange", "CN");
		params.put("stockType", type);
		params.put("column", "symbol,name,current,chg,percent,last_close,open,high,low,volume,amount,market_capital,pe_ttm,high52w,low52w,hasexist");
		params.put("orderBy", "percent");
		params.put("order", "desc");
		params.put("_", System.currentTimeMillis());
		String httpResult = httpUtils.sendGet(STOCK_URL_2, params);
		JSONArray array = JSON.parseObject(httpResult).getJSONArray("data");
		val resultList = new ArrayList<Stock>();
		array.forEach((jsonObject) -> {
			if(jsonObject instanceof JSONArray) {
				JSONArray resultJson = (JSONArray) jsonObject;
				Stock stock = new Stock();
				stock.setMarketId(resultJson.getString(0).substring(0, 2));
				stock.setStockCode(resultJson.getString(0).substring(2));
				stock.setNowPrice(resultJson.getDoubleValue(2));
				stock.setDayPercentage(resultJson.getDoubleValue(3));
				stock.setStockName(resultJson.getString(1));
				stock.setTradeDate(DateUtils.formatNowDate());
				resultList.add(stock);
			}
		});
		if(resultList.isEmpty()) {
			log.error("{}变更了参数或者其他原因，请检查!!!板块类型->{}",STOCK_URL_2,type);
		}
		return resultList;
	}
	
	@Override
	public List<Stock> getStockInfoList(){
		int totalPage = 100;
		int page = 1;
		int pageSize = 100;
		val stockList = new ArrayList<Stock>();
		for(int i=page;i<=totalPage;i++) {
			val params = new HashMap<String,Object>();
			params.put("page", i);
			params.put("size", pageSize);
			params.put("type", 11);
			params.put("_", System.currentTimeMillis());
			String sendGet = httpUtils.sendGet(STOCK_URL, params);
			JSONObject resultJson = JSONObject.parseObject(sendGet);
			int totalRows = resultJson.getJSONObject("count").getIntValue("count");
			log.info("total_rows->{} total_page->{} current_page->{}",totalRows,totalPage,i);
			totalPage = totalRows/pageSize;
			if(totalRows%pageSize != 0) {
				totalPage++;
			}
			JSONArray stockArray = resultJson.getJSONArray("stocks");
			stockArray.forEach((innerJson)->{
				Stock stock = new Stock();
				if(innerJson instanceof JSONObject) {
					JSONObject jsonStock = (JSONObject) innerJson;
					stock.setStockCode(jsonStock.getString("code"));
					stock.setNowPrice(jsonStock.getDoubleValue("current"));
					stock.setStockName(jsonStock.getString("name"));
					stock.setMarketId(jsonStock.getString("symbol").substring(0,2));
					stock.setTradeDate(DateUtils.formatNowDate());
					stock.setDayPercentage(jsonStock.getDoubleValue("percent"));
					stockList.add(stock);
				}
			});
		}
		if(stockList.isEmpty()) {
			log.error("{}变更了参数或者其他原因，请检查",STOCK_URL);
		}
		return stockList;
	}
	
	/**
	 * 获取近一年的基金信息
	 * @param stockCode
	 * @return
	 */
	@Override
	public List<Stock> getFundInfo(String stockCode) {
		val params = new HashMap<String,Object>();
		params.put("day", "360");
		String sendGet = httpUtils.sendGet(FUND_URL+stockCode, params);
		JSONArray jsonArray = JSONObject.parseObject(sendGet).getJSONObject("data").getJSONArray("fund_nav_growth");
		val resultList = new ArrayList<Stock>();
		String name = getFundName(stockCode);
		params.clear();
		jsonArray.forEach((json) -> {
			if(json instanceof JSONObject) {
				JSONObject innerJsonObject = (JSONObject) json;
				Stock stock = new Stock();
				stock.setNowPrice(innerJsonObject.getDoubleValue("nav"));
				stock.setTradeDate(innerJsonObject.getString("date"));
				stock.setDayPercentage(innerJsonObject.getDoubleValue("percentage"));
				stock.setMarketId("F");
				stock.setStockCode(stockCode);
				stock.setStockName(name);
				resultList.add(stock);
			}
		});
		return resultList;
	}
	
	/**
	 * 获取基金的名称
	 * @param stockCode
	 * @return
	 */
	public String getFundName(String stockCode) {
		val params = new HashMap<String,Object>();
		params.put("code", "F"+stockCode);
		params.put("type", "1");
		params.put("size", "8");
		String name = JSONObject.parseObject(httpUtils.sendGet(FUND_NAME_URL,params)).getString("stockname");
		if(StringUtils.isBlank(name)) {
			//从页面得到名称
			params.clear();
			name = httpUtils.getElementText(FUND_NAME_URL_2+"F"+stockCode, params, "stock-name");
			name = name.substring(0, name.indexOf("("));
		}
		return name;
	}
}

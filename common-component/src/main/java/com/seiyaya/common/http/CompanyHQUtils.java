package com.seiyaya.common.http;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HQStatus;
import com.seiyaya.common.bean.Industry;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * 公司行情
 * @author Seiyaya
 *
 */
@Slf4j
public class CompanyHQUtils implements HQUtils{

	private String hqUrl;
	
	private StockCacheService stockCacheService;
	
	private HttpUtils httpUtils = HttpUtils.getHttpUtils();
	
	public CompanyHQUtils(String hqUrl,StockCacheService stockCacheService) {
		this.hqUrl = hqUrl;
		this.stockCacheService = stockCacheService;
	}

	@Override
	public List<Stock> getStockInfoList() {
		DBParam params = new DBParam()
				.set("funcno", stockCacheService.getSysConfig(SystemConfig.HQ_FUNCTION))
				.set("version", "1")
				.set("type", stockCacheService.getSysConfig(SystemConfig.HQ_STOCK_TYPE))
				.set("q", "")
				.set("count", "100000")
				.set("field", "24:22:23:21:12:9:10:11:2:1:3:45:46:28:48");
		List<Stock> stockList = getStockData(params);
		log.info("更新的股票数量:{}",stockList.size());
		return stockList;
	}

	@Override
	public List<Industry> getIndustryList() {
		DBParam params = new DBParam()
				.set("version", "1")
				.set("funcno", 20101);
		String resultString = httpUtils.sendGet(hqUrl, params);
		JSONArray industryArray = JSONObject.parseObject(resultString).getJSONArray("results");
		List<Industry> industrykList = new ArrayList<Industry>();
		industryArray.forEach((object)->{
			JSONArray array = (JSONArray) object;
			String name = array.getString(0);
			String code = array.getString(1);
			String type = array.getString(2);
			if("1".equals(type)) {
				JSONArray stockCodes = array.getJSONArray(4);
				stockCodes.forEach((obj)->{
					String innserObject = (String) obj;
					Industry industry = new Industry();
					industry.setIndustryName(name);
					industry.setIndustryCode(code);
					industry.setIndustryType(type);
					industry.setMarketId(innserObject.substring(0, 2));
					industry.setStockCode(innserObject.substring(2));
					industrykList.add(industry);
				});
			}
		});
		log.info("下载的行业数据大小为:{}",industrykList.size());
		return industrykList;
	}

	@Override
	public List<Stock> getStockInfo(String codeList) {
		DBParam params = new DBParam()
				.set("funcno", "20000")
				.set("version", "1")
				.set("stock_list", codeList)
				.set("field", "24:22:23:21:12:9:10:11:2:1:3:45:46:28:48");
		List<Stock> stockList = getStockData(params);
		return stockList;
	}

	/**
	 * 得到股票数据
	 * @param params
	 * @return
	 */
	private List<Stock> getStockData(DBParam params) {
		String resultString = httpUtils.sendGet(hqUrl, params);
		JSONArray stockArray = JSONObject.parseObject(resultString).getJSONArray("results");
		List<Stock> stockList = new ArrayList<Stock>();
		stockArray.forEach((objecct)->{
			JSONArray json = (JSONArray) objecct;
			Stock stock = new Stock();
			stock.setStockCode(json.getString(0));//24
			stock.setStockName(json.getString(1));//22
			stock.setMarketId(json.getString(2));//23
			stock.setStockType(json.getString(3));//21
			stock.setYesterday(json.getDouble(4));//12
			stock.setOpen(json.getDouble(5));//9
			stock.setHigh(json.getDouble(6));//10
			stock.setLow(json.getDouble(7));//11
			stock.setNowPrice(json.getDouble(8));//2
			stock.setDayPercentage(json.getDouble(9));//1
			stock.setUp(json.getDouble(10));//3
			stock.setLimitUp(json.getDouble(11));//45
			stock.setLimitDown(json.getDouble(12));//46
			stock.setPyName(json.getString(13));//28
			stock.setIssuspend(json.getString(14));//48
			stockList.add(stock);
		});
		return stockList;
	}

	@Override
	public HQStatus getStatus() {
		DBParam params = new DBParam()
				.set("version", "1")
				.set("funcno", "29999");
		String resultString = httpUtils.sendGet(hqUrl, params);
		JSONArray array = JSONObject.parseObject(resultString).getJSONArray("results").getJSONArray(0);
		HQStatus status = new HQStatus();
		status.setStockNum(array.getIntValue(0));
		status.setHqInitDate(array.getString(1));
		status.setDbfTime(array.getString(2));
		status.setHqServerTime(array.getString(3));
		status.setHkHQTime(array.getString(4));
		return status;
	}
}

package com.seiyaya.stock.trade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seiyaya.common.bean.ResultBean;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.utils.CheckConditionUtils;
import com.seiyaya.stock.service.StockCacheService;

/**
 * 行情相关
 * @author Seiyaya
 *
 */
@RequestMapping("/stock")
@Controller
public class StockController {
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@GetMapping("/info/query")
	@ResponseBody
	public ResultBean queryStockInfo(String stockCode) {
		Stock stock = stockCacheService.getStockByCode(stockCode);
		CheckConditionUtils.checkCondition(stock == null, "暂无股票信息");
		stock = stockCacheService.getStockFiveByKey(stock.getMarketId(),stock.getStockCode());
		
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("stock", stock);
		return resultBean;
	}
	
	@GetMapping("/infos/query")
	@ResponseBody
	public ResultBean queryStockInfoList(String stockCode) {
		return null;
	}
}

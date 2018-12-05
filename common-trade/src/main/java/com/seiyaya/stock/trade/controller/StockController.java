package com.seiyaya.stock.trade.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class StockController {
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@GetMapping("/query/info")
	public ResultBean queryStockInfo(String stockCode) {
		Stock stock = stockCacheService.getStockByCode(stockCode);
		CheckConditionUtils.checkCondition(stock == null, "暂无股票信息");
		stock = stockCacheService.getStockFiveByKey(stock.getMarketId(),stock.getStockCode());
		
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("stock", stock).setMsg("查询五档信息成功");
		return resultBean;
	}
	
	@GetMapping("/query/infos")
	public ResultBean queryStockInfoList(String stockCode,@RequestParam(defaultValue = "8")int pageSize) {
		List<Stock> stockList = stockCacheService.getStockKeyList();
		List<Stock> resultList = new ArrayList<>(pageSize);
		int count = 0;
		if(!CollectionUtils.isEmpty(stockList)) {
			for(Stock stock : stockList) {
				if(stock.getStockCode().contains(stockCode)) {
					count++;
					resultList.add(stock);
					if(count == 8) {
						break;
					}
				}
			}
		}
		
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("stocks", resultList).setMsg("股票信息联想成功");
		return resultBean;
	}
}

package com.seiyaya.stock.trade.controller;

import static com.seiyaya.common.utils.CheckConditionUtils.checkCondition;
import static com.seiyaya.common.utils.NumberFormatUtils.formatNumber;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.DBPage;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.ResultBean;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.stock.service.StockCacheService;
import com.seiyaya.stock.trade.service.TradeService;

import lombok.extern.slf4j.Slf4j;

/**
 * 持仓相关
 * @author Seiyaya
 *
 */
@RequestMapping("/holdstock")
@RestController
@Slf4j
public class HoldStockController {
	
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@GetMapping("/query")
	public ResultBean queryHoldStock(Integer accountId,@RequestParam(defaultValue = "1")int pageIndex,@RequestParam(defaultValue = "8")int pageSize) {
		checkCondition(accountId == null, "参数非法");
		DBPage<HoldStock> page = tradeService.queryHoldStock(accountId,pageIndex,pageSize);
		Account account = tradeService.queryAccount(accountId);
		List<HoldStock> list = page.getList();
		if(CollectionUtils.isEmpty(list)) {
			list.forEach((holdStock) -> {
				Stock stock = stockCacheService.getStockByKey(holdStock.getMarketId()+holdStock.getStockCode());
				if(stock!=null) {
					holdStock.setStockName(stock.getStockName());
					holdStock.setMarketBalance(stock.getNowPrice()*holdStock.getTotalQty());
					holdStock.setYesterday(stock.getYesterday());
					//浮动盈亏
					holdStock.setProfit(formatNumber((stock.getNowPrice() - holdStock.getHoldPrice()) * holdStock.getTotalQty()));
					holdStock.setProfitRate(formatNumber((stock.getNowPrice() - holdStock.getHoldPrice())/ holdStock.getHoldPrice()*100));
					holdStock.setDayPercentage(stock.getDayPercentage());
					holdStock.setNowPrice(stock.getNowPrice());
					//个股仓位
					holdStock.setPosition(formatNumber(holdStock.getMarketBalance()/account.getTotalAssets()*100));
				}else {
					log.warn("{}->{}没有行情",holdStock.getMarketId(),holdStock.getStockCode());
				}
			});
		}
		
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("holdStocks", page);
		return resultBean;
	}
	
	@GetMapping("/maxSell")
	public ResultBean queryMaxSell(Integer accountId,String stockCode,String marketId) {
		ResultBean resultBean = new ResultBean();
		if(StringUtils.isEmpty(marketId) || StringUtils.isEmpty(stockCode) || accountId == null) {
			DBParam result = new DBParam().set("current_qty", 0);
			resultBean.setStatus(2);
			resultBean.setResults("result", result);
			return resultBean;
		}
		
		HoldStock holdStock = tradeService.queryHoldStock(accountId, marketId, stockCode);
		resultBean.setResults("result", holdStock);
		resultBean.setMsg("查询最大可卖成功");
		return resultBean;
	}
	
	@GetMapping("/maxBuy")
	public ResultBean queryMaxBuy(Integer accountId,String stockCode,String marketId) {
		ResultBean resultBean = new ResultBean();
		DBParam errorResult = new DBParam().set("maxbuy_qty", 0);
		if(StringUtils.isEmpty(marketId) || StringUtils.isEmpty(stockCode) || accountId == null) {
			resultBean.setResults("result", errorResult).setStatus(2).setMsg("参数非法");
			return resultBean;
		}
		Stock stock = stockCacheService.getStockByKey(marketId+stockCode);
		if(stock == null || stock.getNowPrice() == 0.0D) {
			resultBean.setResults("result", errorResult).setStatus(2).setMsg("行情信息不存在");
			return resultBean;
		}
		
		Account account = tradeService.queryAccount(accountId);
		int maxBuy = (int) (account.getCurrentBalance()/stock.getNowPrice()/100);
		DBParam result = new DBParam().set("maxbuy_qty", maxBuy);
		resultBean.setMsg("查询最大可卖成功").setResults("result", result);
		return resultBean;
	}
}

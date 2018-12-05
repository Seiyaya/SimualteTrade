package com.seiyaya.stock.trade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.DBPage;
import com.seiyaya.common.bean.ResultBean;
import com.seiyaya.common.utils.CheckConditionUtils;
import com.seiyaya.stock.service.StockCacheService;
import com.seiyaya.stock.trade.service.TradeService;

/**
 * 成交
 * @author Seiyaya
 *
 */
@RequestMapping("/bargain")
@RestController
public class BargainController {
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@GetMapping("/today/query")
	public ResultBean queryTodayBargain(Integer accountId,@RequestParam(defaultValue = "1")int pageIndex,@RequestParam(defaultValue = "8")int pageSize) {
		DBPage<Bargain> list = tradeService.queryTodayBargain(accountId,pageIndex,pageSize);
		
		list.getList().forEach((bargain) -> {
			String tradeTypeName = stockCacheService.getEnumValue("trade_type",bargain.getTradeType());
			bargain.setTradeTypeName(tradeTypeName);
		});
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("bargains", list);
		return resultBean;
	}
	
	@GetMapping("/hist/query")
	public ResultBean queryHistBargain(Integer accountId,@RequestParam(defaultValue = "1")int pageIndex,@RequestParam(defaultValue = "8")int pageSize) {
		CheckConditionUtils.checkCondition(accountId == null, "参数非法");
		DBPage<Bargain> list = tradeService.queryHistBargain(accountId,pageIndex,pageSize);
		
		list.getList().forEach((bargain) -> {
			String tradeTypeName = stockCacheService.getEnumValue("trade_type",bargain.getTradeType());
			bargain.setTradeTypeName(tradeTypeName);
		});
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("bargains", list);
		return resultBean;
	}
}

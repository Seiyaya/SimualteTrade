package com.seiyaya.stock.trade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seiyaya.common.bean.DBPage;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.ResultBean;
import com.seiyaya.common.utils.CheckConditionUtils;
import com.seiyaya.stock.trade.service.TradeService;

/**
 * 持仓相关
 * @author Seiyaya
 *
 */
@RequestMapping("/holdstock")
@Controller
public class HoldStockController {
	
	
	@Autowired
	private TradeService tradeService;
	
	@GetMapping("/query")
	@ResponseBody
	public ResultBean queryHoldStock(Integer accountId,@RequestParam(defaultValue = "1")int pageIndex,@RequestParam(defaultValue = "8")int pageSize) {
		CheckConditionUtils.checkCondition(accountId == null, "参数非法");
		DBPage<HoldStock> list = tradeService.queryHoldStock(accountId,pageIndex,pageSize);
		
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("holdStocks", list);
		return resultBean;
	}
}

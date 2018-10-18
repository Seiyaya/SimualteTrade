package com.seiyaya.stock.trade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seiyaya.common.bean.ResultBean;

/**
 * 行情相关
 * @author Seiyaya
 *
 */
@RequestMapping("/stock")
@Controller
public class StockController {
	
	@GetMapping("/info/query")
	@ResponseBody
	public ResultBean queryStockInfo() {
		return null;
	}
	
	@GetMapping("/infos/query")
	@ResponseBody
	public ResultBean queryStockInfoList() {
		return null;
	}
}

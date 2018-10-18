package com.seiyaya.stock.trade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seiyaya.common.bean.ResultBean;

/**
 * 持仓相关
 * @author Seiyaya
 *
 */
@RequestMapping("/holdstock")
@Controller
public class HoldStockController {
	
	@GetMapping("/stock/query")
	@ResponseBody
	public ResultBean queryHoldStock() {
		return null;
	}
}

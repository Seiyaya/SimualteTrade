package com.seiyaya.stock.trade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seiyaya.common.bean.ResultBean;

/**
 * 成交
 * @author Seiyaya
 *
 */
@RequestMapping("/bargain")
@Controller
public class BargainController {
	
	@GetMapping("/today/query")
	public ResultBean queryTodayBargain() {
		return null;
	}
	
	@GetMapping("/hist/query")
	public ResultBean queryHistBargain() {
		return null;
	}
}

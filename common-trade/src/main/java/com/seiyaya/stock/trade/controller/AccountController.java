package com.seiyaya.stock.trade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seiyaya.common.bean.ResultBean;

@Controller
@RequestMapping("/account")
public class AccountController {

	@PostMapping("/add")
	public ResultBean addAccount(Integer userId) {
		return new ResultBean("创建账户成功");
	}
}

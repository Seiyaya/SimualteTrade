package com.seiyaya.stock.trade.service.impl;

import org.springframework.stereotype.Service;

import com.seiyaya.common.bean.Order;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.stock.trade.service.RuleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RuleServiceImpl implements RuleService{

	@Override
	public void compareTradeRule(Order order, Stock stock) {
		log.debug("进入买入规则校验---start");
		
		log.debug("结束买入规则校验---end");
	}

}

package com.seiyaya.stock.trade.service;

import com.seiyaya.common.bean.Order;
import com.seiyaya.common.bean.Stock;

public interface RuleService {

	void compareTradeRule(Order order, Stock stock);
}

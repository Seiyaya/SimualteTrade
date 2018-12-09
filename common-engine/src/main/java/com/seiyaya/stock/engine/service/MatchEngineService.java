package com.seiyaya.stock.engine.service;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.Order;

public interface MatchEngineService {

	public static final String BEAN_NAME = "engine.service.matchEngineService";
	
	/**
	 * 查询需要匹配的委托
	 * @return
	 */
	List<Order> queryNeedMatchOrder();

	/**
	 * 查询需要撤单的委托
	 * @return
	 */
	List<Order> queryNeedCancelOrder();

	/**
	 * 处理撤单委托
	 * @param order
	 */
	void dealCancelOrder(Order order);

	/**
	 * 处理买入委托
	 * @param order
	 */
	void dealBuyOrder(Order order);

	/**
	 * 处理卖出委托
	 * @param order
	 */
	void dealSellOrder(Order order);

	/**
	 * 处理成交
	 * @param value
	 */
	void dealBargains(ConcurrentLinkedQueue<Bargain> value);

	void dealBuyBargain(Bargain bargain);

	void dealSellBargain(Bargain bargain);

	void dealBuyCancelBargain(Bargain bargain);

	void dealSellCancelBargain(Bargain bargain);

}

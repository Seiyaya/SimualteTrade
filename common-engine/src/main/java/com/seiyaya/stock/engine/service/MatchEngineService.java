package com.seiyaya.stock.engine.service;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.CompleteProfit;
import com.seiyaya.common.bean.Order;
import com.seiyaya.common.bean.PositionChange;

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
	Bargain dealCancelOrder(Order order);

	/**
	 * 处理买入委托
	 * @param order
	 */
	Bargain dealBuyOrder(Order order);

	/**
	 * 处理卖出委托
	 * @param order
	 */
	Bargain dealSellOrder(Order order);

	/**
	 * 处理成交
	 * @param value
	 */
	void dealBargains(ConcurrentLinkedQueue<Bargain> value);

	void dealBuyBargain(Bargain bargain);

	void dealSellBargain(Bargain bargain);

	void dealBuyCancelBargain(Bargain bargain);

	void dealSellCancelBargain(Bargain bargain);

	/**
	 * 批量写入完整收益
	 * @param completeProfitList
	 */
	void addCompleteProfitList(List<CompleteProfit> completeProfitList);

	/**
	 * 批量写入仓位变动
	 * @param positionChangeList
	 */
	void addPositionChangeList(List<PositionChange> positionChangeList);

}

package com.seiyaya.stock.trade.service;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.DBPage;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.Order;

/**
 * 订单服务
 * @author Seiyaya
 *
 */
public interface TradeService {

	/**
	 * 查找持仓
	 * @param accountId
	 * @param marketId
	 * @param stockCode
	 * @return
	 */
	HoldStock queryHoldStock(int accountId, String marketId, String stockCode);
	
	/**
	 * 查询账户
	 * @param accountId
	 * @return
	 */
	public Account queryAccount(int accountId);

	/**
	 * 添加买入委托
	 * @param order
	 * @param account
	 * @param newVersion
	 * @return
	 */
	long addBuyOrder(Order order, Account account, String newVersion);

	/**
	 * 处理卖出委托
	 * @param order
	 * @param newVersion
	 * @return
	 */
	long addSellOrder(Order order, HoldStock holdStock, String newVersion);

	DBPage<Order> queryTodayOrder(Integer accountId, int pageIndex, int pageSize);

	DBPage<Order> queryHistOrder(Integer accountId, int pageIndex, int pageSize);

	DBPage<Order> queryCancelOrder(Integer accountId, int pageIndex, int pageSize);

	Order queryOrder(Integer orderId);
}

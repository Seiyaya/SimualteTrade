package com.seiyaya.stock.trade.service;

import java.util.List;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.Bargain;
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

	/**
	 * 查询今日委托
	 * @param accountId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	DBPage<Order> queryTodayOrder(Integer accountId, int pageIndex, int pageSize);

	/**
	 * 查询历史委托
	 * @param accountId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	DBPage<Order> queryHistOrder(Integer accountId, int pageIndex, int pageSize);

	/**
	 * 查询可撤委托
	 * @param accountId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	DBPage<Order> queryCancelOrder(Integer accountId, int pageIndex, int pageSize);

	/**
	 * 查询委托
	 * @param orderId
	 * @return
	 */
	Order queryOrder(Integer orderId);

	/**
	 * 查询今日成交
	 * @param accountId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	DBPage<Bargain> queryTodayBargain(Integer accountId, int pageIndex, int pageSize);

	/**
	 * 查询历史成交
	 * @param accountId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	DBPage<Bargain> queryHistBargain(Integer accountId, int pageIndex, int pageSize);

	/**
	 * 查询账户持仓
	 * @param accountId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	DBPage<HoldStock> queryHoldStock(Integer accountId, int pageIndex, int pageSize);
	
	/**
	 * 查询账户持仓
	 * @param accountId
	 * @return
	 */
	List<HoldStock> queryHoldStock(Integer accountId);
	/**
	 * 创建账户
	 * @param userId
	 * @param accountName
	 * @return
	 */
	Integer addAccount(Integer userId, String accountName);
}

package com.seiyaya.stock.trade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.DBPage;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.Order;
import com.seiyaya.stock.trade.mapper.AccountMapper;
import com.seiyaya.stock.trade.mapper.HoldStockMapper;
import com.seiyaya.stock.trade.mapper.OrderMapper;
import com.seiyaya.stock.trade.service.TradeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private HoldStockMapper holdStockMapper;

	@Override
	public long addSellOrder(Order order, HoldStock holdStock,String newVersion) {
		String version = holdStock.getVersion();
		DBParam param = new DBParam().set("qty", order.getOrderQty()).set("new_version", newVersion)
				.set("account_id", order.getAccountId()).set("version", version).set("market_id", order.getMarketId())
				.set("stock_code", order.getStockCode());
		int flag = holdStockMapper.updateCurrentQty(param);
		if (flag == 1) {
			return orderMapper.addOrder(order);
		} else {
			log.error("账户id为:{} 乐观锁失败! 委托信息:{}", order.getAccountId(), order);
			return -1L;
		}
	}

	@Override
	public HoldStock queryHoldStock(int accountId, String marketId, String stockCode) {
		DBParam param = new DBParam()
				.set("account_id", accountId)
				.set("market_id", marketId)
				.set("stock_code", stockCode);
		return holdStockMapper.queryHoldStock(param);
	}

	@Override
	public long addBuyOrder(Order order,Account account,String newVersion) {
		String version = account.getVersion();
		DBParam param = new DBParam().set("total_balance", -order.getTotalBalance()).set("new_version", newVersion)
				.set("account_id", order.getAccountId()).set("version", version);
		int flag = accountMapper.updateCurrentBalance(param);
		if (flag == 1) {
			return orderMapper.addOrder(order);
		} else {
			log.error("账户id为:{} 乐观锁失败! 委托信息:{}", order.getAccountId(), order);
			return -1L;
		}
	}

	@Override
	public Account queryAccount(int accountId) {
		Account account = accountMapper.queryAccount(new DBParam().set("account_id", accountId));
		log.info("查询到的账户信息:",account);
		return account;
	}

	@Override
	public DBPage<Order> queryTodayOrder(Integer accountId, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex, pageSize);
		DBParam param = new DBParam()
				.set("account_id", accountId);
		List<Order> orderList = orderMapper.queryTodayOrderList(param);
		DBPage<Order> result = new DBPage<>(orderList);
		return result;
	}

	@Override
	public DBPage<Order> queryHistOrder(Integer accountId, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex, pageSize);
		DBParam param = new DBParam()
				.set("account_id", accountId);
		List<Order> orderList = orderMapper.queryHistOrderList(param);
		DBPage<Order> result = new DBPage<>(orderList);
		return result;
	}

	@Override
	public DBPage<Order> queryCancelOrder(Integer accountId, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex, pageSize);
		DBParam param = new DBParam()
				.set("account_id", accountId);
		List<Order> orderList = orderMapper.queryOrderList(param);
		DBPage<Order> result = new DBPage<>(orderList);
		return result;
	}

	@Override
	public Order queryOrder(Integer orderId) {
		DBParam param = new DBParam()
				.set("order_id", orderId);
		return orderMapper.queryOrder(param);
	}

}

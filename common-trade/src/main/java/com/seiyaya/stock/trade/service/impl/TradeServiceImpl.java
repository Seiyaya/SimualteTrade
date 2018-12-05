package com.seiyaya.stock.trade.service.impl;

import static com.seiyaya.common.utils.CheckConditionUtils.checkCondition;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.DBPage;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.Order;
import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.stock.service.StockCacheService;
import com.seiyaya.stock.trade.mapper.AccountMapper;
import com.seiyaya.stock.trade.mapper.BargainMapper;
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
	
	@Autowired
	private BargainMapper bargainMapper;
	
	@Autowired
	private StockCacheService stockCacheService;

	@Override
	public int addSellOrder(Order order, HoldStock holdStock,String newVersion) {
		String version = holdStock.getVersion();
		DBParam param = new DBParam().set("qty", order.getOrderQty()).set("new_version", newVersion)
				.set("account_id", order.getAccountId()).set("version", version).set("market_id", order.getMarketId())
				.set("stock_code", order.getStockCode());
		int flag = holdStockMapper.updateCurrentQty(param);
		if (flag == 1) {
			return orderMapper.addOrder(order);
		} else {
			log.error("账户id为:{} 乐观锁失败! 委托信息:{}", order.getAccountId(), order);
			return -1;
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
	public int addBuyOrder(Order order,Account account,String newVersion) {
		String version = account.getVersion();
		DBParam param = new DBParam().set("total_balance", -order.getTotalBalance()).set("new_version", newVersion)
				.set("account_id", order.getAccountId()).set("version", version);
		int flag = accountMapper.updateCurrentBalance(param);
		if (flag == 1) {
			orderMapper.addOrder(order);
			return order.getOrderId();
		} else {
			log.error("账户id为:{} 乐观锁失败! 委托信息:{}", order.getAccountId(), order);
			return -1;
		}
	}

	@Override
	public Account queryAccount(int accountId) {
		Account account = accountMapper.queryAccount(new DBParam().set("account_id", accountId));
		log.info("查询到的账户信息:",account);
		checkCondition(account == null, "账户信息不存在");
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
				.set("account_id", accountId)
				.set("cancel_status", "3");
		List<Order> orderList = orderMapper.queryTodayOrderList(param);
		DBPage<Order> result = new DBPage<>(orderList);
		return result;
	}

	@Override
	public Order queryOrder(Integer accountId,Integer orderId) {
		DBParam param = new DBParam()
				.set("order_id", orderId)
				.set("account_id", accountId);
		return orderMapper.queryOrder(param);
	}

	@Override
	public DBPage<Bargain> queryTodayBargain(Integer accountId, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex, pageSize);
		DBParam param = new DBParam()
				.set("account_id", accountId);
		List<Bargain> bargainList = bargainMapper.queryTodayBargainList(param);
		DBPage<Bargain> result = new DBPage<>(bargainList);
		return result;
	}

	@Override
	public DBPage<Bargain> queryHistBargain(Integer accountId, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex, pageSize);
		DBParam param = new DBParam()
				.set("account_id", accountId);
		List<Bargain> bargainList = bargainMapper.queryHistBargainList(param);
		DBPage<Bargain> result = new DBPage<>(bargainList);
		return result;
	}

	@Override
	public DBPage<HoldStock> queryHoldStock(Integer accountId, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex, pageSize);
		DBParam param = new DBParam()
				.set("account_id", accountId);
		List<HoldStock> holdStockList = holdStockMapper.queryHoldStockList(param);
		DBPage<HoldStock> result = new DBPage<>(holdStockList);
		return result;
	}

	@Override
	public List<HoldStock> queryHoldStock(Integer accountId) {
		DBParam param = new DBParam()
				.set("account_id", accountId);
		List<HoldStock> holdStockList = holdStockMapper.queryHoldStockList(param);
		return holdStockList;
	}

	@Override
	public Integer addAccount(Integer userId, String accountName) {
		double initBalance = Double.parseDouble(stockCacheService.getSysConfig(SystemConfig.DEFAULT_INIT_BALANCE));
		String date = DateUtils.formatNowDate(DateUtils.PATTERN_TIME);
		
		//添加普通账户
		DBParam param = new DBParam()
				.set("user_id", userId)
				.set("account_name", accountName)
				.set("init_balance", initBalance)
				.set("total_assets", initBalance)
				.set("current_balance", initBalance)
				.set("create_date", date)
				.set("update_date", date);
		accountMapper.addAccount(param);
		log.info("{}",param);
		Integer accountId = param.getInt("account_id");
		//添加资产中间表
		param = new DBParam();
		param.set("account_id", accountId).set("init_balance", initBalance)
			.set("create_day", 1);
		accountMapper.addAccountAssets(param);
		
		//添加账户指标
		param = new DBParam().set("account_id", accountId);
		accountMapper.addAccountIndicator(param);
		
		//添加收益指标
		param = new DBParam().set("account_id", accountId);
		accountMapper.addAccountEarn(param);
		return accountId;
	}

	@Override
	public void cancelOrder(Integer accountId, Integer orderId) {
		DBParam cancelOrder = new DBParam()
				.set("order_id", orderId)
				.set("account_id", accountId)
				.set("create_date", DateUtils.formatNowTime())
				.set("cancel_status", "0")
				.set("cancel_date", DateUtils.formatNowDate());
		
		DBParam updateOrder = new DBParam()
				.set("order_id", orderId);
		orderMapper.addCancelOrder(cancelOrder);
		orderMapper.updateOrder(updateOrder);
	}

	@Override
	public void outTimeCancelOrder(Integer accountId, Order order) {
		if(order.isBuy()) {
			//买入归还资金
			DBParam param = new DBParam()
					.set("total_balance", order.getTotalBalance())
					.set("account_id", accountId);
			accountMapper.updateAccount(param);
		}else if(order.isSell()) {
			//卖出归还持仓
			DBParam param = new DBParam()
					.set("total_qty", order.getOrderQty())
					.set("stock_code", order.getStockCode())
					.set("market_id", order.getMarketId())
					.set("account_id", accountId);
			holdStockMapper.updateHoldStock(param);
		}
		DBParam orderParam = new DBParam()
				.set("order_id", order.getOrderId())
				.set("total_qty",order.getOrderQty());
		orderMapper.updateOrderDone(orderParam);
		
		DBParam cancelParam = new DBParam()
				.set("order_id", order.getOrderId())
				.set("account_id", order.getAccountId())
				.set("create_date", DateUtils.formatNowTime())
				.set("cancel_date", DateUtils.formatNowDate())
				.set("cancel_status", "1");
		orderMapper.addCancelOrder(cancelParam);
	}

}

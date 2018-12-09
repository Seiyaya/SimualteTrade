package com.seiyaya.stock.engine.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.CompleteProfit;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.EnumValue;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.Order;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.stock.engine.mapper.BargainMapper;
import com.seiyaya.stock.engine.mapper.OrderMapper;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.service.MatchEngineService;
import com.seiyaya.stock.engine.util.MatchEngine;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * 撮合
 * 
 * @author Seiyaya
 *
 */
@Service(value = MatchEngineService.BEAN_NAME)
@Slf4j
public class MatchEngineServiceImpl implements MatchEngineService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private BargainMapper bargainMapper;
	
	@Autowired
	private MatchEngineCacheService matchEngineCacheService;
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@Autowired
	private MatchEngine matchEngine;
	
	@Override
	public List<Order> queryNeedMatchOrder() {
		DBParam param = new DBParam().set("trade_status", EnumValue.TRADE_STATUS_0+","+EnumValue.TRADE_STATUS_3);
		List<Order> orderList = orderMapper.queryOrder(param);
		if(!CollectionUtils.isEmpty(orderList)) {
			List<DBParam> params = new ArrayList<>(orderList.size());
			orderList.forEach((order) ->{
				DBParam orderParam = new DBParam().set("deal_flag", EnumValue.DEAL_FLAG_1)
						.set("order_id", order.getOrderId());
				if(EnumValue.TRADE_STATUS_0.equals(order.getTradeStatus())) {
					order.setTradeStatus(EnumValue.TRADE_STATUS_1);
					orderParam.set("trade_status", EnumValue.TRADE_STATUS_1);
				}else {
					orderParam.set("trade_status", EnumValue.TRADE_STATUS_3);
				}
				params.add(orderParam);
			});
			orderMapper.updateOrderList(params);
			params.clear();
		}
		return orderList;
	}

	@Override
	public List<Order> queryNeedCancelOrder() {
		DBParam param = new DBParam().set("cancel_status", EnumValue.CANCEL_STATUS_0)
				.set("cancel_date", DateUtils.formatNowDate());
		List<Order> orderList = orderMapper.queryCancelOrder(param);
		if(!CollectionUtils.isEmpty(orderList)) {
			List<DBParam> params = new ArrayList<>(orderList.size());
			orderList.forEach( (order) -> {
				DBParam orderParam = new DBParam().set("cancel_status",EnumValue.CANCEL_STATUS_2)
						.set("order_id", order.getOrderId());
				params.add(orderParam);
			});
			orderMapper.updateCancelOrderList(params);
			params.clear();
		}
		return orderList;
	}

	@Override
	public void dealCancelOrder(Order order) {
		Bargain bargain = new Bargain();
		bargain.setAccountId(order.getAccountId());
		bargain.setOrderId(order.getOrderId());
		matchEngineCacheService.addBargainToCache(bargain);
		
		if(log.isInfoEnabled()) {
			log.info("委托{},撤单成功，加入写队列",order.getOrderId());
		}
	}

	@Override
	public void dealBuyOrder(Order order) {
		boolean isMatch = false;

		Stock stock = stockCacheService.getStockByKey(order.getMarketId() + order.getStockCode());
		try {
			if (stock != null) {
				order.setStockType(stock.getStockType());
				if (isBuyMatch(order, stock)) {
					double nowPrice = stock.getNowPrice();
					Bargain bargain = getMatchBargainData(order,nowPrice);
					matchEngineCacheService.addBargainToCache(bargain);
					log.info("委托编号");
				}
				isMatch = true;
			} else {
				log.error("处理:{}，行情信息不存在:{}:{}", order.getOrderId(), order.getMarketId(), order.getStockCode());
			}
		} catch (Exception e) {
			log.error("行情信息:{},\n委托信息:{}处理卖出异常:", stock, order, e);
		} finally {
			if (!isMatch) {
				matchEngineCacheService.addOrderToCache(order);
			}
		}

		if (log.isInfoEnabled()) {
			log.info("委托{},买入成功，加入写队列", order.getOrderId());
		}
	}

	private Bargain getMatchBargainData(Order order, double nowPrice) {
		return null;
	}

	/**
	 * 买入匹配
	 * @param order
	 * @param stock
	 * @return
	 */
	private boolean isBuyMatch(Order order, Stock stock) {
		return stock.getNowPrice() != 0D && order.getOrderPrice() >= stock.getNowPrice() && isHaveSell1Qty(stock);
	}

	private boolean isHaveSell1Qty(Stock stock) {
		//TODO:调用五档行情
		return false;
	}

	@Override
	public void dealSellOrder(Order order) {
		boolean isMatch = false;
		
		Stock stock = stockCacheService.getStockByKey(order.getMarketId()+order.getStockCode());
		try {
			if (stock != null) {
				order.setStockType(stock.getStockType());
				if (isSellMatch(order, stock)) {
					isMatch = true;
				}
			} else {
				log.error("处理:{}，行情信息不存在:{}:{}", order.getOrderId(), order.getMarketId(), order.getStockCode());
			}
		} catch (Exception e) {
			log.error("行情信息:{},\n委托信息:{}处理卖出异常:", stock, order, e);
		} finally {
			if (!isMatch) {
				matchEngineCacheService.addOrderToCache(order);
			}
		}
		
		if(log.isInfoEnabled()) {
			log.info("委托{},卖出成功，加入写队列",order.getOrderId());
		}
	}

	/**
	 * 卖出匹配
	 * @param order
	 * @param stock
	 * @return
	 */
	private boolean isSellMatch(Order order, Stock stock) {
		return stock.getNowPrice() != 0D && order.getOrderPrice() <= stock.getNowPrice() && isHaveBuy1Qty(stock);
	}

	private boolean isHaveBuy1Qty(Stock stock) {
		//TODO:调用五档行情
		return false;
	}

	@Override
	public void dealBargains(ConcurrentLinkedQueue<Bargain> value) {
		matchEngine.addBargainList(value);
	}

	@Override
	public void dealBuyBargain(Bargain bargain) {
		//加仓还是建仓
		HoldStock holdStock = queryHoldStockInfo(bargain.getAccountId(),bargain.getMarketId(),bargain.getStockCode());
		if(holdStock == null) {
			//建仓
			holdStock = new HoldStock();
			holdStock.setMarketId(bargain.getMarketId());
			holdStock.setStockCode(bargain.getStockCode());
			holdStock.setCreateDate(DateUtils.formatNowTime());
			updateHoldStock(bargain,holdStock);
			bargainMapper.addHoldStock(holdStock);
		}else {
			//加仓
			updateHoldStock(bargain,holdStock);
		}
		bargain.setHoldId(holdStock.getHoldId());
		bargainMapper.addBargain(bargain);
		updateOrderStatus(bargain);
		updateAccount(bargain);//TODO:多台撮合可能有问题，如果要加入消息队列，此处应该使用版本号更新
		
		matchEngineCacheService.addPositionChange(bargain);
	}

	/**
	 * 查询持仓
	 * @param accountId
	 * @param marketId
	 * @param stockCode
	 * @return
	 */
	private HoldStock queryHoldStockInfo(Integer accountId,String marketId,String stockCode) {
		DBParam holdStockParam = new DBParam()
				.set("account_id", accountId)
				.set("stock_code", marketId)
				.set("market_id", stockCode);
		HoldStock holdStock = bargainMapper.queryHoldStockInfo(holdStockParam);
		return holdStock;
	}

	/**
	 * 买入或者卖出更新持仓
	 * @param bargain
	 * @param holdStock
	 */
	private void updateHoldStock(Bargain bargain, HoldStock holdStock) {
		int changeQty = bargain.getExecQty();
		int holdQty = holdStock.getTotalQty();
		double costPrice = holdStock.getCostPrice();
		double holdPrice = holdStock.getHoldPrice();
		if(bargain.isBuy()) {
			holdQty = holdQty + changeQty;
			holdStock.setTotalQty(holdQty);
			costPrice = (costPrice*holdQty + bargain.getOrderBalance() ) / holdQty;
			holdPrice = (holdPrice*holdQty + bargain.getOrderBalance() ) / holdQty;
		}else if(bargain.isSell()){
			holdQty = holdQty - changeQty;
			holdStock.setTotalQty(holdQty);
			if(holdQty == 0) {
				//清仓
			}else {
				//减仓
				costPrice = (costPrice * holdQty - bargain.getTotalBalance())/holdQty;
			}
		}
		holdStock.setUpdateDate(DateUtils.formatNowTime());
	}

	/**
	 * 买入或者卖出修改账户信息
	 * @param bargain
	 */
	private void updateAccount(Bargain bargain) {
		DBParam param = new DBParam().set("account_id", bargain.getAccountId());
		if(bargain.isBuy()) {
			//归还到可用的金额
			param.set("change_balance", bargain.getOrderBalance()-bargain.getTotalBalance());
			//解冻的金额
			param.set("freezed_balance", bargain.getOrderBalance());
		}else if(bargain.isSell()) {
			param.set("change_balance", bargain.getTotalBalance());
			param.set("freezed_balance", 0);
		}
		bargainMapper.updateAccountBalance(param);
	}

	/**
	 * 更新委托状态为已完成
	 */
	private void updateOrderStatus(Bargain bargain) {
		DBParam orderParam = new DBParam()
				.set("trade_status", bargain.getTradeStatus())
				.set("deal_flag", EnumValue.DEAL_FLAG_2)
				.set("order_id", bargain.getOrderId());
		orderMapper.updateOrder(orderParam);
	}

	@Override
	public void dealSellBargain(Bargain bargain) {
		//减仓还是清仓
		HoldStock holdStock = queryHoldStockInfo(bargain.getAccountId(), bargain.getMarketId(), bargain.getStockCode());
		if(holdStock == null) {
			log.error("{}:{} 账户:{}没有持仓",bargain.getMarketId(),bargain.getStockCode(),bargain.getAccountId());
			return ;
		}
		if(holdStock.getTotalQty() == bargain.getExecQty()) {
			//清仓,直接删除持仓
			bargainMapper.delHoldStock(holdStock.getHoldId());
			//计算完整收益
			CompleteProfit profit = packageCompleteProfit(bargain);
			matchEngineCacheService.addCompleteProfit(profit);
		}else {
			//减仓
			updateHoldStock(bargain, holdStock);
		}
		bargainMapper.addBargain(bargain);
		updateAccount(bargain);
		//仓位变动添加计算
		matchEngineCacheService.addPositionChange(bargain);
	}

	private CompleteProfit packageCompleteProfit(Bargain bargain) {
		CompleteProfit profit = new CompleteProfit();
		profit.setMarketId(bargain.getMarketId());
		profit.setStockCode(bargain.getStockCode());
		profit.setStockName(bargain.getStockName());
		profit.setAccountId(bargain.getAccountId());
		profit.setHoldId(bargain.getHoldId());
		return profit;
	}

	@Override
	public void dealBuyCancelBargain(Bargain bargain) {
		//买入撤单  归还资金
		DBParam accountParam = new DBParam()
				.set("freezed_balance", bargain.getOrderBalance())
				.set("account_id", bargain.getAccountId());
		dealCancel(bargain);
		bargainMapper.updateAccountBalance(accountParam);
	}


	@Override
	public void dealSellCancelBargain(Bargain bargain) {
		//卖出撤单  归还持仓
		DBParam holdParam = new DBParam()
				.set("cancel_qty", bargain.getExecQty())
				.set("hold_id", bargain.getHoldId());
		dealCancel(bargain);
		orderMapper.updateCancelOrder(holdParam);
	}

	/**
	 * 更新委托状态和撤单状态
	 * @param bargain
	 */
	private void dealCancel(Bargain bargain) {
		DBParam orderParam = new DBParam()
				.set("trade_status", EnumValue.TRADE_STATUS_4)
				.set("order_id", bargain.getOrderId());
		
		DBParam cancelParam = new DBParam()
				.set("cancel_status", EnumValue.CANCEL_STATUS_1)
				.set("order_id", bargain.getOrderId());
		orderMapper.updateOrder(orderParam);
		orderMapper.updateCancelOrder(cancelParam);
	}
}

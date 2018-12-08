package com.seiyaya.stock.engine.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.EnumValue;
import com.seiyaya.common.bean.Order;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.utils.DateUtils;
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
		Bargain bargain = new Bargain();
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

}

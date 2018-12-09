package com.seiyaya.stock.engine.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.CompleteProfit;
import com.seiyaya.common.bean.Order;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.util.MatchEngineCache;

@Service(value = MatchEngineCacheService.BEAN_NAME)
public class MatchEngineCacheServiceImpl implements MatchEngineCacheService{

	@Override
	public void addOrderToCache(List<Order> orderList) {
		MatchEngineCache.addOrder(orderList);
	}
	
	@Override
	public void addOrderToCache(Order order) {
		MatchEngineCache.addOrder(order);
	}

	@Override
	public void addCanelOrderToCache(List<Order> orderList) {
		orderList.forEach((order) ->{
			MatchEngineCache.addCancelOrder(order.getOrderId());
		});
		orderList.clear();
	}

	@Override
	public ConcurrentLinkedQueue<Order> getMatchOrderQueue() {
		return MatchEngineCache.getMatchOrderQueue();
	}

	@Override
	public boolean isCancel(Integer orderId) {
		return MatchEngineCache.isCannelOrder(orderId);
	}

	@Override
	public void addBargainToCache(Bargain bargain) {
		MatchEngineCache.addBargain(bargain);
	}

	@Override
	public ConcurrentLinkedQueue<Bargain> getBargainQueue() {
		return MatchEngineCache.getBargainQueue();
	}

	@Override
	public void addCompleteProfit(CompleteProfit profit) {
		
	}

	@Override
	public void addPositionChange(Bargain bargain) {
		
	}
}

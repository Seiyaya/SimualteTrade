package com.seiyaya.stock.engine.service;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.Order;

public interface MatchEngineCacheService {

	public static final String BEAN_NAME = "engine.service.matchEngineCacheService";
	
	void addOrderToCache(List<Order> orderList);
	
	void addOrderToCache(Order order);

	void addCanelOrderToCache(List<Order> orderList);

	ConcurrentLinkedQueue<Order> getMatchOrderQueue();
	
	boolean isCancel(Integer orderId);

	void addBargainToCache(Bargain bargain);

	ConcurrentLinkedQueue<Bargain> getBargainQueue();
}

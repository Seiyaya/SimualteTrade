package com.seiyaya.stock.engine.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.Order;

/**
 * 撮合匹配相关缓存
 * 
 * @author Seiyaya
 *
 */
public class MatchEngineCache {

	/**
	 * 待撮合委托队列
	 */
	private static ConcurrentLinkedQueue<Order> ORDER_QUEUE = new ConcurrentLinkedQueue<>();

	/**
	 * 撤单队列
	 */
	private static Set<Integer> CANCEL_ORDER = new HashSet<>();

	/**
	 * 成交队列
	 */
	private static ConcurrentLinkedQueue<Bargain> BARGAIN_QUEUE = new ConcurrentLinkedQueue<>();

	/**
	 * 是否在撤单队列中
	 * 
	 * @param orderId
	 * @return
	 */
	public static boolean isCannelOrder(Integer orderId) {
		return CANCEL_ORDER.contains(orderId);
	}

	public static void addOrder(List<Order> orderList) {
		ORDER_QUEUE.addAll(orderList);
	}

	public static void addOrder(Order order) {
		ORDER_QUEUE.add(order);
	}

	public static void addCancelOrder(Integer orderId) {
		CANCEL_ORDER.add(orderId);
	}

	public static ConcurrentLinkedQueue<Order> getMatchOrderQueue() {
		return ORDER_QUEUE;
	}

	public static void addBargain(Bargain bargain) {
		BARGAIN_QUEUE.add(bargain);
	}

	public static ConcurrentLinkedQueue<Bargain> getBargainQueue() {
		return BARGAIN_QUEUE;
	}

}

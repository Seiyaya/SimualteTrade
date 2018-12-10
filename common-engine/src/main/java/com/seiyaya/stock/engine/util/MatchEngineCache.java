package com.seiyaya.stock.engine.util;

import java.util.HashSet;
import java.util.Iterator;
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
	 * 完整收益
	 */
	private static ConcurrentLinkedQueue<Bargain> COMPLETE_PROFIT_QUEUE = new ConcurrentLinkedQueue<>();
	
	/**
	 * 仓位变动
	 */
	private static ConcurrentLinkedQueue<Bargain> POSITION_CHANGE_QUEUE = new ConcurrentLinkedQueue<>();

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

	/**
	 * 获取需要撮合的委托单
	 * @return
	 */
	public static ConcurrentLinkedQueue<Order> getMatchOrderQueue() {
		ConcurrentLinkedQueue<Order> orderTmp = new ConcurrentLinkedQueue<>();
		for(Iterator<Order> iterator = ORDER_QUEUE.iterator();iterator.hasNext();) {
			Order order = iterator.next();
			orderTmp.add(order);
			ORDER_QUEUE.remove();
		}
		return orderTmp;
	}
	
	public static ConcurrentLinkedQueue<Bargain> getCompleteProfitQueue(){
		ConcurrentLinkedQueue<Bargain> profitTmp = new ConcurrentLinkedQueue<>();
		for(Iterator<Bargain> iterator = COMPLETE_PROFIT_QUEUE.iterator();iterator.hasNext();) {
			Bargain profit = iterator.next();
			profitTmp.add(profit);
			COMPLETE_PROFIT_QUEUE.remove();
		}
		return profitTmp;
	}
	
	public static ConcurrentLinkedQueue<Bargain> getPositionQueue(){
		ConcurrentLinkedQueue<Bargain> changeTmp = new ConcurrentLinkedQueue<>();
		for(Iterator<Bargain> iterator = POSITION_CHANGE_QUEUE.iterator();iterator.hasNext();) {
			Bargain change = iterator.next();
			changeTmp.add(change);
			POSITION_CHANGE_QUEUE.remove();
		}
		return changeTmp;
	}

	public static void addBargain(Bargain bargain) {
		BARGAIN_QUEUE.add(bargain);
	}

	public static ConcurrentLinkedQueue<Bargain> getBargainQueue() {
		ConcurrentLinkedQueue<Bargain> bargainTmp = new ConcurrentLinkedQueue<>();
		for(Iterator<Bargain> iterator = BARGAIN_QUEUE.iterator();iterator.hasNext();) {
			Bargain bargain = iterator.next();
			bargainTmp.add(bargain);
			BARGAIN_QUEUE.remove();
		}
		return bargainTmp;
	}

	public static void addCompleteProfit(Bargain profit) {
		COMPLETE_PROFIT_QUEUE.add(profit);
	}

	public static void addPositionChange(Bargain change) {
		POSITION_CHANGE_QUEUE.add(change);
	}

}

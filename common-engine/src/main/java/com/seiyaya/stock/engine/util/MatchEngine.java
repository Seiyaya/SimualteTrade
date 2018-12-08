package com.seiyaya.stock.engine.util;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.Order;
import com.seiyaya.stock.engine.worker.BargainWriteWorker;
import com.seiyaya.stock.engine.worker.MatchStockWorker;

/**
 * 撮合
 * @author Seiyaya
 *
 */
@Component
public class MatchEngine {
	
	private static ExecutorService THREADPOOL = new ThreadPoolExecutor(10, 20, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	public void addOrderList(ConcurrentLinkedQueue<Order> orderList) {
		for(Iterator<Order> iterator = orderList.iterator();iterator.hasNext();) {
			Order order = iterator.next();
			THREADPOOL.execute(new MatchStockWorker(order));
		}
	}

	public void addBargainList(ConcurrentLinkedQueue<Bargain> bargainList) {
		for(Iterator<Bargain> iterator = bargainList.iterator();iterator.hasNext();) {
			Bargain bargain = iterator.next();
			THREADPOOL.execute(new BargainWriteWorker(bargain));
		}
	}
}

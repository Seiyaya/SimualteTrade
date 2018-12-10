package com.seiyaya.stock.engine.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.CompleteProfit;
import com.seiyaya.common.bean.Order;
import com.seiyaya.common.bean.PositionChange;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.worker.BargainWriteWorker;
import com.seiyaya.stock.engine.worker.CompleteProfitWriteWorker;
import com.seiyaya.stock.engine.worker.MatchStockWorker;
import com.seiyaya.stock.engine.worker.PositionChangeWriteWorker;

/**
 * 撮合
 * @author Seiyaya
 *
 */
@Component
public class MatchEngine {
	
	@Autowired
	private MatchEngineCacheService matchEngineCacheService;
	
	private static ExecutorService THREADPOOL = new ThreadPoolExecutor(10, 20, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	/**
	 * 成交单撮合
	 * @param orderList
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void addOrderList(ConcurrentLinkedQueue<Order> orderList) throws InterruptedException, ExecutionException {
		for(Iterator<Order> iterator = orderList.iterator();iterator.hasNext();) {
			Order order = iterator.next();
			Future<Bargain> future = THREADPOOL.submit(new MatchStockWorker(order));
			Bargain bargain = future.get();
			matchEngineCacheService.addBargainToCache(bargain);
		}
	}

	/**
	 * 成交单写入，因为成交单必须是事务级操作，所以这里是一单一单写入
	 * @param bargainList
	 */
	public void addBargainList(ConcurrentLinkedQueue<Bargain> bargainList) {
		for(Iterator<Bargain> iterator = bargainList.iterator();iterator.hasNext();) {
			Bargain bargain = iterator.next();
			THREADPOOL.submit(new BargainWriteWorker(bargain));
		}
	}

	/**
	 * 完整多线程计算，批量写入
	 * @param completeProfits
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public List<CompleteProfit> addCompleteProfitList(ConcurrentLinkedQueue<Bargain> completeProfits) throws InterruptedException, ExecutionException {
		List<CompleteProfit> completeProfitList = new ArrayList<>();
		for(Iterator<Bargain> iterator = completeProfits.iterator();iterator.hasNext();) {
			Bargain completeProfit = iterator.next();
			Future<CompleteProfit> future = THREADPOOL.submit(new CompleteProfitWriteWorker(completeProfit));
			CompleteProfit result = future.get();
			completeProfitList.add(result);
		}
		return completeProfitList;
	}

	/**
	 * 仓位多线程计算，批量写入
	 * @param positionChanges
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public List<PositionChange> addPositionChange(ConcurrentLinkedQueue<Bargain> positionChanges) throws InterruptedException, ExecutionException {
		List<PositionChange> positionChangeList = new ArrayList<>();
		for(Iterator<Bargain> iterator = positionChanges.iterator();iterator.hasNext();) {
			Bargain bargain = iterator.next();
			Future<PositionChange> future = THREADPOOL.submit(new PositionChangeWriteWorker(bargain));
			PositionChange positionChange = future.get();
			positionChangeList.add(positionChange);
		}
		return positionChangeList;
	}
}

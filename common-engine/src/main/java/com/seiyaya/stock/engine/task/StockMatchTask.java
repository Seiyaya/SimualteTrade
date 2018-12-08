package com.seiyaya.stock.engine.task;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.seiyaya.common.bean.Order;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.util.MatchEngine;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * 撮合任务
 * @author Seiyaya
 *
 */
@Component
@Slf4j
public class StockMatchTask {
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@Autowired
	private MatchEngineCacheService matchEngineCacheService;
	
	@Autowired
	private MatchEngine matchEngine;
	
	/**
	 * 执行撮合
	 */
	public void stockMatch() {
		if(!stockCacheService.isTradeDate()) {
			return ;
		}
		
		ConcurrentLinkedQueue<Order> orderQueue = matchEngineCacheService.getMatchOrderQueue();
		if(!CollectionUtils.isEmpty(orderQueue)) {
			log.info("撮合的委托单数量:",orderQueue.size());
			matchEngine.addOrderList(orderQueue);
		}
	}
}

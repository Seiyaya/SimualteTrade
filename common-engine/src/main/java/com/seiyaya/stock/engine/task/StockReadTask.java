package com.seiyaya.stock.engine.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.seiyaya.common.bean.Order;
import com.seiyaya.stock.engine.service.MatchEngineService;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * 读委托和读撤单委托的任务
 * @author Seiyaya
 *
 */
@Slf4j
@Component
public class StockReadTask {
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@Autowired
	private MatchEngineService matchEngineService;
	
	@Autowired
	private MatchEngineCacheService matchEngineCacheService;
	
	/**
	 *  扫描普通需要撮合的委托
	 */
	public void readNeedMatchOrder() {
		if(!stockCacheService.isTradeDate()) {
			return ;
		}
		
		List<Order> orderList = null;
		try {
			orderList = matchEngineService.queryNeedMatchOrder();
		} catch (Exception e) {
			log.error("查询普通委托单出现异常:",e);
		}
		if(!CollectionUtils.isEmpty(orderList)) {
			log.info("读取的委托单数量：{}",orderList.size());
			matchEngineCacheService.addOrderToCache(orderList);
		}
	}
	
	/**
	 * 扫描撤单委托
	 */
	public void readNeedCancelOrder() {
		if(!stockCacheService.isTradeDate()) {
			return ;
		}
		
		List<Order> orderList = null;
		try {
			orderList = matchEngineService.queryNeedCancelOrder();
		} catch (Exception e) {
			log.error("扫描撤单委托出现异常:",e);
		}
		
		if(!CollectionUtils.isEmpty(orderList)) {
			log.info("读取撤单委托单数量：{}",orderList.size());
			matchEngineCacheService.addCanelOrderToCache(orderList);
		}
	}
}

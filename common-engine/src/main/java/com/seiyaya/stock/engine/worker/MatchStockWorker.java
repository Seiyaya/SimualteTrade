package com.seiyaya.stock.engine.worker;

import com.seiyaya.common.bean.Order;
import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.service.MatchEngineService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchStockWorker extends AbstractWorker{
	
	private Order order;
	
	private MatchEngineService matchEngineService = SpringUtils.getBean(MatchEngineService.BEAN_NAME, MatchEngineService.class);
	
	private MatchEngineCacheService matchEngineCacheService = SpringUtils.getBean(MatchEngineCacheService.BEAN_NAME, MatchEngineCacheService.class);
	
	public MatchStockWorker(Order order) {
		this.order = order;
	}

	@Override
	protected void execute() {
		if(order != null) {
			if(log.isInfoEnabled()) {
				log.info("处理委托单-->{}",order);
			}
			if(matchEngineCacheService.isCancel(order.getOrderId())) {
				matchEngineService.dealCancelOrder(order);
			}else if(order.isBuy()) {
				matchEngineService.dealBuyOrder(order);
			}else if(order.isSell()) {
				matchEngineService.dealSellOrder(order);
			}
		}
	}

}

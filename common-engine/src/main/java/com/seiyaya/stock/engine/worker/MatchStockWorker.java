package com.seiyaya.stock.engine.worker;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.Order;
import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.service.MatchEngineService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchStockWorker extends AbstractWorker<Bargain>{
	
	private Order order;
	
	private MatchEngineService matchEngineService = SpringUtils.getBean(MatchEngineService.BEAN_NAME, MatchEngineService.class);
	
	private MatchEngineCacheService matchEngineCacheService = SpringUtils.getBean(MatchEngineCacheService.BEAN_NAME, MatchEngineCacheService.class);
	
	public MatchStockWorker(Order order) {
		this.order = order;
	}

	@Override
	protected Bargain execute() {
		if(order != null) {
			Bargain bargain = null;
			if(log.isInfoEnabled()) {
				log.info("处理委托单-->{}",order);
			}
			if(matchEngineCacheService.isCancel(order.getOrderId())) {
				bargain = matchEngineService.dealCancelOrder(order);
			}else if(order.isBuy()) {
				bargain = matchEngineService.dealBuyOrder(order);
			}else if(order.isSell()) {
				bargain = matchEngineService.dealSellOrder(order);
			}
			return bargain;
		}
		log.error("错误的交易类型:{} --> {}",order.getOrderId(),order.getTradeType());
		return null;
	}

}

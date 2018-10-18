package com.seiyaya.common.bean;

import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.service.StockCacheService;

import lombok.Data;

@Data
public class FreeRate {

	public static final String COMMISSION = "commission";
	public static final String STAPTAX = "staptax";
	public static final String TRANSFER_FREE = "transfer_free";
	
	private StockCacheService cacheService = SpringUtils.getBean(StockCacheService.class);

	public FreeRate(){
		
	}
	public FreeRate(Order order) {
		setTotalFare(order);
	}
	
	private double staptaxFree = 0 ; // 印花税
	private double commissoinFree = 0; // 佣金
	private double transferFree = 0; // 沪市过户费
	private double totalFare = 0;
	
	public double setTotalFare(Order order) {
		double orderBalance = order.getOrderBalance();
		commissoinFree = orderBalance*cacheService.getTradeFare(COMMISSION);
		if(order.isBuy()) {
			if("SH".equals(order.getMarketId())) {
				transferFree  =  orderBalance*cacheService.getTradeFare(TRANSFER_FREE);
			}
		}else if(order.isSell()) {
			staptaxFree = orderBalance*cacheService.getTradeFare(STAPTAX);
			if("SH".equals(order.getMarketId())) {
				transferFree = orderBalance*cacheService.getTradeFare(TRANSFER_FREE);
			}
		}
		totalFare = staptaxFree+commissoinFree+transferFree;
		return totalFare;
	}
	
}

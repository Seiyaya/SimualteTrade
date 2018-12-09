package com.seiyaya.common.bean;

import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.service.StockCacheService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class FreeRate {

	public static final String COMMISSION = "commission";
	public static final String STAPTAX = "staptax";
	public static final String TRANSFER_FREE = "transfer_free";
	
	private StockCacheService cacheService = SpringUtils.getBean(StockCacheService.BEAN_NAME,StockCacheService.class);

	public FreeRate(){
		
	}
	public FreeRate(Order order) {
		setTotalFare(order);
	}
	
	public FreeRate(Bargain bargain) {
		setTotalFare(bargain);
	}
	
	private double staptaxFree = 0 ; // 印花税
	private double commissoinFree = 0; // 佣金
	private double transferFree = 0; // 沪市过户费
	private double totalFare = 0;
	
	public double setTotalFare(Order order) {
		return getTotalFare(order.getOrderBalance(), order.getStockType(), order.getMarketId(), order.isBuy());
	}
	
	public double setTotalFare(Bargain bargain) {
		double orderBalance =  bargain.getBargainBalance();
		String stockType = bargain.getStockType();
		String marketId = bargain.getMarketId();
		boolean isBuy = bargain.isBuy();
		return getTotalFare(orderBalance, stockType, marketId, isBuy);
	}
	
	/**
	 * 计算总费用
	 * @param orderBalance
	 * @param stockType
	 * @param marketId
	 * @param isBuy
	 * @return
	 */
	private double getTotalFare(double orderBalance, String stockType, String marketId, boolean isBuy) {
		commissoinFree = getMaxFareValue(orderBalance*cacheService.getTradeFare( stockType+":"+COMMISSION,COMMISSION),cacheService.getSysConfig(SystemConfig.MIN_COMMISSION));
		if( isBuy) {
			if("SH".equals( marketId)) {
				transferFree  =  getMaxFareValue(orderBalance*cacheService.getTradeFare( stockType+":"+TRANSFER_FREE,TRANSFER_FREE),cacheService.getSysConfig(SystemConfig.MIN_TRANSFER));
			}
		}else{
			staptaxFree = orderBalance*cacheService.getTradeFare( stockType +":"+STAPTAX,STAPTAX);
			if("SH".equals( marketId )) {
				transferFree = getMaxFareValue(orderBalance*cacheService.getTradeFare( stockType +":"+TRANSFER_FREE,TRANSFER_FREE),cacheService.getSysConfig(SystemConfig.MIN_TRANSFER));
			}
		}
		totalFare = staptaxFree+commissoinFree+transferFree;
		return totalFare;
	}
	
	
	private double getMaxFareValue(double value, String minValue) {
		double minDoubleValue = 0;
		try {
			minDoubleValue = Double.parseDouble(minValue);
		} catch (Exception e) {
			log.error("{}转换异常:",minValue,e);
		}
		if(value > minDoubleValue) {
			return value;
		}
		return minDoubleValue;
	}

	
}

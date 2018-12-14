package com.seiyaya.stock.settlement.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.seiyaya.stock.service.StockCacheService;
import com.seiyaya.stock.settlement.service.SettlementService;

import lombok.extern.slf4j.Slf4j;

/**
 * 盘中清算
 * @author Seiyaya
 *
 */
@Slf4j
public class OntimeSettlementTask {
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@Autowired
	private SettlementService settlementService;
	
	public void ontimeSettlementTask() {
		if(!stockCacheService.isTradeDate()) {
			log.info("非交易日不进行清算");
			return ;
		}
		log.info("开始盘中清算");
		
		log.info("1.开始更新行情信息");
		settlementService.updateHQInfo();
		log.info("1.结束更新行情信息");
		
		log.info("2.开始计算资产信息");
		settlementService.calcAssets();
		log.info("2.结束计算资产信息");
		
		log.info("结束盘中清算");
	}
}

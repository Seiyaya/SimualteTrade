package com.seiyaya.stock.settlement.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.seiyaya.stock.settlement.service.SettlementService;

import lombok.extern.slf4j.Slf4j;

/**
 * 盘前的处理
 * @author Seiyaya
 *
 */
@Slf4j
public class StartTimeSettlementTask {
	
	@Autowired
	private SettlementService settlementService;
	
	/**
	 * 盘前执行分红送股
	 */
	public void dividendStock() {
		log.info("开始执行分红送股");
		settlementService.dividendStock();
		log.info("结束执行分红送股");
	}
}

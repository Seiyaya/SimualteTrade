package com.seiyaya.stock.settlement.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.stock.service.CommonTradeService;
import com.seiyaya.stock.service.StockCacheService;
import com.seiyaya.stock.settlement.service.SettlementService;

import lombok.extern.slf4j.Slf4j;

/**
 * 盘后清算任务
 * @author Seiyaya
 *
 */
@Slf4j
public class EndTimeSettlementTask {

	@Autowired
	private StockCacheService stockCacheService;

	@Autowired
	private CommonTradeService commonTradeService;

	@Autowired
	private SettlementService settlementService;

	/**
	  *  盘后清算
	 */
	public void endTimeSettlement(){
		if (!stockCacheService.isTradeDate()) {
			log.info("非交易日不进行清算");
			return;
		}
		log.info("开始日终清算");

		log.info("1.开始修改清算状态");
		commonTradeService.updateSysConfig(SystemConfig.SETTLE_FLAG, "off");
		log.info("1.结束修改清算状态");

		log.info("2.开始更新行情");
		settlementService.updateHQInfo();
		log.info("2.结束更新行情");

		log.info("3.开始处理废单和资产、持仓生效");
		settlementService.dealOrder();
		log.info("3.结束处理废单和资产、持仓生效");

		log.info("4.开始计算资产信息");
		settlementService.calcAssets();
		log.info("4.结束计算资产信息");

		log.info("5.开始历史数据迁移");
		settlementService.historyTranfer();
		log.info("5.结束历史数据迁移");

		log.info("6.开始还原清算状态");
		commonTradeService.updateSysConfig(SystemConfig.LAST_SETTLE_DATE, DateUtils.formatNowDate());
		commonTradeService.updateSysConfig(SystemConfig.SETTLE_FLAG, "off");
		log.info("6.开始还原清算状态");
		
		log.info("结束日终清算");
	}
}

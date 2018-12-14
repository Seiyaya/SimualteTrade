package com.seiyaya.stock.settlement.service;

/**
 * 清算服务
 * @author Seiyaya
 *
 */
public interface SettlementService {

	/**
	 * 更新股票信息表现价
	 */
	void updateStcokNowPrice();

	/**
	 * 处理废单以及废单造成的盈盈
	 * 即归还资产和归还持仓
	 */
	void dealOrder();

	/**
	 * 计算资产信息
	 */
	void calcAssets();

	/**
	 * 历史数据迁移
	 * 主要包含account bargain	order
	 */
	void historyTranfer();

	/**
	 * 更新内存行情以及数据库表的现价字段
	 */
	void updateHQInfo();

	/**
	 * 分红送股
	 */
	void dividendStock();

}

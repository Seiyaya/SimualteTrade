package com.seiyaya.stock.trade.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.DBParam;

@Mapper
public interface AccountMapper {

	/**
	 * 查询账户
	 * @param param
	 * @return
	 */
	Account queryAccount(DBParam param);

	/**
	 * 更新可用资产
	 * @param param
	 * @return
	 */
	int updateCurrentBalance(DBParam param);

	/**
	 * 注册账户
	 * @param param
	 * @return
	 */
	int addAccount(DBParam param);

	/**
	 * 添加指标账户
	 * @param param
	 */
	void addAccountIndicator(DBParam param);

	/**
	 * 添加资产中间表账户
	 * @param param
	 */
	void addAccountAssets(DBParam param);

	/**
	 * 添加账户收益表
	 * @param param
	 */
	void addAccountEarn(DBParam param);

	/**
	 * 更新账户
	 * @param param
	 */
	void updateAccount(DBParam param);
	
}

package com.seiyaya.stock.settlement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.stock.settlement.bean.AssetsCompare;

@Mapper
public interface SettleMapper {

	public List<AssetsCompare> queryAccountList();

	/**
	 * 更新账户资产信息
	 * @param list
	 */
	public void updateAccountAssets(List<Account> list);

	public void updateOrder(DBParam orderParam);

	public void updateAccount();

	public void updateHoldStock();

	public void addHistBargain();

	public void delBargain();

	public void addHistOrder();

	public void delOrder();

	public void delHistAccount(DBParam param);

	public void addHistAccount(DBParam param);

	public void delHistHoldStock(DBParam param);

	public void addHistHoldStock(DBParam param);

	public void delHistStockInfo(DBParam param);

	public void addHistStockInfo(DBParam param);

	/**
	 * 更新股票现价
	 * @param stockKeyList
	 */
	public void updateStockInfo(List<Stock> stockKeyList);

}

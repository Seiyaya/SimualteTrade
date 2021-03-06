package com.seiyaya.stock.settlement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.stock.settlement.bean.AssetsCompare;
import com.seiyaya.stock.settlement.bean.Bonus;

@Mapper
public interface SettleMapper {

	/**
	 * 查询清算的账户和持仓
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:57:51
	 * @return
	 */
	public List<AssetsCompare> queryAccountList();

	/**
	 * 更新账户资产信息
	 * @param list
	 */
	public void updateAccountAssets(List<Account> list);

	/**
	 * 更新委托状态
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:53:17
	 * @param orderParam
	 */
	public void updateOrder(DBParam orderParam);

	/**
	 * 更新账户资产
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:53:47
	 */
	public void updateAccount();

	/**
	 * 更新账户持仓
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:53:59
	 */
	public void updateHoldStock();

	/**
	 * 备份当日成交
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:54:09
	 */
	public void addHistBargain();

	/**
	 * 删除当日成交
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:54:16
	 */
	public void delBargain();

	/**
	 * 备份当日委托
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:55:00
	 */
	public void addHistOrder();

	/**
	 * 删除当日委托
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:55:07
	 */
	public void delOrder();

	/**
	 * 删除历史当日历史账户
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:55:07
	 */
	public void delHistAccount(DBParam param);

	/**
	 * 添加当日历史账户
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:55:07
	 */
	public void addHistAccount(DBParam param);

	/**
	 * 删除当日持仓备份
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:55:07
	 */
	public void delHistHoldStock(DBParam param);

	/**
	 * 添加股票持仓备份
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:55:07
	 */
	public void addHistHoldStock(DBParam param);

	/**
	 * 删除历史股票信息
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:55:07
	 */
	public void delHistStockInfo(DBParam param);

	/**
	 * 添加历史股票信息
	 * @author Seiyaya
	 * @date 2018年12月19日 下午4:57:36
	 * @param param
	 */
	public void addHistStockInfo(DBParam param);

	/**
	 * 更新股票现价
	 * @param stockKeyList
	 */
	public void updateStockInfo(List<Stock> stockKeyList);

	/**
	 * 查询分红送股记录
	 * @author Seiyaya
	 * @date 2018年12月20日 下午4:25:30
	 * @param today
	 * @return
	 */
	public List<Bonus> queryBonus(String today);

	/**
	 * 更新账户可用
	 * @author Seiyaya
	 * @date 2018年12月20日 下午4:25:42
	 * @param accountList
	 */
	public void updateAccount(List<Account> accountList);

	/**
	 * 更新账户持仓
	 * @author Seiyaya
	 * @date 2018年12月20日 下午4:25:55
	 * @param holdStockList
	 */
	public void updateHoldStock(List<HoldStock> holdStockList);

	/**
	 * 添加成交记录
	 * @author Seiyaya
	 * @date 2018年12月20日 下午4:26:05
	 * @param bargainList
	 */
	public void addBargain(List<Bargain> bargainList);

	/**
	 * 更新状态
	 * @author Seiyaya
	 * @date 2018年12月20日 下午4:29:11
	 * @param today
	 * @param status
	 */
	public void updateBonus(String today,String status);

}

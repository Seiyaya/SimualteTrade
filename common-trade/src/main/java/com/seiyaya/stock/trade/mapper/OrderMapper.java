package com.seiyaya.stock.trade.mapper;

import java.util.List;

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.Order;

public interface OrderMapper {

	/**
	 * 添加委托单
	 * @param order
	 */
	long addOrder(Order order);

	/**
	 * 根据条件查询委托
	 * @param param
	 * @return
	 */
	Order queryOrder(DBParam param);

	List<Order> queryOrderList(DBParam param);

	List<Order> queryHistOrderList(DBParam param);

	List<Order> queryTodayOrderList(DBParam param);

}

package com.seiyaya.stock.trade.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.Order;

@Mapper
public interface OrderMapper {

	/**
	 * 添加委托单
	 * @param order
	 */
	int addOrder(Order order);

	/**
	 * 根据条件查询委托
	 * @param param
	 * @return
	 */
	Order queryOrder(DBParam param);

	List<Order> queryHistOrderList(DBParam param);

	List<Order> queryTodayOrderList(DBParam param);

	/**
	 * 添加撤单委托
	 * @param cancelOrder
	 */
	void addCancelOrder(DBParam cancelOrder);

	/**
	 * 根据order_id更新委托
	 * @param updateOrder
	 */
	void updateOrder(DBParam updateOrder);

	/**
	 * 更新委托单为完成
	 * @param orderParam
	 */
	void updateOrderDone(DBParam orderParam);

	/**
	 * 添加委托理由
	 * @param remarkParam
	 */
	void addOrderRemark(DBParam remarkParam);
}

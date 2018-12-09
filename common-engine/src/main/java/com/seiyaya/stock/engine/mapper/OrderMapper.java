package com.seiyaya.stock.engine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.Order;

@Mapper
public interface OrderMapper {

	List<Order> queryOrder(DBParam param);

	void updateOrderList(List<DBParam> params);

	List<Order> queryCancelOrder(DBParam param);

	void updateCancelOrderList(List<DBParam> params);

	void updateOrder(DBParam orderParam);

	void updateCancelOrder(DBParam cancelParam);

}

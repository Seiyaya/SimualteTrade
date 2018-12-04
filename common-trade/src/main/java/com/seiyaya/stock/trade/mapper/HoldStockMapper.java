package com.seiyaya.stock.trade.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HoldStock;

@Mapper
public interface HoldStockMapper {

	int updateCurrentQty(DBParam param);

	HoldStock queryHoldStock(DBParam param);

	List<HoldStock> queryHoldStockList(DBParam param);

}

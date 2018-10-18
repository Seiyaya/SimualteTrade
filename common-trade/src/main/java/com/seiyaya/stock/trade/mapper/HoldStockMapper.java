package com.seiyaya.stock.trade.mapper;

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HoldStock;

public interface HoldStockMapper {

	int updateCurrentQty(DBParam param);

	HoldStock queryHoldStock(DBParam param);

}

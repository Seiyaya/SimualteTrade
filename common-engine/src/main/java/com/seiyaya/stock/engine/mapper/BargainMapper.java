package com.seiyaya.stock.engine.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HoldStock;

@Mapper
public interface BargainMapper {

	HoldStock queryHoldStockInfo(DBParam holdStockParam);

	void addBargain(Bargain bargain);

	void addHoldStock(HoldStock holdStock);

	void delHoldStock(int holdId);

	void updateAccountBalance(DBParam param);
	
	
}

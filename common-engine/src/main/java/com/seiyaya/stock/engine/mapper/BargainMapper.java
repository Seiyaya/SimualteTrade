package com.seiyaya.stock.engine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.CompleteProfit;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.PositionChange;

@Mapper
public interface BargainMapper {
	
	HoldStock queryHoldStockInfo(DBParam holdStockParam);

	void addBargain(Bargain bargain);

	void addHoldStock(HoldStock holdStock);

	void delHoldStock(int holdId);

	void updateAccountBalance(DBParam param);

	void addCompleteProfitList(List<CompleteProfit> completeProfitList);

	void addPositionChangeList(List<PositionChange> positionChangeList);

	List<Bargain> queryBargain(DBParam bargainParam);
	
	
}

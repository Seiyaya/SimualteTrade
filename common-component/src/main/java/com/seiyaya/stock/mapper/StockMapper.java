package com.seiyaya.stock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.Stock;

@Mapper
public interface StockMapper {
	
	int insertStockInfoList(List<Stock> list);

	int getStockInfoNum(String date);

	void deleteStockInfo();

	List<Stock> queryStockList();
}

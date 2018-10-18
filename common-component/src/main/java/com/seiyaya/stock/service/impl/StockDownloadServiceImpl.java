package com.seiyaya.stock.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.http.XueQiuUtils;
import com.seiyaya.stock.mapper.StockMapper;
import com.seiyaya.stock.service.StockDownloadkService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockDownloadServiceImpl implements StockDownloadkService{
	
	@Autowired
	private StockMapper stockMapper;
	
	@Autowired
	private XueQiuUtils xueQiuUtils;
	
	@Override
	public int insertFundInfoByXueQiu(String stockCode) {
		log.info("下载的股票代码:{}",stockCode);
		return insertFundInfoByXueQiu(new String[] {stockCode});
	}

	@Override
	public int insertFundInfoByXueQiu(String[] stockCodes) {
		if(stockCodes!=null && stockCodes.length>0) {
			for(int i=0;i<stockCodes.length;i++) {
				String stockCode = stockCodes[i];
				List<Stock> stocks = xueQiuUtils.getFundInfo(stockCode);
				stockMapper.insertStockInfoList(stocks);
			}
		}
		return stockCodes.length;
	}

	@Override
	public int insertStockInfoByXueQiu() {
		List<Stock> stocks = xueQiuUtils.getStockInfoList();
		return stockMapper.insertStockInfoList(stocks);
	}

}

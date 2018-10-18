package com.seiyaya.common.http;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.seiyaya.common.bean.Stock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestXueQiuUtils {
	
	@Autowired
	private HQUtils hqUtils;
	
	@Test
	public void getFundData() {
		List<Stock> fundInfo = hqUtils.getFundInfo("161725");
		log.info("stockInfos -> {}",fundInfo);
	}
	
	@Test
	public void getStockInfo() {
		List<Stock> stockInfoList = hqUtils.getStockInfoList();
		log.info("股票信息:{}",stockInfoList);
	}
	
	@Test
	public void getStockByType() {
		hqUtils.getStockInfoListByType("zxb");
	}
}

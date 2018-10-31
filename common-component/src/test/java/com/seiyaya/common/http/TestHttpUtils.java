package com.seiyaya.common.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestHttpUtils {
	
	@Autowired
	StockCacheService stockCacheService;
	
	@Test
	public void testUse() {
		log.info("{}",stockCacheService.getStockByKey(SystemConfig.SETTLE_FLAG));
		stockCacheService.downloadExponentInfo();
	}
}

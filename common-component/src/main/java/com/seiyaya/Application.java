package com.seiyaya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.seiyaya.stock.service.StockDownloadkService;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		StockDownloadkService bean = context.getBean(StockDownloadkService.class);
		//"161725","161724","163001","001076","110022"
		bean.insertFundInfoByXueQiu(new String[] {"161725","161724","163001","001076","110022"});
//		bean.insertStockInfoByXueQiu();
		context.close();
	}
}

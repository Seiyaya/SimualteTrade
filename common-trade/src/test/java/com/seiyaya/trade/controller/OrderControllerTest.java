package com.seiyaya.trade.controller;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.seiyaya.BaseTest;
import com.seiyaya.common.bean.DBParam;

public class OrderControllerTest extends BaseTest{
	
	private String accountId  = "1";
	
	@Test
	@Rollback(true)
	public void addBuyOrder() throws Exception {
		DBParam param  = new DBParam().set("accountId", accountId)
		.set("stockCode", "000001").set("marketId", "SZ")
		.set("orderQty", "100").set("orderPrice", "10.6")
		.set("tradeType", "0");
		invokePost("/order/add", param);
	}
	
	@Test
	public void addSellOrder() throws Exception {
		DBParam param  = new DBParam().set("accountId", accountId)
				.set("stockCode", "000001").set("marketId", "SZ")
				.set("orderQty", "100").set("orderPrice", "10.6")
				.set("tradeType", "1");
		invokePost("/order/add", param);
	}
	
	@Test
	public void queryTodayOrderList() throws Exception {
		DBParam param = new DBParam().set("accountId", accountId);
		invokeGet("/order/query/today", param);
	}
	
	@Test
	public void queryHistOrderList() throws Exception {
		DBParam param = new DBParam().set("accountId", accountId);
		invokeGet("/order/query/hist", param);
	}
	
	@Test
	public void queryCancelOrderList() throws Exception {
		DBParam param = new DBParam().set("accountId", accountId);
		invokeGet("/order/query/cancel", param);
	}
}

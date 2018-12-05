package com.seiyaya.trade.controller;

import org.junit.Test;

import com.seiyaya.BaseTest;
import com.seiyaya.common.bean.DBParam;

/**
 * 成交相关控制器测试
 * @author Seiyaya
 *
 */
public class BargainControllerTest extends BaseTest{
	
	
	@Test
	public void testTodayBargain() {
		DBParam param = new DBParam().set("accountId", "1");
		invokeGet("/bargain/today/query", param);
	}
	
	@Test
	public void testHistBargain() {
		DBParam param = new DBParam().set("accountId", "1");
		invokeGet("/bargain/hist/query", param);
	}
}

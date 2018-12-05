package com.seiyaya.trade.controller;

import org.junit.Test;

import com.seiyaya.BaseTest;
import com.seiyaya.common.bean.DBParam;

/**
 * 持仓测试
 * @author Seiyaya
 *
 */
public class HoldStockControllerTest extends BaseTest{
	
	@Test
	public void testQueryHoldStock() {
		DBParam param = new DBParam().set("accountId", "1");
		invokeGet("/holdstock/query", param);
	}
}

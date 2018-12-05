package com.seiyaya.trade.controller;

import org.junit.Test;

import com.seiyaya.BaseTest;
import com.seiyaya.common.bean.DBParam;

/**
 * 账户相关测试
 * @author Seiyaya
 *
 */
public class AccountControllerTest extends BaseTest{
	
	@Test
	public void queryAccountAssets() {
		DBParam param = new DBParam().set("accountId", "1");
		invokeGet("/account/query/assets", param);
	}
	
	@Test
	public void addAccount() {
		DBParam param = new DBParam().set("userId", "1")
				.set("accountName", "张三二号");
		invokePost("/account/add", param);
	}
}

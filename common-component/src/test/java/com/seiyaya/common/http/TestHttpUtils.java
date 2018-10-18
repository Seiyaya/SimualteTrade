package com.seiyaya.common.http;

import java.util.HashMap;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestHttpUtils {
	
	public static final String URL = "https://fund.xueqiu.com/dj/open/fund/growth/161725";
	
	@Test
	public void testUse() {
		val params = new HashMap<String,Object>();
		params.put("day", "360");
		HttpUtils httpUtils = new HttpUtils();
		String sendGet = httpUtils.sendGet(URL, params);
		JSONArray jsonArray = JSONObject.parseObject(sendGet).getJSONObject("data").getJSONArray("fund_nav_growth");
		jsonArray.forEach((json) -> {
			if(json instanceof JSONObject) {
				JSONObject innerJsonObject = (JSONObject) json;
				log.info("info->{}",innerJsonObject);
			}
		});
	}
}

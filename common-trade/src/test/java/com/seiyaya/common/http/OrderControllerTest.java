package com.seiyaya.common.http;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.seiyaya.common.bean.DBParam;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void contextLoader() {
		
	}
	
	@Test
	public void addOrder() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("accountId", "1");
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/order/add").param("accountId", "1")
				.param("stockCode", "000001").param("marketId", "SZ")
				.param("orderQty", "100").param("orderPrice", "10.6").param("tradeType", "0"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
		log.info(result.getResponse().getContentAsString());
	}
}

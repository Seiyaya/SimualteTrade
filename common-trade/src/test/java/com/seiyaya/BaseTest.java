package com.seiyaya;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.utils.SpringUtils;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BaseTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		new SpringUtils().setApplicationContext(wac);
	}
	
	
	public void invokeGet(String url,DBParam param) {
		try {
			MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(url);
			param.entrySet().forEach((entry) -> {
				get.param(entry.getKey(), entry.getValue().toString());
			});
			MvcResult result = mockMvc
					.perform(get)
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
			log.info(result.getResponse().getContentAsString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void invokePost(String url,DBParam param){
		try {
			MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(url);
			param.entrySet().forEach((entry) -> {
				post.param(entry.getKey(), entry.getValue().toString());
			});
			MvcResult result = mockMvc
					.perform(post)
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
			log.info(result.getResponse().getContentAsString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

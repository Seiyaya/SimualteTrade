package com.seiyaya.stock.engine.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.seiyaya.common.utils.SpringUtils;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StockReadTaskTest{
	
	@Autowired
	private WebApplicationContext wac;
	
	@Before
	public void before() {
		new SpringUtils().setApplicationContext(wac);
	}
	
	@Test
	public void testReadTask() {
		StockReadTask task = SpringUtils.getBean(StockReadTask.class);
		task.readNeedMatchOrder();
	}
}

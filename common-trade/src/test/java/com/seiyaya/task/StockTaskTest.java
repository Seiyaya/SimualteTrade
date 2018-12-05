package com.seiyaya.task;

import org.junit.Test;

import com.seiyaya.BaseTest;
import com.seiyaya.stock.task.StockTask;

/**
 * 股票信息定时任务测试
 * @author Seiyaya
 *
 */
public class StockTaskTest extends BaseTest{
	
	@Test
	public void testDBInsertStock() {
		new StockTask().addStockDB();
	}
}

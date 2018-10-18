package com.seiyaya.common.http;

import org.junit.Test;

import com.seiyaya.common.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDateUtils {
	
	@Test
	public void formatMill() {
		String formatMilliSecond = DateUtils.formatMilliSecond(1539097730338L);
		log.info("{}",formatMilliSecond);
		formatMilliSecond = DateUtils.formatMilliSecond(System.currentTimeMillis());
		log.info("{}",formatMilliSecond);
	}
}

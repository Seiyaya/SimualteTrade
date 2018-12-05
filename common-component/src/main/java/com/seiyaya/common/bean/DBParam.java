package com.seiyaya.common.bean;

import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * mybatis的入参，避免每次map需要加上泛型
 * @author Seiyaya
 *
 */
@SuppressWarnings("serial")
@Slf4j
public class DBParam extends HashMap<String, Object> {

	public DBParam set(String key, Object value) {
		put(key, value);
		return this;
	}
	
	public String getString(String key) {
		return get(key).toString();
	}

	public Integer getInt(String key) {
		try {
			return Integer.parseInt(get(key).toString());
		} catch (Exception e) {
			log.error("{}转换成int异常:",key,e);
			return 0;
		}
	}
}

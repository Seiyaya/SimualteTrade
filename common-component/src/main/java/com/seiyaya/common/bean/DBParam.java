package com.seiyaya.common.bean;

import java.util.HashMap;

/**
 * mybatis的入参，避免每次map需要加上泛型
 * @author Seiyaya
 *
 */
@SuppressWarnings("serial")
public class DBParam extends HashMap<String, Object> {

	public DBParam set(String key, Object value) {
		put(key, value);
		return this;
	}
	
	public String getString(String key) {
		return get(key).toString();
	}
}

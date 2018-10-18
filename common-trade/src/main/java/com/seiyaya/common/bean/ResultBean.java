package com.seiyaya.common.bean;

import java.util.HashMap;

import lombok.Data;

/**
 * 响应结果集 
 * @author Seiyaya
 *
 */
@Data
public class ResultBean {
	
	private String msg = "操作成功";
	/**
	 * <0表示系统内部异常
	 * >0表示业务系统异常
	 * =0正常
	 */
	private int status = 0;
	private HashMap<String, Object> results = new HashMap<>();
	
	public ResultBean() {
	}
	
	public ResultBean(String msg, int status) {
		this.msg = msg;
		this.status = status;
	}

	public ResultBean(String msg) {
		this.msg = msg;
	}
	
	public ResultBean setResults(String key , Object value) {
		this.results.put(key, value);
		return this;
	}
	
}

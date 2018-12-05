package com.seiyaya.common.bean;

import java.util.HashMap;

/**
 * 响应结果集 
 * @author Seiyaya
 *
 */
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

	public String getMsg() {
		return msg;
	}

	public ResultBean setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public ResultBean setStatus(int status) {
		this.status = status;
		return this;
	}

	public HashMap<String, Object> getResults() {
		return results;
	}

	public ResultBean setResults(HashMap<String, Object> results) {
		this.results = results;
		return this;
	}
}

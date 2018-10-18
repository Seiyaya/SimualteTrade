package com.seiyaya.common.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统配置
 * @author Seiyaya
 *
 */
@Data
@Slf4j
public class SystemConfig {
	
	public static final String SETTLE_FLAG = "settle_flag";
	
	private String name;
	private String key;
	private String value;
	private String descritpion;
	private String createDate;
	private int type;
	
	public static boolean isSettlementTime(String value) {
		if("on".equals(value)) {
			return true;
		}
		
		if("off".equals(value)) {
			return false;
		}
		log.error("系统配置{}异常",SETTLE_FLAG);
		return false;
	}
}

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
	
	public static final String EXPONENT_LIST = "exponeny_list";
	
	public static final String HQ_URL = "hq_url";
	
	public static final String HQ_STOCK_TYPE = "hq_stock_type";
	
	public static final String HQ_ENCODING = "hq_encoding";
	
	public static final String HQ_FUNCTION = "hq_function";
	
	public static final String BONUS_LAST_DATE = "bonus_last_date";
	
	public static final String BONUS_DOWNLOAD_URL = "bonus_download_url";
	
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

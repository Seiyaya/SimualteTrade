package com.seiyaya.common.bean;

import lombok.Data;

/**
 * 交易账户
 * @author Seiyaya
 *
 */
@Data
public class Account {
	
	public static final String IS_VALID = "1";
	
	private int accountId;
	private String accountName;
	private String accountType;
	private String userId;
	private String moneyId;
	private double initBalance;
	private double currentBalance;
	private double freezedBalance;
	private double totalAssets;
	private double totalMarket;
	private double createDate;
	private double updateDate;
	private String remark;
	private String state;
	private String version;
	
	public boolean isValid() {
		if(IS_VALID.equals(state)) {
			return true;
		}
		return false;
	}
}

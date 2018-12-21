package com.seiyaya.stock.settlement.bean;

import lombok.Data;

/**
 * 分红送股记录
 * @author Seiyaya
 *
 */
@Data
public class Bonus {
	
	private String marketId;
	private String stockCode;
	private String bonusDate;
	private double rate;
	private String type;
	private int totalQty;
	
	private double costPrice;
	private double holdPrice;
	private int accountId;
	private int holdId;
	/**
	 * 是否是分红
	 * @author Seiyaya
	 * @date 2018年12月20日 下午3:56:39
	 * @return
	 */
	public boolean isBonus() {
		return "0".equals(type);
	}
}

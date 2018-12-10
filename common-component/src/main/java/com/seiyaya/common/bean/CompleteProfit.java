package com.seiyaya.common.bean;

import lombok.Data;

/**
 * 完整收益
 * @author Seiyaya
 *
 */
@Data
public class CompleteProfit {
	
	private int accountId;
	private int holdId;
	private String marketId;
	private String stockCode;
	private String stockName;
	
	private double tradeQty;
	private double tradeFare;
	private double earn;
	private double earnRate;
	private double buyPrice;
	private double sellPrice;
	private String startDate;
	private String endDate;
	private int tradeDays;
}

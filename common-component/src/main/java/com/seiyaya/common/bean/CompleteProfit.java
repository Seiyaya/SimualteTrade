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
}

package com.seiyaya.common.bean;

import lombok.Data;

/**
 * 持仓
 * @author Seiyaya
 *
 */
@Data
public class HoldStock {
	private int holdId;
	private int accountId;
	private String marketId;
	private String stockCode;
	private int totalQty;
	private int currentQty;
	private double costPrice;
	private double holdPrice;
	private String createDate;
	private String updateDate;
	private String remark;
	private String version;
}

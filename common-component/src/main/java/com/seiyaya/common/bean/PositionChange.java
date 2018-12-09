package com.seiyaya.common.bean;

import lombok.Data;

/**
 * 仓位变动
 * @author Seiyaya
 *
 */
@Data
public class PositionChange {
	private int accountId;
	private int bargainId;
	private double accountBeforePosition;
	private double accountAfterPosition;
	private double stockBeforePosition;
	private double stockAfterPosition;
}

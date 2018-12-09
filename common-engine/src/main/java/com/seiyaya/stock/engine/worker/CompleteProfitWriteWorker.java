package com.seiyaya.stock.engine.worker;

import com.seiyaya.common.bean.CompleteProfit;

/**
 * 计算完整收益以及写入
 * @author Seiyaya
 *
 */
public class CompleteProfitWriteWorker extends AbstractWorker<CompleteProfit> {

	private CompleteProfit completeProfit;
	
	public CompleteProfitWriteWorker(CompleteProfit completeProfit) {
		this.completeProfit = completeProfit;
	}
	
	@Override
	public CompleteProfit execute() {
		return completeProfit;
	}

}

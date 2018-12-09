package com.seiyaya.stock.engine.worker;

import com.seiyaya.common.bean.PositionChange;

/**
 * 仓位变动写入worker
 * @author Seiyaya
 *
 */
public class PositionChangeWriteWorker extends AbstractWorker<PositionChange>{

	private PositionChange positionChange;
	
	public PositionChangeWriteWorker(PositionChange positionChange) {
		this.positionChange = positionChange;
	}
	
	@Override
	public PositionChange execute() {
		return positionChange;
		
	}

}

package com.seiyaya.stock.engine.worker;

import com.seiyaya.common.bean.Bargain;

public class BargainWriteWorker extends AbstractWorker{

	private Bargain bargain;
	
	public BargainWriteWorker(Bargain bargain) {
		this.bargain = bargain;
	}
	
	@Override
	protected void execute() {
	}

}

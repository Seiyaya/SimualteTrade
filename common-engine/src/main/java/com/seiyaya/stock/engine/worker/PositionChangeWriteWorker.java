package com.seiyaya.stock.engine.worker;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.PositionChange;
import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.engine.service.MatchEngineService;

/**
 * 仓位变动写入worker
 * @author Seiyaya
 *
 */
public class PositionChangeWriteWorker extends AbstractWorker<PositionChange>{

	private Bargain bargain;
	
	private MatchEngineService matchEngineService = SpringUtils.getBean(MatchEngineService.BEAN_NAME, MatchEngineService.class);
	
	public PositionChangeWriteWorker(Bargain bargain) {
		this.bargain = bargain;
	}
	
	@Override
	public PositionChange execute() {
		PositionChange positionChange = new PositionChange();
		positionChange.setAccountId(bargain.getAccountId());
		positionChange.setBargainId(bargain.getBargainId());
		double accountBeforePosition = 0;
		double accountAfterPosition = 0;
		double stockBeforePosition = 0;
		double stockAfterPosition = 0;
		
		
		/**
		 *  此处处理的疑问：
		 *  	具有延时性，没有记录当时的资产情况，连续成交两笔将会计算的不准确
		 *  	暂时的解决方案可考虑和写成交在一起
		 *  	或者记录写成交时的总资产和成交前的总持仓
		 *  	暂时还是比较推荐写在一起
		 */
		return positionChange;
		
	}

}

package com.seiyaya.stock.engine.worker;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.EnumValue;
import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.service.MatchEngineService;

import lombok.extern.slf4j.Slf4j;

/**
 * 写入成交单
 * @author Seiyaya
 *
 */
@Slf4j
public class BargainWriteWorker extends AbstractWorker<Void>{

	private Bargain bargain;
	
	private MatchEngineService matchEngineService = SpringUtils.getBean(MatchEngineService.BEAN_NAME, MatchEngineService.class);
	
	private MatchEngineCacheService matchEngineCacheService = SpringUtils.getBean(MatchEngineCacheService.BEAN_NAME, MatchEngineCacheService.class);
	
	public BargainWriteWorker(Bargain bargain) {
		this.bargain = bargain;
	}
	
	@Override
	protected Void execute() {
		try {
			dealBargain();
		} catch (Exception e) {
			log.error("写入成交单异常:",e);
			matchEngineCacheService.addBargainToCache(bargain);
		}
		return null;
	}

	/**
	 * 处理成交单
	 */
	private void dealBargain() {
		if(EnumValue.TRADE_STATUS_2.equals(bargain.getTradeStatus())) {
			//已成成交单的处理
			if(EnumValue.TRADE_TYPE_0.equals(bargain.getTradeType())) {
				matchEngineService.dealBuyBargain(bargain);
			}else if(EnumValue.TRADE_TYPE_1.equals(bargain.getTradeType())) {
				matchEngineService.dealSellBargain(bargain);
			}
		}else if(EnumValue.TRADE_STATUS_4.equals(bargain.getTradeStatus())) {
			//撤单成功成交单的处理
			if(EnumValue.TRADE_TYPE_0.equals(bargain.getTradeType())) {
				matchEngineService.dealBuyCancelBargain(bargain);
			}else if(EnumValue.TRADE_TYPE_1.equals(bargain.getTradeType())) {
				matchEngineService.dealSellCancelBargain(bargain);
			}
		}
	}

}

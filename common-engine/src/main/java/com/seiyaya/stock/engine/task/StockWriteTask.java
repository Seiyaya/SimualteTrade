package com.seiyaya.stock.engine.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.CompleteProfit;
import com.seiyaya.common.bean.PositionChange;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.service.MatchEngineService;
import com.seiyaya.stock.engine.util.MatchEngine;
import com.seiyaya.stock.service.StockCacheService;

/**
 * 撮合写任务
 * @author Seiyaya
 *
 */
@Component
public class StockWriteTask {
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@Autowired
	private MatchEngineCacheService matchEngineCacheService;
	
	@Autowired
	private MatchEngineService matchEngineService;
	
	@Autowired
	private MatchEngine matchEngine;
	
	private static final int THREAD_NUM = 8;
	
	/**
	 * 写入匹配成功的成交单
	 */
	public void writeMatchStock() {
		if(!stockCacheService.isTradeDate() || !DateUtils.inTwoTradeTime()) {
			return ;
		}
		ConcurrentLinkedQueue<Bargain> bargainQueue = matchEngineCacheService.getBargainQueue();
		
		Map<Integer, ConcurrentLinkedQueue<Bargain>> map = new HashMap<>();
		for(int i=0;i<THREAD_NUM;i++) {
			ConcurrentLinkedQueue<Bargain> list = new ConcurrentLinkedQueue<>();
			for (Iterator<Bargain> iterator = bargainQueue.iterator(); iterator.hasNext();)
            {
                Bargain bargain = iterator.next();
                Integer accountId =  bargain.getAccountId();
                Integer remain = accountId % THREAD_NUM;
                if ( remain == i )
                {
                    list.add(bargain);
                }
            }
			map.put(i, list);
		}
		
		map.entrySet().forEach((entry) ->{
			if(!entry.getValue().isEmpty()) {
				matchEngineService.dealBargains(entry.getValue());
			}
		});
	}
	
	/**
	 * 写入完整收益
	 */
	public void writeCompleteProfit() {
		ConcurrentLinkedQueue<CompleteProfit> completeProfits = matchEngineCacheService.getCompleteProfitQueue();
		if(!CollectionUtils.isEmpty(completeProfits)) {
			matchEngine.addCompleteProfitList(completeProfits);
		}
	}
	
	/**
	 *写入仓位变动
	 */
	public void writePositionChange() {
		ConcurrentLinkedQueue<PositionChange> positionChanges = matchEngineCacheService.getPositionChangeQueue();
		if(!CollectionUtils.isEmpty(positionChanges)) {
			matchEngine.addPositionChange(positionChanges);
		}
	}
}

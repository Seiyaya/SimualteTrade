package com.seiyaya.stock.engine.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.stock.engine.service.MatchEngineCacheService;
import com.seiyaya.stock.engine.service.MatchEngineService;
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
	
	private static final int THREAD_NUM = 8;
	
	public void writeMatchStock() {
		if(!stockCacheService.isTradeDate()) {
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
}

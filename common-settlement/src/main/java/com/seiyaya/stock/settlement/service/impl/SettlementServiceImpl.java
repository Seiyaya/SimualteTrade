package com.seiyaya.stock.settlement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.DBPage;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.EnumValue;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.stock.service.StockCacheService;
import com.seiyaya.stock.settlement.bean.AssetsCompare;
import com.seiyaya.stock.settlement.mapper.SettleMapper;
import com.seiyaya.stock.settlement.service.SettlementService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SettlementServiceImpl implements SettlementService {

	@Autowired
	private StockCacheService stockCacheService;
	
	@Autowired
	private SettleMapper settleMapper;
	
	
	@Override
	public void updateStcokNowPrice() {
		List<Stock> stockKeyList = stockCacheService.getStockKeyList();
		settleMapper.updateStockInfo(stockKeyList);
	}

	@Override
	public void dealOrder() {
		/**
		 * 1.将委托处理为废单
		 * 2.归还冻结资产
		 * 3.生效持仓
		 */
		DBParam orderParam = new DBParam()
				.set("trade_status", EnumValue.TRADE_STATUS_5)
				.set("deal_flag", EnumValue.DEAL_FLAG_2);
		settleMapper.updateOrder(orderParam);
		settleMapper.updateAccount();
		settleMapper.updateHoldStock();
	}

	@Override
	public void calcAssets() {
		int pageIndex = 1;
		int pageSize = 2000;
		
		while(true) {
			PageHelper.startPage(pageIndex, pageSize);
			List<AssetsCompare> accountList = settleMapper.queryAccountList();
			DBPage<AssetsCompare> result = new DBPage<>(accountList);
			
			int startId = accountList.get(0).getAccountId();
			int endId = accountList.get(accountList.size()-1).getAccountId();
			log.info("计算的范围{}--->{}",startId,endId);
			
			Map<Integer,Map<String,Double>> marketTmp = new HashMap<>();
			if(!CollectionUtils.isEmpty(accountList)) {
				for(AssetsCompare assets : accountList) {
					double tmpMarket = 0;
					int accountId = assets.getAccountId();
					Stock stock = stockCacheService.getStockByKey(assets.getMarketId()+assets.getStockCode());
					if(stock == null) {
						log.warn("{}  {} 行情信息为空",assets.getMarketId(),assets.getStockCode());
					}else {
						tmpMarket = stock.getNowPrice() * assets.getTotalQty();
					}
					if(marketTmp.containsKey(accountId)) {
						Map<String, Double> marketInnerMap = marketTmp.get(accountId);
						double totalMarket = marketInnerMap.get("total_market");
						totalMarket += tmpMarket;
						marketInnerMap.put("total_market", totalMarket);
					}else {
						Map<String,Double> marketInnerMap = new HashMap<>();
						marketInnerMap.put("freezed_balance", assets.getFreezedBalance());
						marketInnerMap.put("current_balance", assets.getCurrentBalance());
						marketTmp.put(accountId, marketInnerMap);
					}
				}
				final List<Account> list = new ArrayList<>(marketTmp.entrySet().size());
				marketTmp.entrySet().forEach((entry) -> {
					Account account = new Account();
					Map<String, Double> value = entry.getValue();
					account.setAccountId(entry.getKey());
					account.setTotalMarket(value.get("total_market"));
					account.setTotalAssets(value.get("total_market")+value.get("current_balance")+value.get("freezed_balance"));
					list.add(account);
				});
				settleMapper.updateAccountAssets(list);
			}
			
			pageIndex++;
			if(pageIndex > result.getTotal()) {
				break;
			}
		}
	}

	@Override
	public void historyTranfer() {
		DBParam param = new DBParam().set("current_date", DateUtils.formatNowDate());
		settleMapper.addHistBargain();
		settleMapper.delBargain();
		
		settleMapper.addHistOrder();
		settleMapper.delOrder();
		
		settleMapper.delHistAccount(param);
		settleMapper.addHistAccount(param);
		
		settleMapper.delHistHoldStock(param);
		settleMapper.addHistHoldStock(param);
		
		settleMapper.delHistStockInfo(param);
		settleMapper.addHistStockInfo(param);
	}

	@Override
	public void updateHQInfo() {
		boolean isGetHq = true;
		while (isGetHq) {
			try {
				stockCacheService.initStockCache();
				updateStcokNowPrice();
				isGetHq = false;
			} catch (Exception e) {
				log.error("获取行情信息出错，等待60S");
				try {
					TimeUnit.SECONDS.sleep(60);
				} catch (InterruptedException e1) {
					throw new RuntimeException(e1);
				}
			}
		}
	}

	@Override
	public void dividendStock() {
		/**
		 * 1.添加今日分红成交
		 * 2.添加今日送股成交
		 * 3.更新分红导致的账户可用增加
		 * 4.更新送股导致的账户持仓增加，以及成本价
		 * 5.更新分红导致的成本价降低
		 * 6.更新最后一次执行该任务的日期
		 */
	}

}

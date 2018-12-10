package com.seiyaya.stock.engine.worker;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.CompleteProfit;
import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.EnumValue;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.engine.mapper.BargainMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 计算完整收益以及写入
 * @author Seiyaya
 *
 */
@Slf4j
public class CompleteProfitWriteWorker extends AbstractWorker<CompleteProfit> {

	private Bargain bargain;
	
	private BargainMapper bargainMapper = SpringUtils.getBean(BargainMapper.class);
	
	public CompleteProfitWriteWorker(Bargain bargain) {
		this.bargain = bargain;
	}
	
	@Override
	public CompleteProfit execute() {
		CompleteProfit completeProfit = packageCompleteProfit();
		
		//获取所有的成交记录
		DBParam bargainParam = new DBParam().set("account_id", bargain.getAccountId())
				.set("hold_id", bargain.getHoldId());
		List<Bargain> bargainList = bargainMapper.queryBargain(bargainParam);
		if(!CollectionUtils.isEmpty(bargainList)) {
			String startDate = bargainList.get(0).getExecDate();
			int tradeQty = 0;
			double buyBalance = 0;
			double tradeFare = 0;
			double sellBalance = 0;
			for(Bargain bargain : bargainList) {
				if(startDate.compareTo(bargain.getExecDate()) > 0) {
					startDate = bargain.getExecDate();
				}
				//增加持仓  都可以处理为买入
				if(EnumValue.TRADE_TYPE_0.equals(bargain.getTradeType()) || EnumValue.TRADE_TYPE_3.equals(bargain.getTradeType())) {
					tradeQty += bargain.getExecQty();
					buyBalance += bargain.getTotalBalance();
					tradeFare += bargain.getTotalFare();
				}else if(EnumValue.TRADE_TYPE_1.equals(bargain.getTradeType()) || EnumValue.TRADE_TYPE_2.equals(bargain.getTradeType())) {
					//可用增加  都可以处理为卖出
					sellBalance += bargain.getTotalBalance();
					tradeFare += bargain.getTotalFare();
				}else {
					log.error("无效的交易类型:{},trade_type -> {}",bargain.getBargainId(),bargain.getTradeType());
				}
			}
			String endDate = DateUtils.formatNowDate();
			completeProfit.setTradeFare(tradeFare);
			completeProfit.setEarn(sellBalance - buyBalance);
			completeProfit.setTradeQty(tradeQty);
			completeProfit.setEarnRate((sellBalance - buyBalance)/buyBalance);
			completeProfit.setBuyPrice(buyBalance/tradeQty);
			completeProfit.setSellPrice(sellBalance/tradeQty);
			completeProfit.setStartDate(startDate);
			completeProfit.setEndDate(endDate);
			completeProfit.setTradeDays(DateUtils.getTradeDays(startDate, endDate));
		}else {
			log.error("完整收益计算出错：没有对应的成交记录:{}",bargain.getHoldId());
		}
		return completeProfit;
	}

	private CompleteProfit packageCompleteProfit() {
		CompleteProfit completeProfit = new CompleteProfit();
		completeProfit.setAccountId(bargain.getAccountId());
		completeProfit.setHoldId(bargain.getHoldId());
		completeProfit.setMarketId(bargain.getMarketId());
		completeProfit.setStockCode(bargain.getStockCode());
		completeProfit.setStockName(bargain.getStockName());
		return completeProfit;
	}

}

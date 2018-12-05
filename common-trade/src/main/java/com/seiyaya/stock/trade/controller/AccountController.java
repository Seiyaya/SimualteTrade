package com.seiyaya.stock.trade.controller;

import static com.seiyaya.common.utils.CheckConditionUtils.checkCondition;
import static com.seiyaya.common.utils.NumberFormatUtils.formatNumber;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.ResultBean;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.stock.service.StockCacheService;
import com.seiyaya.stock.trade.service.TradeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private StockCacheService stockCacheService;
	
	@PostMapping("/add")
	public ResultBean addAccount(Integer userId,String accountName) {
		checkCondition(userId == null, "用户帐号不能为空");
		
		Integer accountId = tradeService.addAccount(userId,accountName);
		ResultBean result = new ResultBean("创建账户成功");
		result.setResults("account_id", accountId);
		return result;
	}
	
	@GetMapping("/query/assets")
	public ResultBean queryAccountAssets(Integer accountId) {
		checkCondition(accountId == null, "交易帐号不能为空");
		
		Account account = tradeService.queryAccount(accountId);
		List<HoldStock> holdStockList = tradeService.queryHoldStock(accountId);
		double totalMarket = 0;
		if(!CollectionUtils.isEmpty(holdStockList)) {
			for(HoldStock hold :holdStockList) {
				Stock stock = stockCacheService.getStockByKey(hold.getMarketId()+hold.getStockCode());
				if(stock!=null) {
					double market = hold.getTotalQty() * stock.getNowPrice();
					totalMarket += market;
				}else {
					log.warn("{}:{}没有行情",hold.getMarketId(),hold.getStockCode());
				}
			}
		}
		account.setTotalMarket(formatNumber(totalMarket));
		//总资产 = 市值+可用+冻结
		double totalAssets = totalMarket+account.getCurrentBalance()+account.getFreezedBalance();
		account.setTotalAssets(formatNumber(totalMarket+account.getCurrentBalance()+account.getFreezedBalance()));
		//仓位 = 市值/总资产
		account.setPosition(formatNumber(totalMarket/totalAssets*100));
		//浮动盈亏 = 总资产 - 初始资产
		account.setProfitEarn(formatNumber(totalAssets- account.getInitBalance()));
		
		//打包出参
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("account", account);
		resultBean.setMsg("查询实时资产成功");
		return resultBean;
	}
}

package com.seiyaya.stock.trade.controller;

import static com.seiyaya.common.utils.CheckConditionUtils.checkCondition;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.DBPage;
import com.seiyaya.common.bean.FreeRate;
import com.seiyaya.common.bean.HoldStock;
import com.seiyaya.common.bean.Order;
import com.seiyaya.common.bean.ResultBean;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.exception.CommonException;
import com.seiyaya.common.utils.DateUtils;
import com.seiyaya.stock.service.CommonTradeService;
import com.seiyaya.stock.service.StockCacheService;
import com.seiyaya.stock.trade.service.RuleService;
import com.seiyaya.stock.trade.service.TradeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

	@Autowired
	private TradeService tradeService;

	@Autowired
	private CommonTradeService commonTradeService;

	@Autowired
	private StockCacheService stockCacheService;

	@Autowired
	private RuleService ruleService;
	
	@PostMapping("/add")
	public ResultBean addOrder(Order order) {
		checkCondition(commonTradeService.isSettlementTime() && DateUtils.isSettlementTime(), "系统清算期间不能进行交易操作");
		checkCondition(!order.validate(), "委托单参数不合法");
		if(StringUtils.isEmpty(order.getOrderType())) {
			order.setOrderType(Order.LIMITPRICE);
		}
		
		Stock stock = stockCacheService.getStockByKey(order.getMarketId() + order.getStockCode());
		checkCondition(stock == null, "未找到当天证券信息");

		//校验委托价格和参数添加
		order.setOrderPrice(checkOrderPrice(order, stock));
		order.setOrderBalance(order.getOrderQty() * order.getOrderPrice());
		order.setOrderDate(DateUtils.formatNowDate());
		order.setOrderTime(DateUtils.formatNowTime());
		order.setStockName(stock.getStockName());
		order.setTradeStatus("0");
		order.setDealFlag("0");
		order.setStockType(stock.getStockType());
		
		//手续费计算
		FreeRate freeRate = new FreeRate(order);
		order.setStapTax(freeRate.getStaptaxFree());
		order.setCommission(freeRate.getCommissoinFree());
		order.setTransferFare(freeRate.getTransferFree());
		order.setTotalFare(freeRate.getTotalFare());
		
		order.setTotalBalance(order.calcTotalBalance());

		String newVersion = UUID.randomUUID().toString();
		int serialNum = -1;
		if(order.isBuy()) {
			serialNum = dealBuyOrder(order, stock, newVersion);
		}else if(order.isSell()) {
			serialNum = dealSellOrder(order, newVersion);
		}
		//TODO:将完成的订单推送到rabbitMQ,用来解决多台撮合开启的问题
		
		//下委托
		if (serialNum < 0) {
			log.error("买卖委托失败:{}",order);
			throw new CommonException(3, "买卖委托失败!");
		}
		ResultBean resultBean = new ResultBean("下单成功");
		resultBean.setResults("orderId", serialNum);
		return resultBean;
	}

	/**
	 * 处理卖出委托
	 * @param order
	 * @param newVersion
	 * @return
	 */
	private int dealSellOrder(Order order, String newVersion) {
		HoldStock holdStock = tradeService.queryHoldStock(order.getAccountId(), order.getMarketId(), order.getStockCode());
		//持仓校验
		checkCondition(holdStock == null, "持仓不存在");
		checkCondition(holdStock.getCurrentQty() <= 0 || holdStock.getCurrentQty() < order.getOrderQty(), "可用股票数不足");
		int maxSellQty = holdStock.getCurrentQty();
		checkCondition(maxSellQty >= 100 && maxSellQty != order.getOrderQty() && order.getOrderQty() % 100 == 0, "非平仓卖出数量必须为100股整数倍");
		checkCondition(maxSellQty < 100 && order.getOrderQty() != maxSellQty, "最大可卖数小于100，应一次性全部卖出");
		tradeService.addSellOrder(order,holdStock,newVersion);
		return order.getOrderId();
	}

	/**
	 * 处理买入委托
	 * @param order
	 * @param stock
	 * @param newVersion
	 * @return
	 */
	private int dealBuyOrder(Order order, Stock stock, String newVersion) {
		checkCondition(!(order.getOrderQty() % 100 == 0), "买入数量必须是整数并且是100的整数倍");
		//买入规则校验
		ruleService.compareTradeRule(order, stock);
		Account account = tradeService.queryAccount(order.getAccountId());
		//交易账户校验
		checkCondition(order.getTotalBalance()> account.getCurrentBalance() || account.getCurrentBalance() <= 0, "可用资金不足");
		return tradeService.addBuyOrder(order,account,newVersion);
	}

	/**
	 * 校验委托价格
	 * @param order
	 * @param stock
	 * @return
	 */
	private double checkOrderPrice(Order order, Stock stock) {
		double orderPrice = order.getOrderPrice();
		String nowDate = DateUtils.formatNowDate(DateUtils.NO_PATTERN_DATE);
		if (stockCacheService.isTradeDate(nowDate) && DateUtils.isNotTradeTime()) {
			// 非交易时间下单
			double scale = 0.1;
			checkCondition(stock.getNowPrice() == 0, "行情取得的股票的现价为0,请检查行情服务器!");
			if (stock.getStockName() != null && stock.getStockName().contains("ST")) {
				scale = 0.05;
			}
			double upLimitPrice = (stock.getNowPrice() * (1 + scale));
			double downLimitPrice = (stock.getNowPrice() * (1 - scale));
			return checkUpDownPrice(order, orderPrice, upLimitPrice, downLimitPrice);
		} else {
			// 交易时间下单
			double upLimitPrice = stock.getLimitUp();
			double downLimitPrice = stock.getLimitDown();
			return checkUpDownPrice(order, orderPrice, upLimitPrice, downLimitPrice);
		}
	}

	/**
	 * 获取委托价格，市价委托获取涨跌停价格，现价委托检验合法性
	 * 
	 * @param order
	 * @param orderPrice
	 * @param upLimitPrice
	 * @param downLimitPrice
	 * @return
	 */
	private double checkUpDownPrice(Order order, double orderPrice, double upLimitPrice, double downLimitPrice) {
		if (order.isMarketPrice()) {
			if (order.isBuy()) {
				return upLimitPrice;
			}
			if (order.isSell()) {
				return downLimitPrice;
			}
		} else {
			checkCondition(orderPrice > upLimitPrice, "委托价格不能高于涨停价");
			checkCondition(orderPrice < downLimitPrice, "委托价格不能低于跌停价");
		}
		return orderPrice;
	}
	
	@GetMapping("/query/today")
	public ResultBean queryTodayOrder(Integer accountId,@RequestParam(defaultValue = "1")int pageIndex,@RequestParam(defaultValue = "8")int pageSize) {
		DBPage<Order> list = tradeService.queryTodayOrder(accountId,pageIndex,pageSize);
		
		list.getList().forEach((order) -> {
			String tradeTypeName = stockCacheService.getEnumValue("trade_type",order.getTradeType());
			order.setTradeTypeName(tradeTypeName);
			String tradeStatusName = stockCacheService.getEnumValue("trade_status", order.getTradeStatus());
			order.setTradeStatus(tradeStatusName);
		});
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("orders", list);
		return resultBean;
	}
	
	@GetMapping("/query/hist")
	public ResultBean queryHistOrder(Integer accountId,@RequestParam(defaultValue = "1")int pageIndex,@RequestParam(defaultValue = "8")int pageSize) {
		DBPage<Order> list = tradeService.queryHistOrder(accountId,pageIndex,pageSize);
		
		list.getList().forEach((order) -> {
			String tradeTypeName = stockCacheService.getEnumValue("trade_type",order.getTradeType());
			order.setTradeTypeName(tradeTypeName);
			String tradeStatusName = stockCacheService.getEnumValue("trade_status", order.getTradeStatus());
			order.setTradeStatus(tradeStatusName);
		});
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("orders", list);
		return resultBean;
	}
	
	@GetMapping("/query/cancel")
	public ResultBean queryCancelOrder(Integer accountId,@RequestParam(defaultValue = "1")int pageIndex,@RequestParam(defaultValue = "8")int pageSize) {
		DBPage<Order> list = tradeService.queryCancelOrder(accountId,pageIndex,pageSize);
		ResultBean resultBean = new ResultBean();
		resultBean.setResults("orders", list);
		return resultBean;
	}
	
	@PostMapping("/add/cancel")
	public ResultBean queryCancelOrder(Integer accountId,Integer orderId){
		Order order = tradeService.queryOrder(accountId,orderId);
		checkCondition(order == null, "当前查询的委托不存在");
		log.info("查询的委托信息:{}",order);
		
		ResultBean result = new ResultBean();
		if("2".equals(order.getTradeStatus())) {
			result.setMsg("委托已经成交，不能申请撤单");
			return result;
		}
		
		if("3".equals(order.getTradeStatus())) {
			result.setMsg("申请撤单已经提交，不能重复提交");
			return result;
		}
		
		if("4".equals(order.getTradeStatus())) {
			result.setMsg("订单已经撤单，不能重复撤单");
			return result;
		}
		
		if(!DateUtils.isNotTradeTime() || "1".equals(order.getTradeStatus())) {
			//交易时间内撤单
			tradeService.cancelOrder(accountId,orderId);
			result.setMsg("申请撤单成功").setResults("orderId",orderId);
			return result;
		}else {
			//非交易时间内撤单
			tradeService.outTimeCancelOrder(accountId,order);
			result.setMsg("撤单成功").setResults("orderId", orderId);
			return result;
		}
	}
}

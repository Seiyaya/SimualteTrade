package com.seiyaya.common.bean;

/**
 *  系统常用的数据字典常量
 * @author Seiyaya
 *
 */
public class EnumValue {
	
	/***************通用状态*********************/
	/**
	 * 通常表示消极的状态  关闭、不可用等
	 */
	public static final String STATUS_LOSS = "0";
	
	/**
	 * 通常表示积极的状态  开启、可用
	 */
	public static final String STATUS_SUCCESS = "1";
	
	/**************交易类型**********************/
	/**
	 * 普通买入
	 */
	public static final String TRADE_TYPE_0 = "0";
	
	/**
	 * 普通卖出
	 */
	public static final String TRADE_TYPE_1 = "1";
	
	/**
	 * 分红
	 */
	public static final String TRADE_TYPE_2 = "2";
	
	/**
	 * 送股
	 */
	public static final String TRADE_TYPE_3 = "3";
	
	/**************委托单状态*********************/
	
	/**
	 * 未报
	 */
	public static final String TRADE_STATUS_0 = "0";
	
	/**
	 * 已报
	 */
	public static final String TRADE_STATUS_1 = "1";
	
	/**
	 * 已成
	 */
	public static final String TRADE_STATUS_2 = "2";
	
	/**
	 * 申请撤单
	 */
	public static final String TRADE_STATUS_3 = "3";
	
	/**
	 * 已撤
	 */
	public static final String TRADE_STATUS_4 = "4";
	
	/**
	 * 废单
	 */
	public static final String TRADE_STATUS_5 = "5";
	
	/***************撤单状态********************/
	/**
	 * 申请撤单
	 */
	public static final String CANCEL_STATUS_0 = "0";
	
	/**
	 * 撤单成功
	 */
	public static final String CANCEL_STATUS_1 = "1";
	
	/**
	 *  盘中撤单，撮合已读
	 */
	public static final String CANCEL_STATUS_2 = "2";
	
	/***********处理状态***********************/
	/**
	 * 未处理
	 */
	public static final String DEAL_FLAG_0 = "0";
	
	/**
	 * 已处理
	 */
	public static final String DEAL_FLAG_1 = "1";
	
	/**
	 * 处理完成
	 */
	public static final String DEAL_FLAG_2 = "2";
}

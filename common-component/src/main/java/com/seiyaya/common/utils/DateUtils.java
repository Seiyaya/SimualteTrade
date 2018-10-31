package com.seiyaya.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * 日期时间相关工具类
 * 
 * @author Seiyaya
 *
 */
public class DateUtils {

	// 默认 16：00
	private static final int CLOSE_TIME = 960;

	// 调整开盘时间为9点20
	private static final int OPEN_BEFORE = 560;

	public static final String NO_PATTERN_DATE = "yyyyMMdd";

	public static final String PATTERN_DATE = "yyyy-MM-dd";

	public static final String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";

	public static String formatMilliSecond(long milliSeconds) {
		return formatMilliSecond(milliSeconds, PATTERN_TIME);
	}

	/**
	 * 格式化时间戳
	 * 
	 * @param milliSeconds
	 * @param format
	 * @return
	 */
	public static String formatMilliSecond(long milliSeconds, String format) {
		return new DateTime(milliSeconds).toString(format);
	}

	/**
	 * 格式化当前时间
	 * 
	 * @param format
	 * @return
	 */
	public static String formatNowDate(String format) {
		return DateTime.now().toString(format);
	}

	public static String formatNowDate() {
		return formatNowDate(NO_PATTERN_DATE);
	}

	/**
	 * 获取传递日期的前N天或者后N天
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static String getNDate(Date date, int day) {
		return new DateTime(date).plusDays(day).toString(PATTERN_DATE);
	}

	public static String getNDate(Date date, String format, int day) {
		return new DateTime(date).plusDays(day).toString(format);
	}

	public static String getNDate(String date, int day) {
		return getNDate(date, PATTERN_DATE, day);
	}

	public static String getNDate(String date, String format, int day) {
		return DateTime.parse(date, DateTimeFormat.forPattern(format)).plusDays(day).toString(format);
	}

	/**
	 * 是否是清算时间
	 * 
	 * @return
	 */
	public static boolean isSettlementTime() {
		Calendar now = Calendar.getInstance(Locale.CHINA);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int minutes = hour * 60 + minute;
		return ((minutes >= 900 && minutes <= 960));
	}

	/**
	 * 是否是交易时间
	 * @return
	 */
	public static boolean isNotTradeTime() {
		Calendar now = Calendar.getInstance(Locale.CHINA);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int minutes = hour * 60 + minute; //
		return minutes >= CLOSE_TIME || minutes < OPEN_BEFORE;
	}
}

package com.seiyaya.common.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 数字格式化相关
 * @author Seiyaya
 *
 */
public class NumberFormatUtils {
	
	/**
	 * 格式化数字
	 * @param number
	 * @param pattern
	 * @return
	 */
	public static double formatNumber(double number,String pattern) {
		 DecimalFormat df = new DecimalFormat(pattern);
		 df.setRoundingMode(RoundingMode.HALF_UP);
		 return Double.valueOf(df.format(number));
	}
	
	/**
	 * 格式化数字
	 * @param number
	 * @param pattern
	 * @return
	 */
	public static double formatNumber(double number) {
		return formatNumber(number,"0.00");
	}
}

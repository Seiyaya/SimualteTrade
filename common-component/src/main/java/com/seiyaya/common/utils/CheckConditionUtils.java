package com.seiyaya.common.utils;

import com.seiyaya.common.exception.CommonException;

/**
 * 对抛出异常进行简单的包装
 * @author Seiyaya
 *
 */
public class CheckConditionUtils {
	
	/**
     * 条件检验，简化异常抛出
     * 对于异常的情况
     * 返回值为1表示参数校验有误
     * 返回值为2表示检验条件不满足
     * 其他返回值可特别说明特别处理
     * @author 王佳
     * @created 2018年10月14日 上午1:17:06
     * @param condition
     * @param errorMsg
     */
    public static void checkCondition(boolean condition,String errorMsg) {
        checkCondition(condition,2, errorMsg);
    }
    
    /**
     * 条件检验，简化异常抛出
     * @author 王佳
     * @created 2018年10月14日 上午1:17:06
     * @param condition
     * @param errorMsg
     */
    public static void checkCondition(boolean condition,int errorNo,String errorMsg) {
        if(condition) {
            throw new CommonException(errorNo,errorMsg);
        }
    }
}

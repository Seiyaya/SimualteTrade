package com.seiyaya.common.utils;

import java.util.Collection;

/**
 * 集合相关工具类
 * @author Seiyaya
 *
 */
public class CollectionUtils {
	
	public static boolean isNotEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}
}

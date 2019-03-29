package com.seiyaya.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	/**
	  *  删除指定key 
	 * @author Seiyaya
	 * @date 2018年12月24日 上午11:29:03
	 * @param key
	 */
	public void del(String key) {
		redisTemplate.delete(key);
	}
	
	/**
	  *  删除指定key集合 
	 * @author Seiyaya
	 * @date 2018年12月24日 上午11:29:03
	 * @param key
	 */
	public void del(Collection<String> keys) {
		redisTemplate.delete(keys);
	}
	
	/**
	  * 是否存在key
	 * @author Seiyaya
	 * @date 2018年12月24日 上午11:29:03
	 * @param key
	 */
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	/**
	 * 添加键值
	 * @author Seiyaya
	 * @date 2018年12月24日 上午11:29:03
	 * @param key
	 */
	public RedisUtil set(String key,String value) {
		redisTemplate.opsForValue().set(key, value);
		return this;
	}
	
	/**
	 * 设置过期时间
	 * @author Seiyaya
	 * @date 2018年12月24日 上午11:29:03
	 * @param key
	 */
	public boolean expire(String key,long timeout,TimeUnit timeUnit) {
		return redisTemplate.expire(key, timeout, timeUnit);
	}
	
	/**
	 * 获取键值对
	 * @author Seiyaya
	 * @date 2018年12月24日 上午11:29:03
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> hValues(String key,Class<T> t) {
		return (List<T>)redisTemplate.opsForHash().values(key);
	}
}

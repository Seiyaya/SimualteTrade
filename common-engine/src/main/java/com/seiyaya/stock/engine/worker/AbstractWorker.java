package com.seiyaya.stock.engine.worker;

import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractWorker<T> implements Callable<T> {
	
	protected abstract T execute();
	
	public T call() {
		before();
		T t = execute();
		after();
		return t;
	}
	
	protected void before() {
		if(log.isDebugEnabled()) {
			log.debug("work before");
		}
	}
	
	protected void after() {
		if(log.isDebugEnabled()) {
			log.debug("work after");
		}
	}
}

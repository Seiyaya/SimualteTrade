package com.seiyaya.stock.engine.worker;

public abstract class AbstractWorker implements Runnable {
	
	protected abstract void execute();
	
	public void run() {
		before();
		execute();
		after();
	}
	
	protected void before() {
	}
	
	protected void after() {
		
	}
}

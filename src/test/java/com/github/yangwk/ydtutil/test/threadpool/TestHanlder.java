package com.github.yangwk.ydtutil.test.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHanlder implements Runnable{
	private static final Logger LOG = LoggerFactory.getLogger(TestHanlder.class);
	
	private long sleepTime;
	private ThreadPoolExecutor executor;
	private boolean canInterrupt;
	
	public TestHanlder(long sleepTime, ThreadPoolExecutor executor) {
		this(sleepTime, executor, false);
	}
	
	public TestHanlder(long sleepTime, ThreadPoolExecutor executor, boolean canInterrupt) {
		this.sleepTime = sleepTime;
		this.executor = executor;
		this.canInterrupt = canInterrupt;
	}

	@Override
	public void run() {
		LOG.info("{} , {}", Thread.currentThread().toString(), executor.toString());
		
		if(canInterrupt) {
			Thread.currentThread().interrupt();
		}
		
		if(sleepTime > 0) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

}

package com.github.yangwk.ydtutil.test.threadpool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yangwk.ydtutil.threadpool.BlockThreadPoolHolder;


public class TestThreadPool {
	
	private static Logger LOG = LoggerFactory.getLogger(TestThreadPool.class);

	public static void main(String[] args) {
        final int maximumPoolSize = 2;
        
        BlockThreadPoolHolder.init(maximumPoolSize, -1);
        
		final int executeCount = 100;
		final int oneSleepTime = 100;
		final boolean canInterrupt = false;
		final boolean useRandomSleepTime = true;
		
		List<Long> randomSleepTimes = new ArrayList<Long>();
		Random random = new Random();
		for(int r=0; r<executeCount; r++) {
			long time = oneSleepTime;
			if(useRandomSleepTime) {
				time = random.nextInt(oneSleepTime + 1);
			}
			randomSleepTimes.add(time);
		}
		
		int exOccurredCount = 0;
		long startTime = System.currentTimeMillis();
		for(int r=0; r<executeCount; r++) {
			try {
				BlockThreadPoolHolder.execute(new TestHanlder(randomSleepTimes.get(r), BlockThreadPoolHolder.getExecutor(), canInterrupt));
			}catch (RejectedExecutionException e) {
				exOccurredCount ++;
				LOG.error(e.getMessage(), e);
			}
		}
		long endTime = System.currentTimeMillis();
		
		
		try {
			System.out.println("press Enter to terminate");
			System.in.read();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}finally{
			LOG.info(BlockThreadPoolHolder.getExecutor().toString());
			
			BlockThreadPoolHolder.close();
			LOG.info("submit task cost time " + (endTime - startTime) + " ms");
			LOG.info("exception occurred count " + exOccurredCount);
			LOG.info(
					String.format("test info -> maximumPoolSize:%d executeCount:%d oneSleepTime:%d", 
							maximumPoolSize, executeCount, oneSleepTime)
					);
			
			System.out.println("terminated");
		}
	}
}

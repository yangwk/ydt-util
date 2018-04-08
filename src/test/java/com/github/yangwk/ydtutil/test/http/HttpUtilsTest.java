package com.github.yangwk.ydtutil.test.http;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yangwk.ydtutil.http.HttpUtils;


public class HttpUtilsTest {
	static Logger LOG = LoggerFactory.getLogger(HttpUtilsTest.class);

	static void test(final String url, final long interval) {
		Thread[] threads = new Thread[1];
		
		final CountDownLatch latch = new CountDownLatch(threads.length);
		
		long startTime = System.currentTimeMillis();
		for(int r=0; r < threads.length; r ++) {
			Thread thr = threads[r];
			thr = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						HttpUtils.get(url, null);

						if(interval > 0) {
							try {
								Thread.sleep(interval);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}finally {
						latch.countDown();
					}
				}
			});
			thr.start();
		}
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println("cost time :" + (endTime - startTime));
	}
	
	
	static void testDefaultHttpRequestRetryHandler() {
		DefaultHttpRequestRetryHandler h = DefaultHttpRequestRetryHandler.INSTANCE;
		IOException exception = new org.apache.http.NoHttpResponseException("test");
		boolean retry = h.retryRequest(exception, 1, new BasicHttpContext());
		
		System.out.println(retry);
	}
	
	
	static void testPressure() {
		final int threads = 10;
		final int interval = 3;
		final String url = "http://localhost/json";
		
		LOG.info("threads {}", threads);
		LOG.info("interval {}", interval);
		LOG.info("url {}", url);
		
		Thread[] threadArr = new Thread[threads];
		for(int r=0; r<threadArr.length; r++) {
			Thread th = threadArr[r];
			th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true) {
						try {
							HttpUtils.postJson(url, "", null);
							
							Thread.sleep(interval);
						}catch(Exception e) {
							LOG.error(e.getMessage(), e);
						}
					}
				}
			}, "test-post"+r);
			th.start();
		}
		
		try {
			System.in.read();
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {
//		test("http://localhost/http-fs/", 0);
		
//		testDefaultHttpRequestRetryHandler();
		
		testPressure();
	}
}

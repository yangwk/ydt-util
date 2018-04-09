package com.github.yangwk.ydtutil.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.github.yangwk.ydtutil.threadpool.component.BlockRejectedExecutionHandler;

/**
 * 可阻塞的线程池持有者。更适用于short-lived场景
 * @author yangwk
 *
 */
public final class BlockThreadPoolHolder {
	private BlockThreadPoolHolder() {
	}

	private static ThreadPoolExecutor executor = null;
	
	private static ScheduledExecutorService scheduledExecutor = null;

	/**
	 * 初始化，必须先调用
	 * @author yangwk
	 * @param executorMaximumPoolSize 并发线程池大小的最大值
	 * @param scheduleThreads 延时调度线程池的大小，大于0则创建ScheduledExecutorService
	 */
	public synchronized static void init(final int executorMaximumPoolSize, final int scheduleThreads) {
		if (executor == null) {
			
			BlockingQueue<Runnable> workQueue = new SynchronousQueue<Runnable>();
			
			RejectedExecutionHandler handler = new BlockRejectedExecutionHandler();

			//线程池不存在核心线程，实际线程按需新增
			executor = new ThreadPoolExecutor(0, executorMaximumPoolSize, 60L, TimeUnit.SECONDS, workQueue, handler);
		}
		
		if(scheduledExecutor == null) {
			if(scheduleThreads > 0) {
				scheduledExecutor = Executors.newScheduledThreadPool(scheduleThreads);
			}
		}
	}

	public static void execute(Runnable command) {
		executor.execute(command);
	}
	
	
	/**
	 * Creates and executes a one-shot action that becomes enabled
     * after the given delay.
	 */
	public static void schedule(Runnable command, long delay, TimeUnit unit) {
		scheduledExecutor.schedule(command, delay, unit);
	}
	
	
	private static void close(ExecutorService service) {
		service.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!service.awaitTermination(2, TimeUnit.SECONDS)) {
				service.shutdownNow(); // Cancel currently executing tasks
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			service.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
	
	
	/**
	 * 关闭线程池
	 * @author yangwk
	 */
	public static void close() {
		if (executor != null) {
			close(executor);
		}
		
		if(scheduledExecutor != null) {
			close(scheduledExecutor);
		}
	}

	
	/**
	 * 仅限监控和调试目的而才使用该方法，外部不应该对线程池进行任何操作
	 * @author yangwk
	 * @return 并发线程池
	 */
	public static ThreadPoolExecutor getExecutor(){
		return executor;
	}
	
}

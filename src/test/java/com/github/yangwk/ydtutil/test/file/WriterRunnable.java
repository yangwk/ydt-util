package com.github.yangwk.ydtutil.test.file;

import java.io.IOException;
import java.util.Random;

import com.github.yangwk.ydtutil.file.FileSynOperator;


public class WriterRunnable implements Runnable{

	private volatile boolean stopped = false;
	
	private Random random = new Random();
	private int intervals;
	private boolean append;
	private FileSynOperator operator;
	
	public WriterRunnable(FileSynOperator operator, boolean append, int intervals){
		this.intervals = intervals;
		this.append = append;
		this.operator = operator;
	}
	
	@Override
	public void run() {
		while(! stopped){
			String s = Thread.currentThread().getName()+"\r\n";
			byte[] content = s.getBytes();
			try {
				operator.write(content, append);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			if(intervals > 0){
				int sl = random.nextInt(intervals);
				try {
					Thread.sleep(sl);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void stop(){
		this.stopped = true;
	}

}

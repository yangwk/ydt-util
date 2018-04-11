package com.github.yangwk.ydtutil.test.file;

import java.io.IOException;
import java.util.Random;

import com.github.yangwk.ydtutil.file.FileSynOperator;


public class ReaderRunnable implements Runnable{

	private volatile boolean stopped = false;
	
	private Random random = new Random();
	private int intervals;
	
	private FileSynOperator operator;
	
	public ReaderRunnable(FileSynOperator operator, int intervals){
		this.intervals = intervals;
		this.operator = operator;
	}
	
	@Override
	public void run() {
		while(! stopped){
			byte[] content = null;
			try {
				content = operator.read();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println(new String(content));
			
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

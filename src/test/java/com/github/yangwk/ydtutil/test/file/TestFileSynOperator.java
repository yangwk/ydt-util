package com.github.yangwk.ydtutil.test.file;

import java.io.IOException;

import com.github.yangwk.ydtutil.file.FileSynOperator;


public class TestFileSynOperator {

	public static void main(String[] args) {
		final String pathname = "D:/yangwk/tmp/test.txt";
		FileSynOperator operator = null;
		try {
			operator = new FileSynOperator(pathname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		final int threads = 3;
		final int type = 3;
		int intervals = 1000;
		boolean append = true;
		
		WriterRunnable[] writers = null;
		if(type == 1 || type >= 3){
			System.out.println("new writer threads");
			writers = new WriterRunnable[threads];
			for(int r=0; r < writers.length; r++){
				writers[r] = new WriterRunnable(operator, append, intervals);
				new Thread(writers[r]).start();
			}
		}
		
		ReaderRunnable[] readers = null;
		if(type == 2 || type >= 3){
			System.out.println("new reader threads");
			readers = new ReaderRunnable[threads];
			for(int r=0; r < readers.length; r++){
				readers[r] = new ReaderRunnable(operator, intervals);
				new Thread(readers[r]).start();
			}
		}
		
		
		
		try {
			System.out.println("press Enter to stop");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writers != null){
				for(WriterRunnable r : writers){
					r.stop();
				}
			}
			if(readers != null){
				for(ReaderRunnable r : readers){
					r.stop();
				}
			}
			
			System.out.println("stopped");
		}
	}
}

package com.github.yangwk.ydtutil.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.commons.io.IOUtils;

/**
 * 进程、线程安全同步的文件操作
 * 
 * @author yangwk
 */
public class FileSynOperator {
	private File file;

	/**
	 * @param pathname
	 *            文件路径
	 * @throws FileNotFoundException
	 */
	public FileSynOperator(String pathname) throws IOException {
		this.file = new File(pathname);
	}

	/**
	 * 进程、线程安全地写
	 * 
	 * @author yangwk
	 * @param content
	 *            写入的内容
	 * @param append
	 *            如果为 true，则写入文件末尾处，而不是写入文件开始处
	 */
	public synchronized void write(byte[] content, boolean append) throws IOException{
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(file, "rwd");
			FileChannel channel = accessFile.getChannel();
			FileLock lock = channel.lock(0L, Long.MAX_VALUE, false);
			try {
				channel.position(0L);
				
				if (!append) {
					channel.truncate(0L); // clear file
				} else {
					channel.position(channel.size());
				}
				ByteBuffer buffer = ByteBuffer.allocate(content.length);
				buffer.put(content);
				buffer.rewind();
	
				channel.write(buffer);
	
				buffer = null;
			} finally {
				lock.release();
			}
		}finally {
			IOUtils.closeQuietly(accessFile);
		}
	}

	/**
	 * 进程、线程安全地读
	 * 
	 * @author yangwk
	 */
	public synchronized byte[] read() throws IOException{
		byte[] content = null;
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(file, "r");
			FileChannel channel = accessFile.getChannel();
			// shared lock
			FileLock lock = channel.lock(0L, Long.MAX_VALUE, true);
			try {
				channel.position(0L);
				
				final long size = channel.size();
				// 文件长度可为Long，一次性读取最多能读Integer最大值
				if (size > Integer.MAX_VALUE) {
					throw new IOException(file.getAbsolutePath() + " size is greater than " + Integer.MAX_VALUE);
				}
				ByteBuffer buffer = ByteBuffer.allocate((int) size);
	
				channel.read(buffer);
	
				content = buffer.array();
	
				buffer = null;
			} finally {
				lock.release();
			}
		}finally {
			IOUtils.closeQuietly(accessFile);
		}

		return content;
	}

}

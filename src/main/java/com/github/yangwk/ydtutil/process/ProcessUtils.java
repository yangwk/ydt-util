package com.github.yangwk.ydtutil.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

/**
 * 进程工具类
 * @author yangwk
 *
 */
public class ProcessUtils {
	
	/**
	 * 执行进程
	 * <br>执行进程后，将等待进程终止。如果进程等待输入，将可能不会终止
	 * @author yangwk
	 *
	 * @param command 执行的命令
	 * @param redirectErrorStream 是否合并输出到标准io
	 * @param outLimit 输出限制
	 * @return
	 */
	public static String executeProcess(boolean redirectErrorStream, Integer outLimit,String... command){
		StringBuilder resultBuffer = new StringBuilder();
		InputStream in = null;
		InputStreamReader streamReader = null;
		BufferedReader reader = null;
		try{
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			processBuilder = processBuilder.redirectErrorStream(redirectErrorStream);	//是否合并到标准io
			Process process = processBuilder.start();	//启动进程
			
			in = process.getInputStream();
			streamReader = new InputStreamReader(in);
			reader = new BufferedReader(streamReader);
			
			String line = null;
			while((line = reader.readLine()) != null){
				//超出限制不保存
				if(outLimit != null && resultBuffer.length() <= outLimit){
					resultBuffer.append(line);	//保存结果
				}
			}
			process.waitFor();
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(reader, streamReader, in);
		}
		return resultBuffer.toString();
	}

}

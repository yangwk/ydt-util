package com.github.yangwk.ydtutil.web.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * 上传
 * @author yangwk
 *
 */
public class Upload {
	/**
	 * 分块传输
	 */
	public static final int TRANSFER_CHUNK = 0;
	/**
	 * 单个文件一次性传输
	 */
	public static final int TRANSFER_SINGLE = 1;
	
	private Chunk chunk;
	private File file;	//保存的文件
	private boolean isStarted = false;	//标记是否已开始
	
	/**
	 * 
	 * @param chunk
	 * @param absolutePath 保存文件绝对路径
	 */
	public Upload(Chunk chunk,String absolutePath){
		this.chunk = chunk;
		this.file = new File(absolutePath);
		if(this.file.getParentFile() != null && ! this.file.getParentFile().exists()) {
			this.file.getParentFile().mkdirs();
		}
	}
	
	/**
	 * 获取传输方式
	 */
	private int getTransfer(){
		//没有分块数据
		if( getInt(chunk.getChunk(), -1) < 0 || getInt(chunk.getChunks(), -1) < 0 ){
			return TRANSFER_SINGLE;
		}
		return TRANSFER_CHUNK;
	}
	
	/**
	 * 开始上传
	 * @author yangwk
	 *
	 * @param chunk
	 * @param input 可以使用MultipartFile.getInputStream()等方法获取
	 * @return the number of bytes uploaded
	 */
	public long start(InputStream input){
		return saveFile(input);
	}
	
	/**
	 * 判断是否上传完毕
	 * @author yangwk
	 * @return
	 */
	public boolean isFinished(){
		return isStarted && isLastChunk();
	}

	/**
	 * 保存文件
	 */
	private long saveFile(InputStream input){
		long count = 0;
		FileOutputStream output = null;
		File tempFile = null;
		Boolean error = Boolean.FALSE;
		try{
			tempFile = getTempFile();
			output = FileUtils.openOutputStream(tempFile,true);	//append
			count = IOUtils.copy(input, output);
			
			isStarted = true;	
		}catch(Exception e){
			error = Boolean.TRUE;
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(input,output);
			if(error){
				if(tempFile != null && tempFile.isFile()){
					boolean deleted = tempFile.delete();
					if(! deleted){
						throw new RuntimeException("upload savefile error , can't delete created file : "+tempFile.getAbsolutePath());
					}
				}
			}else{
				if(isLastChunk()){
					boolean ok = tempFile.renameTo(file);	//重命名为保存的文件
					if(! ok){
						throw new RuntimeException(tempFile.getAbsolutePath() + "can't renameTo " + file.getAbsolutePath());
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * 获取临时文件
	 */
	private File getTempFile() throws IOException{
		//临时文件与保存文件同父路径
		File tempFile = new File( file.getParentFile().getAbsolutePath() + File.separator + tempFileName() );
		tempFile.createNewFile();	//如果存在，不会创建文件
		return tempFile;
	}

	/**
	 * 临时文件名，用chunk的uuid命名
	 */
	private String tempFileName(){
		return chunk.getUuid()+"_chunk";
	}

	private int getInt(Integer value,int nullValue){
		return (value == null) ? nullValue : value.intValue();
	}
	
	/**
	 * 判断是否为最后一块(单个文件一次性传输也属于最后一块)
	 */
	private boolean isLastChunk(){
		if(getTransfer() == TRANSFER_CHUNK){	//分块上传
			int cks = getInt( chunk.getChunks(), 1);
			int ck = getInt( chunk.getChunk(), 0);
			return (cks - ck) == 1;
		}
		return true;
	}
	
}

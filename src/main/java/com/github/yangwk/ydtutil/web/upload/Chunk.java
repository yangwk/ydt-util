package com.github.yangwk.ydtutil.web.upload;

/**
 * 分块
 * @author yangwk
 *
 */
public class Chunk {
	
	private String name;
	private Integer chunk;
	private Integer chunks;
	private String uuid;
	
	/**
	 * @see #Chunk(String, Integer, Integer, String)
	 */
	public Chunk(String name,String uuid) {
		this(name, null, null, uuid);
	}
	
	/**
	 * 
	 * @param name 文件名称
	 * @param chunk 当前块，从0开始
	 * @param chunks 块总数
	 * @param uuid 文件唯一识别码
	 */
	public Chunk(String name, Integer chunk, Integer chunks, String uuid) {
		super();
		if(chunk < 0 || chunk >= chunks){
			throw new IllegalArgumentException(chunk + ">=" +chunks);
		}
		this.name = name;
		this.chunk = chunk;
		this.chunks = chunks;
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getChunk() {
		return chunk;
	}
	public void setChunk(Integer chunk) {
		this.chunk = chunk;
	}
	public Integer getChunks() {
		return chunks;
	}
	public void setChunks(Integer chunks) {
		this.chunks = chunks;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}

package com.github.yangwk.ydtutil.security;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.security.KeyPair;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 * 密钥对 工具类
 * @author yangwk
 *
 */
public class KeyPairUtils {

	private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
	private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";

	private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
	private static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";

	/**
	 * 将密钥对保存到文件
	 * 
	 * @author yangwk
	 *
	 * @param keyPair
	 *            密钥对
	 * @param privateKeyPath
	 *            私钥存放路径，绝对路径
	 * @param publicKayPath
	 *            公钥存放路径，绝对路径
	 */
	public static void saveToFile(KeyPair keyPair, String privateKeyPath, String publicKayPath) {
		File privateKeyFile = new File(privateKeyPath);
		File publicKayFile = new File(publicKayPath);
		{
			File priParent = privateKeyFile.getParentFile();
			if (priParent != null) {
				priParent.mkdirs();
			}
		}
		{
			File pubParent = publicKayFile.getParentFile();
			if (pubParent != null) {
				pubParent.mkdirs();
			}
		}
		String base64privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
		String base64publicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
		write("pri", privateKeyFile, base64privateKey);
		write("pub", publicKayFile, base64publicKey);
	}

	/**
	 * 输出模板
	 */
	private static void write(String keyMode, File file, String content) {
		Writer writer = null;
		BufferedWriter bufWriter = null;
		try {
			writer = new FileWriter(file, false);
			bufWriter = new BufferedWriter(writer);
			int chunkSize = 64;	//64字符一行
			char[] data = content.toCharArray();
			
			bufWriter.write(getBegin(keyMode));
			bufWriter.newLine();	//换行
			
			int bytes = data.length;
			int dstOffset = 0;
			while (bytes > 0) {
				int chunk = Math.min(bytes, chunkSize);
				bufWriter.write(data, dstOffset, chunk);
				bufWriter.newLine();	//换行
				bytes -= chunk;
				dstOffset += chunk;
			}
			bufWriter.write(getEnd(keyMode));
			
			bufWriter.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(bufWriter,writer);
		}
	}
	
	private static String getBegin(String keyMode){
		if(keyMode.equals("pri")){
			return BEGIN_PRIVATE_KEY;
		}else if(keyMode.equals("pub")){
			return BEGIN_PUBLIC_KEY;
		}
		return "";
	}
	
	private static String getEnd(String keyMode){
		if(keyMode.equals("pri")){
			return END_PRIVATE_KEY;
		}else if(keyMode.equals("pub")){
			return END_PUBLIC_KEY;
		}
		return "";
	}

}

package com.github.yangwk.ydtutil.security;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.github.yangwk.ydtutil.security.SecurityUtils;

public class AESUtilsTest {
	
	@Test
	public void testEncrypt() throws Exception{
		String data = "一二三四五六七八九";
		String key = "1234567890123456";
		
		byte[] result = SecurityUtils.encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"), SecurityUtils.ALGORITHM_AES);
		
		String hexStr = Hex.encodeHexString(result);
		
		System.out.println(hexStr);	//a77482f29b98387dae95925f5b5026280fa5b1265b590ebf82fe493943b28471

	}
	
	@Test
	public void  testDecrypt() throws Exception {
		String src = "a77482f29b98387dae95925f5b5026280fa5b1265b590ebf82fe493943b28471";
		byte[] data = Hex.decodeHex(src.toCharArray());
		String key = "1234567890123456";
		
		byte[] result = SecurityUtils.decrypt(data, key.getBytes("UTF-8"), SecurityUtils.ALGORITHM_AES);
		
		String s = new String(result, "UTF-8");
		
		System.out.println(s);	//一二三四五六七八九

	}
}

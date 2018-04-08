package com.github.yangwk.ydtutil.test.security;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.github.yangwk.ydtutil.security.SecurityUtils;

public class DesUtilsTest {

	@Test
	public void testEncrypt() throws Exception{
		String data = "abcdefg";
		String key = "12345678";
		
		byte[] result = SecurityUtils.encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"), SecurityUtils.ALGORITHM_DES);
		
		String hexStr = Hex.encodeHexString(result);
		
		System.out.println(hexStr);	//486cb8b81caccdb7

	}
	
	@Test
	public void  testDecrypt() throws Exception {
		String src = "486cb8b81caccdb7";
		byte[] data = Hex.decodeHex(src.toCharArray());
		String key = "12345678";
		
		byte[] result = SecurityUtils.decrypt(data, key.getBytes("UTF-8"), SecurityUtils.ALGORITHM_DES);
		
		String s = new String(result, "UTF-8");
		
		System.out.println(s);	//abcdefg
	}
}

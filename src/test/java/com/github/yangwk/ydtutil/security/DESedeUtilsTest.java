package com.github.yangwk.ydtutil.security;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.github.yangwk.ydtutil.security.SecurityUtils;

public class DESedeUtilsTest {
	
	@Test
	public void testEncrypt() throws Exception{
		String data = "一二三四五六七八九";
		String key = "123456789012345678901234";
		
		byte[] result = SecurityUtils.encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"), SecurityUtils.ALGORITHM_DESEDE);
		
		String hexStr = Hex.encodeHexString(result);
		
		System.out.println(hexStr);	//517cd8b7a27c7936851ea97571d42e524abb10d40c6498d46598243f9d8fcdd6

	}
	
	@Test
	public void  testDecrypt() throws Exception {
		String src = "517cd8b7a27c7936851ea97571d42e524abb10d40c6498d46598243f9d8fcdd6";
		byte[] data = Hex.decodeHex(src.toCharArray());
		String key = "123456789012345678901234";
		
		byte[] result = SecurityUtils.decrypt(data, key.getBytes("UTF-8"), SecurityUtils.ALGORITHM_DESEDE);
		
		String s = new String(result, "UTF-8");
		
		System.out.println(s);	//一二三四五六七八九

	}
}

package com.github.yangwk.ydtutil.barcode;

import java.io.File;

import org.junit.Test;

import com.github.yangwk.ydtutil.barcode.QrcodeUtils;

public class QrcodeUtilsTest {
	
	@Test
	public void testDecode(){
		File file = new File("f:/test/qr-encode-4.png");
		String result = QrcodeUtils.decode(file);
		System.out.println(result);
	}
	
	@Test
	public void testEncode(){
		StringBuilder sb = new StringBuilder();
		sb.append("阿里记得佛iajflja阿拉丁回家哦iaua阿娇的哦好iua阿娇噢电话iu阿娇的哦好iua");
		String contents = sb.toString();
		int width = 240;
		String inner = "f:/test/logo.png";
		inner = null;
		String path = "f:/test/qr-encode-4.png";
		QrcodeUtils.encode(contents, width,inner, path);
	}

}

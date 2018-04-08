package com.github.yangwk.ydtutil.test.http;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.github.yangwk.ydtutil.http.HttpUtils;

public class HttpUtilsTest {

	@Test
	public void test_postJson(){
		String url = "http://localhost:5151";
		url = "http://120.24.74.199:9001/eher/api/DoOrganization/login";
		Godness godness = new Godness("beauty", 23, 23, 23f);
		
		String body = JSONObject.toJSONString(godness);
		body = "{\"version\":\"0.0.1\",\"sessionId\":\"\",\"sys_type\":\"mobile\",\"account\":\"\",\"device\":\"1111111\",\"data\":{\"username\":\"13999999999\",\"password\":\"123456\",\"deviceType\":\"mobile\"}}";
		String result = HttpUtils.postJson(url, body);
		
		System.out.println(result);
	}

	@Test
	public void test_get(){
		String url = "https://www.baidu.com/s?ie=UTF-8&wd=%E7%88%B1%E6%83%85";
		
		String result = HttpUtils.get(url,null);
		
		System.out.println(result);
	}

	@Test
	public void test_getFile(){
		String url = "http://oimagec7.ydstatic.com/image?id=-7868013978043558082&product=adpublish";
		String absolutePath = "f:/test/youdao-ad2.jpg";
		HttpUtils.getFile(url, null, absolutePath);
	}
}

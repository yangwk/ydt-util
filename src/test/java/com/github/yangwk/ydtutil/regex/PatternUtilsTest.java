package com.github.yangwk.ydtutil.regex;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PatternUtilsTest {
	
	
	@Test
	public void testIndexOf(){
		List<String> names = new ArrayList<String>();
		for(int c=0; c<400; c++){
			names.add("我|爱:你");
			names.add("你)爱!我.吗\\s");
			names.add("哈(哈|");
			names.add("e^谁?说=的$呢{s");
		}
		System.out.println(names.size());
		
		StringBuilder ssssss = new StringBuilder();
		for(int c=0; c<400; c++){
			ssssss.append("start$我|爱:你$你)爱!我.吗\\s$哈(哈|$e^谁?说=的$呢{s$end");
		}
		String cotent = ssssss.toString();
		System.out.println(cotent.length());
		
		Object[] matched = PatternUtils.indexOf(cotent, names);
		if(matched != null){
			System.out.println(matched[0]);
			System.out.println(matched[1]);
		}
	}
	
	/**
	 * 匹配到末尾
	 * @author yangwk
	 */
	@Test
	public void testIndexOfEnd(){
		List<String> names = new ArrayList<String>();
		for(int c=0; c<400; c++){
			names.add("abdcd");
			names.add("哈(哈|");
			names.add("e^谁?说=的$呢{s");
		}
		names.add("我|爱:你");
		System.out.println(names.size());
		
		StringBuilder ssssss = new StringBuilder();
		for(int c=0; c<400; c++){
			ssssss.append("start$现在要测试了$end");
		}
		ssssss.append("我|爱:你");
		String cotent = ssssss.toString();
		System.out.println(cotent.length());
		
		Object[] matched = PatternUtils.indexOf(cotent, names);
		if(matched != null){
			System.out.println(matched[0]);
			System.out.println(matched[1]);
		}
	}


	/**
	 * 匹配到末尾，与String.indexOf()比较
	 * @author yangwk
	 */
	@Test
	public void testIndexOfCompare(){
		List<String> names = new ArrayList<String>();
		for(int c=0; c<900; c++){
			names.add("abdcd");
			names.add("哈(哈|");
			names.add("e^谁?说=的$呢{s");
		}
		names.add("我|爱:你");
		System.out.println(names.size());
		
		StringBuilder ssssss = new StringBuilder();
		for(int c=0; c<900; c++){
			ssssss.append("start$现在要测试了$end");
		}
		ssssss.append("我|爱:你");
		String cotent = ssssss.toString();
		System.out.println(cotent.length());
		
		//正则开始
		{
			long startTime = System.currentTimeMillis();
			Object[] matched = PatternUtils.indexOf(cotent, names);
			long endTime = System.currentTimeMillis();
			if(matched != null){
				System.out.println("正则匹配结果");
				System.out.println(matched[0]);
				System.out.println(matched[1]);
				System.out.println("正则所需要时间"+(endTime-startTime));
			}
		}
		
		//String.indexOf()开始
		{
			long startTime = System.currentTimeMillis();
			int idx = -1;
			String matched = null;
			for(String s : names){
				idx = cotent.indexOf(s);
				if(idx != -1){
					matched = s;
					break;
				}
			}
			long endTime = System.currentTimeMillis();
			if(idx != -1){
				System.out.println("String.indexOf()匹配结果");
				System.out.println(idx);
				System.out.println(matched);
				System.out.println("String.indexOf()所需要时间"+(endTime-startTime));
			}
		}
	}

	
	@Test
	public void testEscapeKeywords(){
		String s = PatternUtils.escapeKeywords(null, ":!=\\^$*+?{}.()[]|-");
		System.out.println(s);
	}
	

}

package com.github.yangwk.ydtutil.test.reflect;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import com.github.yangwk.ydtutil.reflect.Bean2Utils;

public class Bean2UtilsTest {
	
	public Son getBean() {
		Son son = new Son();
		son.setId("123456");
		son.setLastName("boy");
		son.setName("great boy");
		son.setType(2);
		
		return son;
	}
	
	@Test
	public void testMine() {
		Son son = getBean();
		
		try {
			long start = System.currentTimeMillis();
			Map<String, Object> map = Bean2Utils.describe(son);
			long end = System.currentTimeMillis();
			System.out.println("cost time : " + (end - start) );
			
			for(Entry<String, Object> entry : map.entrySet()){
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
				if(entry.getValue() != null){
					System.out.println("value class is :");
					System.out.println(entry.getValue().getClass());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCommons() {
		Son son = getBean();
		
		try {
			long start = System.currentTimeMillis();
			Map<String, Object> map = PropertyUtils.describe(son);
			long end = System.currentTimeMillis();
			System.out.println("cost time : " + (end - start) );
			
			for(Entry<String, Object> entry : map.entrySet()){
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
				if(entry.getValue() != null){
					System.out.println("value class is :");
					System.out.println(entry.getValue().getClass());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

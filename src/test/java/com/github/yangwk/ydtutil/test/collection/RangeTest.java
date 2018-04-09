package com.github.yangwk.ydtutil.test.collection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Test;

import com.github.yangwk.ydtutil.Eq;
import com.github.yangwk.ydtutil.collection.CollUtils;
import com.github.yangwk.ydtutil.collection.Range;

/**
 * 
 * 开始 中间 结尾
连续 不连续

开始连 中间连 结尾连
x x y y z z
x x y y x x

开始连 中间连 结尾不
x x y y z x
x x y y x y

开始连 中间不 结尾连
x x y z x x
x x z x y y

开始连 中间不 结尾不
x x y z x y
x x y x

开始不 中间连 结尾连
x y y y x x 
x y z z y y

开始不 中间连 结尾不
x y y x z
x y y y x

开始不 中间不 结尾不
x y x y x
x y z y x

开始不 中间不 结尾连
x y x x x
x y x z z 
 * 
 */
public class RangeTest {
	final String x = "x", y = "y", z = "z";
	
	//开始连 中间连 结尾连
	final String[] datas_1 = { x, x, y, y, z, z };
	final String[] datas_1_1 = { x };
	final String[] datas_1_2 = { x, x};
	final String[] datas_2 = { x, x, y, y, x, x };
	
	//开始连 中间连 结尾不
	final String[] datas_3 = { x, x, y, y, z, x };
	final String[] datas_4 = { x, x, y, y, x, y };
	
	//开始连 中间不 结尾连
	final String[] datas_5 = { x, x, y, z, x, x };
	final String[] datas_6 = { x, x, z, x, y, y };
	
	//开始连 中间不 结尾不
	final String[] datas_7 = { x, x, y, z, x, y};
	final String[] datas_8 = { x, x, y, x };
	
	//开始不 中间连 结尾连
	final String[] datas_9 = { x, y, y, y, x, x };
	final String[] datas_10 ={ x, y, z, z, y, y };
	
	//开始不 中间连 结尾不
	final String[] datas_11 ={ x, y, y, x, z };
	final String[] datas_12 ={ x, y, y, y, x };
	
	//开始不 中间不 结尾不
	final String[] datas_13 ={ x, y, x, y, x };
	final String[] datas_14 ={ x, y, z, y, x };
	
	//开始不 中间不 结尾连
	final String[] datas_15 ={ x, y, x, x, x };
	final String[] datas_16 ={ x, y, x, z, z };
	
	@Test
	public void test() {
		List<String> datas = new ArrayList<String>();
		for(String s : datas_11){
			datas.add(s);
		}
		
		List<Range> ranges = CollUtils.range(datas, new Eq<String, String>() {
			@Override
			public boolean equals(String one, String another) {
				return one == null ? one == another : one.equals(another);
			}
		});
		for(Range range : ranges){
			System.out.println(ReflectionToStringBuilder.toString(range));
		}
	}
}

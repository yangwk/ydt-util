package com.github.yangwk.ydtutil.test.prop;

import com.github.yangwk.ydtutil.prop.PropUtils;

public class PropUtilsTest {

	public static void main(String[] args) {
		final String f1 = "a.txt";
		final String f2 = "b.txt";
		PropUtils prop1 = PropUtils.use(f1);
		System.out.println(prop1.toString());
		
		PropUtils prop2 = PropUtils.use(f2);
		System.out.println(prop2.toString());
		
		System.out.println(prop1 == PropUtils.use(f1));
		System.out.println(prop2 == PropUtils.use(f2));
		
		System.out.println("test method");
		
		System.out.println(prop1.getBool("yes"));
		System.out.println(prop2.getBool("yes"));
	}

}

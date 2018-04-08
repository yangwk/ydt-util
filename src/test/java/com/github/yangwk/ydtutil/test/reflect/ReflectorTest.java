package com.github.yangwk.ydtutil.test.reflect;

import org.junit.Test;

import com.github.yangwk.ydtutil.reflect.Reflector;

public class ReflectorTest {

	@Test
	public void testMethod() {
		Reflector<Son> refl = new Reflector<Son>(Son.class);

		System.out.println( refl.getField("id") );
		
		System.out.println( refl.getSetter("id") );
		System.out.println( refl.getGetter("id") );
	}

	@Test
	public void testInvoke() {
		Reflector<Son> refl = new Reflector<Son>(Son.class);
		
		Son son = refl.invokeConstructor();
		
		son.setId("1");
		
		Object id = refl.invokeMethod( refl.getGetter("id") , son);
		
		System.out.println(id);
		
		refl.invokeMethod( refl.getSetter("id") , son, "2");
		
		System.out.println(son.getId());
	}
	
}

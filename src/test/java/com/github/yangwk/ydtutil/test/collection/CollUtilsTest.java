package com.github.yangwk.ydtutil.test.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.github.yangwk.ydtutil.collection.CollUtils;
import com.github.yangwk.ydtutil.collection.Eq;
import com.github.yangwk.ydtutil.collection.SortHelper;

public class CollUtilsTest {
	
	@Test
	public void fullDifferenceTest(){
		List<String> a = Arrays.asList(new String[]{
				"1", "2", "3", "4", "5", "6", "7", "8", "8", "9"
		});
		List<String> b = Arrays.asList(new String[]{
				"1", "3", "5", "7", "8", "9", "11", "13", "13"
		});
	
		Collection<String>[] result = CollUtils.fullDifference(a, b);
		
		for(Collection<String> coll : result){
			System.out.println(coll);
		}
		
	}
	
	void printArray(Object[] vs){
		SortHelper sh = new SortHelper();
		for(Object o : vs){
			if(sh.isArray(o)){
				System.out.println();
				printArray((Object[])o);
			}else{
				System.out.print( o );
				System.out.print("\t");
			}
		}
		System.out.println();
	}
	
	void printColl(Collection<?> coll){
		SortHelper sh = new SortHelper();
		Iterator<?> it = coll.iterator();
		while(it.hasNext()){
			Object v = it.next();
			if(sh.isArray(v)){
				printArray((Object[])v);
			}else if(sh.isColl(v)){
				System.out.println(v);
			}else{
				System.out.println(v);
			}
		}
	}
	
	void printMap(Map<?,?> map){
		SortHelper sh = new SortHelper();
		for(Entry<?, ?> entry : map.entrySet()){
			Object key = entry.getKey();
			Object value = entry.getValue();
			System.out.print(key);
			System.out.print("->");
			if(sh.isArray(value)){
				printArray((Object[])value);
			}else if(sh.isMap(value)){
				printMap((Map<?,?>)value);
			}
			else{
				System.out.println(value);
			}
		}
	}
	
	@Test
	public void sortArrayTest(){
		Object obj = new Object[]{
				"1", 
				"3", 
				new String[]{"4", "1", "6"}, 
				"2",
				new Object[]{
					"8", 
					"2",
					new String[]{"2", "5", "0"},
					"4"}
		};
		SortHelper sh = new SortHelper();
		Object newobj = sh.sortArray(obj, true);
		
		printArray((Object[])newobj);
		System.out.println("--------");
		printArray((Object[])obj);
	}

	@Test
	public void sortCollTest(){
		List<Object> coll = new ArrayList<Object>();
		coll.add("2");
		coll.add("1");
		coll.add("4");
		coll.add( new String[]{"2","4","1"} );
		coll.add(Arrays.asList(new String[]{"5","2","1"}));
		coll.add("6");
		coll.add(new Object[]{
				"8", 
				"2",
				new String[]{"2", "5", "0"},
				"4"
				});
		coll.add("5");
		
		SortHelper sh = new SortHelper();
		Collection<?> newcoll = sh.sortColl(coll);
		
		printColl(newcoll);
		System.out.println("--------");
		printColl(coll);
	}
	
	@Test
	public void sortCollTest2(){
		HashSet<Object> coll = new HashSet<Object>();
		coll.add("2");
		coll.add("1");
		coll.add("4");
		coll.add( new String[]{"2","4","1"} );
		coll.add(Arrays.asList(new String[]{"5","2","1"}));
		coll.add("6");
		coll.add(new Object[]{
				"8", 
				"2",
				new String[]{"2", "5", "0"},
				"4"
				});
		coll.add("5");
		
		SortHelper sh = new SortHelper();
		Collection<?> newcoll = sh.sortColl(coll);
		
		printColl(newcoll);
		System.out.println("--------");
		printColl(coll);
		
	}

	@Test
	public void sortMapTest(){
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("aa", "aa");
		map.put("bb", "bb");
		map.put("dd", new String[]{"2", "3", "1"});
		map.put("ff", "ff");
		{
			Map<Object, Object> m1 = new HashMap<Object, Object>();
			m1.put("1", "1");
			m1.put("3", "3");
			m1.put("2", "2");
			map.put("gg", m1);
		}
		map.put("cc", "cc");
		map.put("ee", Arrays.asList(new String[]{"5", "9", "0"}));
		
		SortHelper sh = new SortHelper();
		Map<?, ?> newmap = sh.sortMap(map);
		printMap(newmap);
		System.out.println("----------");
		printMap(map);
	}


	
	@Test
	public void testFindRemove() {
		List<Integer> m = new ArrayList<Integer>( Arrays.asList(new Integer[] {0,489,8,9,15,24,95, 488}) );
		List<Integer> n = new ArrayList<Integer>( Arrays.asList(new Integer[] {0,1,8,9,15,24,95}) );
		
		int removed = CollUtils.findRemove(m, n, new Eq<Integer, Integer>() {
			@Override
			public boolean equals(Integer m, Integer n) {
				return m.equals(n);
			}
			
		});
		
		System.out.println(removed);
		System.out.println(m);
	}
	
}

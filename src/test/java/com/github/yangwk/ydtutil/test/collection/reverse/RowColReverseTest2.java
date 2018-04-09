package com.github.yangwk.ydtutil.test.collection.reverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.yangwk.ydtutil.collection.reverse.ListMapRowColReverse;

public class RowColReverseTest2 {
	
	public void printListMap(List<Map<String,Object>> sources){
		for(Map<String,Object> map : sources){
			System.out.println(map);
		}
	}

	public List<Map<String,Object>> getSourcesMap(){
		String[] col4Keys = {"id", "name", "value", "text"};
		List<Map<String,Object>> sourcesMap = new ArrayList<Map<String,Object>>();
		for(int r=0; r<5; r++){
			Map<String,Object> map = new HashMap<String, Object>();
			
			for(int m=0; m<col4Keys.length; m++){
				map.put(col4Keys[m], col4Keys[m]+ r);
			}
			
			sourcesMap.add(map);
		}
		
		return sourcesMap;
	}

	@Test
	public void testListMapRowColReverse(){
		List<Map<String,Object>> sources = getSourcesMap();
		
		printListMap(sources);
		
		System.out.println("\n---反转后---\n");
		
		List<String> srcColsKeys = new ArrayList<String>( Arrays.asList(new String[]{"id", "name", "value", "text"}) );
		List<String> newColsKeys = new ArrayList<String>( Arrays.asList(new String[]{"id", "name", "value", "text", "new"}) );
		
		ListMapRowColReverse<String,Object> reverse = new ListMapRowColReverse<String,Object>(srcColsKeys, newColsKeys);
		List<Map<String,Object>> newSources = reverse.reverse(sources);
		
		printListMap(newSources);
		
		System.out.println("\n---打印原资源---\n");
		
		printListMap(sources);
		
	}

}

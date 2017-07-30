package com.github.yangwk.ydtutil.collection.reverse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * List Map组合
 * @author yangwk
 */
public class ListMapRowColReverse<K,V> extends AbstractRowColReverse<List<Map<K,V>>, List<Map<K,V>>, Map<K,V> >{

	private List<K> srcColsKeys;	//原资源的keys，和原来的列数等长
	
	private List<K> newColsKeys;	//新资源的keys，和原资源的行数等长
	
	public ListMapRowColReverse(List<K> srcColsKeys, List<K> newColsKeys){
		this.srcColsKeys = srcColsKeys;
		this.newColsKeys = newColsKeys;
	}
	
	@Override
	int[] getRowsCols(List<Map<K, V>> sources) {
		if(sources == null || sources.size() <= 0){
			return null;
		}
		int[] rowcols = {sources.size(), sources.get(0).size() };
		return rowcols;
	}

	@Override
	List<Map<K, V>> newSources(List<Map<K, V>> sources, int rows) {
		List<Map<K, V>> newSources = new ArrayList<Map<K, V>>();
		return newSources;
	}

	@Override
	Map<K, V> newRowBean(List<Map<K, V>> newSources, int rowIdx, int cols) {
		Map<K, V> rowBean = new HashMap<K, V>();
		newSources.add(rowBean);
		return rowBean;
	}

	@Override
	Object get(List<Map<K, V>> sources, int rowIdx, int colIdx) {
		K key = srcColsKeys.get(colIdx);
		
		Object v = sources.get(rowIdx).get( key );
		return v;
	}

	
	@Override
	void set(Map<K, V> rowBean, int colIdx, Object v) {
		K key = newColsKeys.get(colIdx);
		
		@SuppressWarnings("unchecked")
		V val = (V)v;
		
		rowBean.put(key, val);
	}

}

package com.github.yangwk.ydtutil.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

public class CollUtils {
	
	/**
	 * 提取两个集合的相同部分和不同部分。不同部分是指以原始集合为参考，对比新的集合得出：被删除部分和增加部分。
	 * @author yangwk
	 * @param original 原始集合
	 * @param newcoll 新的集合
	 * @return 返回长度为3的数组，第0个是相同部分，第1个是删除部分，第2个是增加部分。
	 * 		例如原始集合{"1", "2", "3", "4"}，新的集合{"1", "3", "5"}，对比得出相同部分{"1", "3"}，删除部分{"2", "4"}，增加部分{"5"}
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T>[] fullDifference(Collection<T> original, Collection<T> newcoll){
		if(original == null || newcoll == null)
			return null;
		//保持不变的部分
		Collection<T> noChanged = CollectionUtils.intersection(original, newcoll);
		//删除的部分
		Collection<T> deleted = CollectionUtils.subtract(original, noChanged);
		//增加的部分
		Collection<T> added = CollectionUtils.subtract(newcoll, noChanged);
		
		Collection<T>[] retval = new ArrayList[3];
		retval[0] = noChanged;
		retval[1] = deleted;
		retval[2] = added;
		
		return retval;
	}
	
	
	/**
	 * 按自然顺序对指定对象进行升序排序。支持3种对象：Collection、Map、数组。同时也支持3种对象的复合类型。
	 * 存在复合类型，会递归排序，例如：Map map = ...; map.put("1","1"); map.put("3","3"); map.put("2",new String[]{"b","a"});
	 * map的keys和values都会被排序，包括String[]数组的元素
	 * @author yangwk
	 * @param obj 要进行排序的对象，不会对其进行任何修改
	 * @return 新的排序后的对象
	 */
	public static Object sort(Object obj){
		return new SortHelper().sort(obj);
	}
	
	
	public static class SortHelper {
		TreeMap<?, ?> sortMap(Object obj){
			TreeMap<?, Object> retval = null;
			if(isMap(obj)){
				Map<?, ?> m = (Map<?, ?>)obj;
				//排序keys
				retval = new TreeMap<Object,Object>(m);
				
				//排序values
				for(Entry<?, Object> ent : retval.entrySet()){
					Object v = ent.getValue();
					Object newv = sort(v);
					ent.setValue(newv);
				}
			}
			return retval;
		}

		Collection<?> sortColl(Object obj){
			Collection<Object> retval = null;
			if(isColl(obj)){
				Collection<?> coll = (Collection<?>)obj;
				Object[] vs = coll.toArray();
				Object[] newvs = (Object[])sortArray(vs, false);
				
				retval = new ArrayList<Object>();
				for(Object v : newvs){
					retval.add(v);
				}
			}
			return retval;
		}
		
		
		Object sortArray(Object obj, boolean copySrc){
			Object[] retval = null;
			if(isArray(obj)){
				Object[] vs = (Object[])obj;
				Object[] newvs = vs;
				if(copySrc){
					newvs = Arrays.copyOf(vs, vs.length);	//副本
				}
				//先提取出来
				List<Object> sim = new ArrayList<Object>();
				List<Object> nosim = new ArrayList<Object>();
				for(Object v : newvs){
					if(isSimple(v))
						sim.add(v);
					else
						nosim.add(v);
				}
				
				//分开排序
				Object[] sims = sim.toArray();
				Arrays.sort(sims);	//排序单个
				
				Object[] newnosim = new Object[nosim.size()];
				int r=0;
				for(Object v : nosim){
					Object newv = sort(v);	//排序非单个
					newnosim[r] = newv;
					r ++;
				}
				//单个在前，非单个在后
				retval = new Object[newvs.length];
				System.arraycopy(sims, 0, retval, 0, sims.length);
				System.arraycopy(newnosim, 0, retval, sims.length, newnosim.length);
			}
			return retval;
		}

		boolean isArray(Object obj) {
			return obj.getClass().isArray();
		}
		
		boolean isColl(Object obj){
			return ( obj instanceof Collection<?> );
		}

		boolean isMap(Object obj){
			return ( obj instanceof Map<?, ?> );
		}
		
		boolean isSimple(Object obj){
			return ! isArray(obj) && ! isColl(obj) && ! isMap(obj);
		}
		
		Object sort(Object obj){
			Object retval = null;
			if(obj == null){
				return null;
			}else if(isArray(obj)){
				retval = sortArray(obj, true);
			}else if(isColl(obj)){
				retval = sortColl(obj);
			}else if(isMap(obj)){
				retval = sortMap(obj);
			}else{
				retval = obj;
			}
			
			return retval;
		}
		
	}

	
	
	
	

}

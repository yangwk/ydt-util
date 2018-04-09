package com.github.yangwk.ydtutil.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.github.yangwk.ydtutil.Eq;

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
	

	/**
	 * 查找并删除。将不在collFind集合内的collRemove元素删除掉
	 * @author yangwk
	 * @param collRemove 要删除的集合
	 * @param collFind 匹配的集合
	 * @param eq 等值比较
	 * @return 删除的元素个数
	 */
	public static <M,N> int findRemove(Collection<M> collRemove, Collection<N> collFind, Eq<M, N> eq) {
		if(collRemove == null || collFind == null)
			return 0;
		int removed = 0;
		
		Iterator<M> iterM =  collRemove.iterator();
		while(iterM.hasNext()) {
			M m = iterM.next();
			
			boolean found = false;
			Iterator<N> iterN =  collFind.iterator();
			while(iterN.hasNext()) {
				N n = iterN.next();
				
				if(eq.equals(m, n)) {
					found = true;
					break;
				}
				
			}
			
			//找不到，就删除
			if(! found) {
				iterM.remove();
				removed ++;
			}
		}
		
		return removed;
	}
	
	

	/**
	 * 用于行与行之间相等数据的范围提取
	 * @author yangwk
	 * @param datas 要遍历的数据
	 * @param rangeEqual 
	 * @return 行的范围，每一项都代表每行相等值的跨度范围
	 */
	public static <T> List<Range> range(final List<T> datas, Eq<T, T> rangeEqual){
		List<Range> result = new ArrayList<Range>();
		
		if(datas == null || datas.isEmpty()){
			return result;
		}
		
		int preIndex = 0;	//上一个下标
		T preValue = datas.get(preIndex);	//上一个值
		int currIndex=1;	//当前下标
		T currValue = null;	//当前值
		
		for(; currIndex< datas.size(); currIndex++){
			currValue = datas.get(currIndex);
			if( rangeEqual.equals(preValue, currValue) ){	//比较上一个和当前
				;
			}
			else{	//不相等，即触发了新一轮
				result.add(new Range(preIndex, currIndex-1));
				
				//新一轮赋值
				preIndex = currIndex;
				preValue = currValue;
			}
		}
		currIndex --;
		//最后没有触发新一轮（末尾值连续的情况）
		if(currIndex - preIndex >= 0){
			result.add(new Range(preIndex, currIndex));
		}
		
		return result;
	}
	
	
}

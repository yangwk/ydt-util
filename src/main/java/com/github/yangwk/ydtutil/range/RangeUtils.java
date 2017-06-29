package com.github.yangwk.ydtutil.range;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangwk
 */
public class RangeUtils {
	
	/**
	 * 用于行与行之间相等数据的抽取
	 * @author yangwk
	 * @param datas 要遍历的数据
	 * @param rangeEqual 
	 * @return 行的范围，每一项都代表每行相等值的跨度范围
	 */
	public static <T> List<Range> extract(final List<T> datas, RangeEqual<T> rangeEqual){
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
			if( rangeEqual.isEqual(preValue, currValue) ){
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

package com.github.yangwk.ydtutil.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * 排序
 * @author yangwk
 *
 */
public class SortHelper {


	public TreeMap<?, ?> sortMap(Object obj){
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

	public Collection<?> sortColl(Object obj){
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
	
	
	public Object sortArray(Object obj, boolean copySrc){
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

	public boolean isArray(Object obj) {
		return obj.getClass().isArray();
	}
	
	public boolean isColl(Object obj){
		return ( obj instanceof Collection<?> );
	}

	public boolean isMap(Object obj){
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

package com.github.yangwk.ydtutil.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeQueryAdapter<T> implements TreeQuery<T>{

	@Override
	public T queryById(T t) {
		return null;
	}

	@Override
	public List<T> queryChildrenById(T t) {
		return new ArrayList<T>();
	}

	@Override
	public T queryByParentId(T t) {
		return null;
	}
	
}

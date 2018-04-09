package com.github.yangwk.ydtutil.collection.reverse;

/**
 * 行与列的坐标反转
 * @author yangwk
 */
public interface RowColReverse<T,M> {

	/**
	 * 反转。不会对数据源进行修改
	 * @author yangwk
	 * @param sources 数据源
	 * @return 反转后的新数据
	 */
	M reverse(final T sources);
}

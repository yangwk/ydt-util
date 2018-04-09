package com.github.yangwk.ydtutil;

/**
 * 判断两个值是否相等的接口
 * @author yangwk
 */
public interface Eq<M,N> {

	/**
	 * 判断两个值是否相等
	 * <br>普遍的做法：
	 * <code>return m == null ? m == n : m.equals(n);</code>
	 * @author yangwk
	 * @param m 比较值
	 * @param n 比较值
	 * @return 相等则返回true
	 */
	boolean equals(M m,N n);
}

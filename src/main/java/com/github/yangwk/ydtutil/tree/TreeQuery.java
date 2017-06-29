package com.github.yangwk.ydtutil.tree;

import java.util.List;

/**
 * 树结构查询接口 
 * @author yangwk
 */
public interface TreeQuery<T> {
	
	/**
	 * 查询本节点
	 * <br>相似于：
	 * <pre>
	 * select * from tb where id = {t.getId()}
	 * </pre>
	 * @author yangwk
	 *
	 * @param t
	 * @return may be null
	 */
	T queryById(T t);
	
	/**
	 * 查询子节点
	 * <br>相似于：
	 * <pre>
	 * select * from tb where parentId = {t.getId()}
	 * </pre>
	 * @author yangwk
	 *
	 * @param t
	 * @return not be null
	 */
	List<T> queryChildrenById(T t);
	
	/**
	 * 查询父节点
	 * <br>相似于：
	 * <pre>
	 * select * from tb where id = {t.getParentId()}
	 * </pre>
	 * @author yangwk
	 *
	 * @param t
	 * @return null代表根
	 */
	T queryByParentId(T t);
	
}

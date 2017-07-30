package com.github.yangwk.ydtutil.collection.reverse;

/**
 * 抽象基类
 * @author yangwk
 */
public abstract class AbstractRowColReverse<T, M, R> implements RowColReverse<T,M> {

	/**
	 * 获取原资源的行数和列数
	 * @param sources
	 * @return 一个长度为2的数组，分别代表行数和列数
	 */
	abstract int[] getRowsCols(T sources);
	
	/**
	 * 创建新资源。确保新资源能容纳下rows个数据
	 * @param sources
	 * @param rows 
	 * @return 新资源
	 */
	abstract M newSources(T sources, int rows);
	
	/**
	 * 创建新的行bean，这个bean代表整行的数据
	 * @param newSources 新资源
	 * @param rowIdx 当前行
	 * @param cols 总列数
	 * @return
	 */
	abstract R newRowBean(M newSources, int rowIdx, int cols);
	
	/**
	 * 根据行和列获取原资源的数据
	 * @param sources 原资源
	 * @param rowIdx 行
	 * @param colIdx 列
	 * @return
	 */
	abstract Object get(T sources, int rowIdx, int colIdx);
	
	/**
	 * 将原资源的数据赋值给行bean
	 * @param rowBean
	 * @param colIdx
	 * @param v 原资源的数据
	 */
	abstract void set(R rowBean, int colIdx, Object v);
	
	
	@Override
	public M reverse(final T sources) {
		int[] rowcols = getRowsCols(sources);
		if(rowcols == null || rowcols.length != 2 || rowcols[0] <= 0 || rowcols[1] <= 0){
			throw new IllegalArgumentException("行数和列数不合法");
		}
		
		int rows = rowcols[0], cols = rowcols[1];
		
		//新资源的行数和列数，分别等于原资源的列数和行数
		//坐标系x,y互换
		M newSources = newSources(sources, cols);
		for(int y=0; y<cols; y++){
			R rowBean = newRowBean(newSources, y, rows);
			for(int x=0; x<rows; x++){
				Object v = get(sources, x, y);
				set(rowBean, x, v);
			}
		}
		
		return newSources;
	}

}

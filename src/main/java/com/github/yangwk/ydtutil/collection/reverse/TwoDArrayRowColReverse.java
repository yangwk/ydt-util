package com.github.yangwk.ydtutil.collection.reverse;

/**
 * 二维数组
 * @author yangwk
 */
public class TwoDArrayRowColReverse extends AbstractRowColReverse<Object[][], Object[][], Object[]>{
	
	@Override
	int[] getRowsCols(Object[][] sources) {
		if(sources == null || sources.length <= 0){
			return null;
		}
		int[] rowscols = {sources.length, sources[0].length};
		return rowscols;
	}

	@Override
	Object[][] newSources(Object[][] sources, int rows) {
		Object[][] newSources = new Object[rows][];
		return newSources;
	}

	@Override
	Object[] newRowBean(Object[][] newSources, int rowIdx, int cols) {
		newSources[rowIdx] = new Object[cols];
		return newSources[rowIdx];
	}

	@Override
	Object get(Object[][] sources, int rowIdx, int colIdx) {
		return sources[rowIdx][colIdx];
	}

	@Override
	void set(Object[] rowBean, int colIdx, Object v) {
		rowBean[colIdx] = v;
	}

}

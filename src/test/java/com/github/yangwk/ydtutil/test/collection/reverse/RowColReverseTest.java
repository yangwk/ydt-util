package com.github.yangwk.ydtutil.test.collection.reverse;

import org.junit.Test;

import com.github.yangwk.ydtutil.collection.reverse.RowColReverse;
import com.github.yangwk.ydtutil.collection.reverse.TwoDArrayRowColReverse;

public class RowColReverseTest {
	
	public void print(Object[][] sources){
		for(int r=0; r<sources.length; r++){
			for(int c=0; c<sources[r].length; c++){
				Object col = sources[r][c];
				if(col instanceof int[]){
					int[] cols = (int[])col;
					System.out.print(cols[0]+","+cols[1]);
				}else{
					System.out.print(col);
				}
				System.out.print("\t\t");
			}
			System.out.println();
		}
	}
	
	public Object[][] getSourcesArray(){
		//9,9
		//10,10
		//1,1
		//1,2
		//2,1
		//3,4
		//10,5
		int mX = 10, mY = 5;
		Object[][] sources = new Object[mY+1][];
		
		{
			for(int y=0; y<sources.length; y++){
				sources[y] = new Object[mX+1];
				Object[] row = sources[y];
				for(int x=0; x<row.length; x++){
					int[] col = new int[2];	//x,y
					col[0] = x;
					col[1] = y;
					if(y == 0){
						if(x != 0){
							row[x] = "值"+x;
						}else{
							row[x] = "名称";
						}
					}else{
						if(x == 0){
							row[x] = "司"+y;
						}else{
							row[x] = col;
						}
					}
				}
			}
		}
		
		return sources;
	}

	@Test
	public void test() {
		
		Object[][] sources = getSourcesArray();
		
		print(sources);
		
		//开始反转
		System.out.println("\n---反转后---\n");
		
		{
			
			int rows = sources.length;
			int cols = sources[0].length;	//获取原来的行数和列数
			Object[][] newSources = new Object[cols][];	//分配一个列表，可容纳行数
			for(int y=0; y<cols; y++){
				newSources[y] = new Object[rows];
				Object[] row = newSources[y];
				for(int x=0; x<rows; x++){
					row[x] = sources[x][y];
				}
			}
			
			print(newSources);
		}
		
		
	}
	
	@Test
	public void testTwoDArrayRowColReverse(){
		Object[][] sources = getSourcesArray();
		
		print(sources);
		
		System.out.println("\n---反转后---\n");
		
		RowColReverse<Object[][], Object[][]> reverse = new TwoDArrayRowColReverse();
		Object[][] newSources = reverse.reverse(sources);
		
		print(newSources);
		
		System.out.println("\n---打印原资源---\n");
		
		print(sources);
		
	}

}

package com.github.yangwk.ydtutil.tree;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.yangwk.ydtutil.tree.Tree;
import com.github.yangwk.ydtutil.tree.TreeNode;
import com.github.yangwk.ydtutil.tree.TreeQueryAdapter;

public class TreeTest3 {
	
	//模拟数据库
	static List<City> _cityDatabase = new ArrayList<City>();
	
	//初始化数据
	static{
		_cityDatabase.add(new City(1, "中国", 18));
		_cityDatabase.add(new City(2, "广西省", 1));
		_cityDatabase.add(new City(3, "钦州市", 2));
		_cityDatabase.add(new City(4, "钦南区", 3));
		_cityDatabase.add(new City(5, "犀牛脚镇", 4));
		_cityDatabase.add(new City(6, "船厂村", 5));
		_cityDatabase.add(new City(7, "广东省", 1));
		_cityDatabase.add(new City(8, "广州市", 7));
		_cityDatabase.add(new City(9, "天河区", 8));
		_cityDatabase.add(new City(10, "番禺区", 8));
		_cityDatabase.add(new City(11, "深圳市", 7));
		_cityDatabase.add(new City(12, "福田区", 11));
		_cityDatabase.add(new City(13, "海南省", 1));
		_cityDatabase.add(new City(14, "青龙村", 5));
		_cityDatabase.add(new City(15, "担水坑", 5));
		_cityDatabase.add(new City(16, "炮台村", 5));
		_cityDatabase.add(new City(17, "西乡村", 5));
		_cityDatabase.add(new City(18, "地球", null));
		_cityDatabase.add(new City(19, "湖南省", 1));
		_cityDatabase.add(new City(20, "长沙市", 19));
		_cityDatabase.add(new City(21, "新农屯1", 6));
		_cityDatabase.add(new City(22, "新农屯2", 6));
	}
	
	@Test
	public void test_getPath(){
		Tree<City> tree = new Tree<City>();
		
		TreeNode<City> start = new TreeNode<City>( new City(22, "新农屯2", 6) );
		
		List<TreeNode<City>> pathToRoot = tree.getPath(start, new TreeQueryAdapter<TreeNode<City>>() {

			@Override
			public TreeNode<City> queryByParentId(TreeNode<City> t) {
				City city = t.getUserObject();
				for(City c : _cityDatabase){
					if(c.getId().equals(city.getParentId())){
						return new TreeNode<City>(c);
					}
				}
				return null;
			}
			
		});
		
		for(TreeNode<City> node : pathToRoot){
			System.out.println(node.getUserObject().toString());
		}
		
	}
	
	//
	
	

}
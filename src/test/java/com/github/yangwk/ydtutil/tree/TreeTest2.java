package com.github.yangwk.ydtutil.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.github.yangwk.ydtutil.tree.JsonConvert;
import com.github.yangwk.ydtutil.tree.Tree;
import com.github.yangwk.ydtutil.tree.TreeNode;
import com.github.yangwk.ydtutil.tree.TreeQuery;
import com.github.yangwk.ydtutil.tree.TreeQueryAdapter;

public class TreeTest2 {
	
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
	public void test_getRoot(){
		Tree<City> tree = new Tree<City>();
		
		TreeNode<City> start = new TreeNode<City>( new City(12, "地球", 11) );
		
		TreeNode<City> root = tree.getRoot(start, new TreeQueryAdapter<TreeNode<City>>() {

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
		
		System.out.println(root.getUserObject().toString());
	}
	
	@Test
	public void test_getWholeTree(){
		Tree<City> tree = new Tree<City>();
		
		TreeNode<City> start = new TreeNode<City>( new City(18, null, 18) );
		
		TreeNode<City> root = tree.getWholeTree(start, new TreeQuery<TreeNode<City>>() {
			
			@Override
			public List<TreeNode<City>> queryChildrenById(TreeNode<City> t) {
				List<TreeNode<City>> cities = new ArrayList<TreeNode<City>>();
				for(City city : _cityDatabase){
					if(city.getParentId() != null && city.getParentId().equals(t.getUserObject().getId())){
						cities.add( new TreeNode<City>(city) );
					}
				}
				
				return cities;
			}
			
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
			
			@Override
			public TreeNode<City> queryById(TreeNode<City> t) {
				for(City city : _cityDatabase){
					if(city.getId().equals(t.getUserObject().getId())){
						return new TreeNode<City>(city);
					}
				}
				return null;
			}
		});
		
		String str = tree.toJson(root, new JsonConvert<City>() {
			
			@Override
			public String convert(City t) {
				return JSONObject.toJSONString(t);
			}
		});
		
		System.out.println( str);
		
	}

	@Test
	public void test_toJson(){
		TreeNode<String> root = new TreeNode<String>("root");
		TreeNode<String> root_0 = new TreeNode<String>("root_0");
		TreeNode<String> root_1 = new TreeNode<String>("root_1");
		
		TreeNode<String> root_0_0 = new TreeNode<String>("root_0_0");
		TreeNode<String> root_0_1 = new TreeNode<String>("root_0_1");
		
		TreeNode<String> root_1_0 = new TreeNode<String>("root_1_0");
		TreeNode<String> root_1_1 = new TreeNode<String>("root_1_1");
		
		TreeNode<String> root_0_0_0 = new TreeNode<String>("root_0_0_0");
		TreeNode<String> root_0_0_1 = new TreeNode<String>("root_0_0_1");
		
		TreeNode<String> root_0_1_0 = new TreeNode<String>("root_0_1_0");
		TreeNode<String> root_0_1_1 = new TreeNode<String>("root_0_1_1");
		
		root_0_1.add(root_0_1_0);
		root_0_1.add(root_0_1_1);
		
		root_0_0.add(root_0_0_0);
		root_0_0.add(root_0_0_1);
		
		root_0.add(root_0_0);
		root_0.add(root_0_1);
		
		root_1.add(root_1_0);
		root_1.add(root_1_1);
		
		root.add(root_0);
		root.add(root_1);
		
		Tree<String> tree = new Tree<String>();
		String str = tree.toJson(root, new JsonConvert<String>() {
			
			@Override
			public String convert(String t) {
				return JSONObject.toJSONString(t);
			}
		});
		
		System.out.println(str);
		
	}

	@Test
	public void test_sortChildren(){
		TreeNode<String> root = new TreeNode<String>("root");
		TreeNode<String> root_0 = new TreeNode<String>("root_0");
		TreeNode<String> root_1 = new TreeNode<String>("aroot_1");
		
		TreeNode<String> root_0_0 = new TreeNode<String>("root_0_0");
		TreeNode<String> root_0_1 = new TreeNode<String>("root_0_1");
		
		TreeNode<String> root_1_0 = new TreeNode<String>("root_1_0");
		TreeNode<String> root_1_1 = new TreeNode<String>("root_1_1");
		
		TreeNode<String> root_0_0_0 = new TreeNode<String>("root_0_0_0");
		TreeNode<String> root_0_0_1 = new TreeNode<String>("aroot_0_0_1");
		
		TreeNode<String> root_0_1_0 = new TreeNode<String>("root_0_1_0");
		TreeNode<String> root_0_1_1 = new TreeNode<String>("root_0_1_1");
		
		root_0_1.add(root_0_1_0);
		root_0_1.add(root_0_1_1);
		
		root_0_0.add(root_0_0_0);
		root_0_0.add(root_0_0_1);
		
		root_0.add(root_0_0);
		root_0.add(root_0_1);
		
		root_1.add(root_1_0);
		root_1.add(root_1_1);
		
		root.add(root_0);
		root.add(root_1);
		
		//排序前
		Tree<String> tree = new Tree<String>();
		String str = tree.toJson(root, new JsonConvert<String>() {
			
			@Override
			public String convert(String t) {
				return JSONObject.toJSONString(t);
			}
		});
		System.out.println(str);
		
		//排序后
		TreeNode.sortChildren(root, new Comparator<TreeNode<String>>() {

			@Override
			public int compare(TreeNode<String> o1, TreeNode<String> o2) {
				return o1.getUserObject().compareTo(o2.getUserObject());
			}
		});
		str = tree.toJson(root, new JsonConvert<String>() {
			
			@Override
			public String convert(String t) {
				return JSONObject.toJSONString(t);
			}
		});
		System.out.println(str);
		
	}

}
package com.github.yangwk.ydtutil.test.tree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.After;
import org.junit.Test;

import com.github.yangwk.ydtutil.test.db.DataSourceFactory;
import com.github.yangwk.ydtutil.tree.Tree;
import com.github.yangwk.ydtutil.tree.TreeQuery;

public class TreeTest {
	
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
	public void testSql_getChildrenList() throws SQLException{
		
		Tree<City> tree = new Tree<City>();
		
		List<City> result = tree.getChildrenList( new City(3, null, null),  new TreeQuery<City>() {
			
			@Override
			public List<City> queryChildrenById(City t) {
				QueryRunner run = new QueryRunner(DataSourceFactory.getDataSource());
				
				ResultSetHandler<List<City>> h = new BeanListHandler<City>(City.class);
				
				try {
					List<City> cities = run.query("select * from city where parentId = ?", h, t.getId());
					return cities;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return new ArrayList<City>();
			}
			
			@Override
			public City queryById(City t) {
				QueryRunner run = new QueryRunner(DataSourceFactory.getDataSource());
				
				ResultSetHandler<City> h = new BeanHandler<City>(City.class);
				
				try {
					City city = run.query("select * from city where id = ?", h, t.getId());
					return city;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public City queryByParentId(City t) {
				return null;
			}
		});
		
		System.out.println(result.size());
		for(City city : result){
			System.out.println(ReflectionToStringBuilder.toString(city));
		}
		
	}
	
	@Test
	public void testData_getChildrenList() throws SQLException{
		
		Tree<City> tree = new Tree<City>();
		
		List<City> result = tree.getChildrenList( new City(5, null, null),  new TreeQuery<City>() {
			
			@Override
			public List<City> queryChildrenById(City t) {
				List<City> cities = new ArrayList<City>();
				for(City city : _cityDatabase){
					if(city.getParentId() != null && city.getParentId().equals(t.getId())){
						cities.add(city);
					}
				}
				
				return cities;
			}
			
			@Override
			public City queryById(City t) {
				for(City city : _cityDatabase){
					if(city.getId().equals(t.getId())){
						return city;
					}
				}
				return null;
			}

			@Override
			public City queryByParentId(City t) {
				return null;
			}
		});
		
		System.out.println(result.size());
		for(City city : result){
			System.out.println(ReflectionToStringBuilder.toString(city));
		}
		
	}

	@After
	public void after(){
		DataSourceFactory.close();
		System.out.println("datasource closed");
	}

}
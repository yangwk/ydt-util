package com.github.yangwk.ydtutil.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

public class DataSourceFactory {
	private final static DruidDataSource dataSource = new DruidDataSource();
	private volatile static boolean inited = false;
	
	private synchronized static void init(){
		if(inited)
			return ;
		dataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		
		dataSource.setMaxActive(20);
		dataSource.setInitialSize(1);
		dataSource.setMaxWait(60000);
		dataSource.setMinIdle(1);
		
		try {
			dataSource.init();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		inited = true;
	}
	
	public static DataSource getDataSource(){
		init();
		return dataSource;
	}
	
	public synchronized static void close(){
		dataSource.close();
	}
}

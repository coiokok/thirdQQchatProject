package com.qq.db;

// DButil类，这里写所有项目中会用到的有关数据库的操作

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
	
	private Connection conn = null;

	private Statement sta = null;

	private ResultSet rs = null;

	public DBUtil(){
		this.conn = this.getConnection();
	}	

	private Connection getConnection(){
		Connection conn = null;
		
		try {
			// Properties类，该类主要用于读取Java的配置文件，也就是读取存储着数据库连接信息的db.properties文件，这样能保护数据库
			// db.properties文件是以键值对方式存储着数据库连接信息的
			Properties properties = new Properties();
			properties.load(DBUtil.class.getResourceAsStream("/db.properties"));

			// getProperty(String key);方法  在此属性列表中搜索具有指定键的属性
			String driverClass =  properties.getProperty("driver_class");
			String connectionUrl = properties.getProperty("connection_url");
			String dbUser = properties.getProperty("db_user");
			String dbPassword = properties.getProperty("db_password");
			
			Class.forName(driverClass);
			conn = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	
	public int update(String sql) {
		int row = -1;
		try {
			sta = conn.createStatement();
			row = sta.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("update:SQLException");
			e.printStackTrace();
		}
		return row;
	}

	
	public ResultSet query(String sql) {
		try {
			sta = conn.createStatement();
			rs = sta.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	
	public void close() {
		try {
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(sta != null){
				sta.close();
				sta = null;
			}
			if(conn != null){
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
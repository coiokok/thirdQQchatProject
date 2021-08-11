package com.qq.dao;

// 操作数据库中用户表qquser方法的接口实现类

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qq.bean.Qquser;
import com.qq.db.DBUtil;

public class QqUserDaoImpl implements IQqUserDao {

	
	// 根据qq账号去查找用户的所有信息，返回一个qquser对象
	public Qquser findById(String qqNo) {
		Qquser qquser = null;
		String sql = "select * from qquser where account = '" + qqNo + "'";
		DBUtil util = new DBUtil();
		ResultSet rs = util.query(sql);
		try {
			while(rs.next()) {
				qquser = new Qquser();
				qquser.setAccount(rs.getString(1));
				qquser.setName(rs.getString(2));
				qquser.setPassword(rs.getString(3));
				qquser.setState(rs.getString(4));
				qquser.setIp(rs.getString(5));
				qquser.setPort(rs.getString(6));
				qquser.setPic(rs.getString(7));
				qquser.setInfo(rs.getString(8));
				qquser.setPlace1(rs.getString(9));
				qquser.setPlace2(rs.getString(10));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qquser;
	}

	
	// 保存用户信息
	public int save(Qquser qquser) {
		if(qquser == null){
			return -1;
		}
		int result = -1;
		String account = qquser.getAccount();
		String name = qquser.getName();
		String password = qquser.getPassword();
		String state = qquser.getState();
		String ip = qquser.getIp();
		String port = qquser.getPort();
		String pic = qquser.getPic();
		String sql = "insert into QqUser(account,name,password,state,ip,port,pic,info,place1,place2) " + 
		 "values(" +
		 "'" + account + "', " +
		 "'" + name + "', " +
		 "'" + password + "', " +
		 "'" + state + "', " +
		 "'" + ip + "', " +
		 "'" + port + "', " +
		 "'" + pic + "', " +
		 "'" + 0 + "', " +
		 "'" + 0 + "', " +
		 "'" + 0 + "'  " +
		 ")";
		DBUtil dbUtil = new DBUtil();
		result = dbUtil.update(sql);
		dbUtil.close();
		return result;
	}

	
	// 根据传进来的qq用户对象中的账号更新该用户的 在线标识state，ip地址，port值
	public int update(Qquser qquser) {
		String sql = "update qquser set state = '" + qquser.getState() + "', ip = '" + qquser.getIp() + "', port = '" + qquser.getPort() + "' where account = '" + qquser.getAccount() + "'";
		DBUtil dbUtil = new DBUtil();
		int num = dbUtil.update(sql);
		return num;
	}

	
	// 根据传进来的sql语句进行更新
	public int update(String sql) {
		DBUtil dbUtil = new DBUtil();
		int num =dbUtil.update(sql);
		return num;
	}
	
	
	// 根据sql语句进行查找用户，返回的可能是多个用户对象的集合
	public List<Qquser> findBySql(String sql) {
		List<Qquser> list = new ArrayList<Qquser>();
		DBUtil util = new DBUtil();
		ResultSet rs = util.query(sql);
		try {
			while(rs.next()) {
				Qquser qquser = new Qquser();
				qquser.setAccount(rs.getString(1));
				qquser.setName(rs.getString(2));
				qquser.setPassword(rs.getString(3));
				qquser.setState(rs.getString(4));
				qquser.setIp(rs.getString(5));
				qquser.setPort(rs.getString(6));
				qquser.setPic(rs.getString(7));
				qquser.setInfo(rs.getString(8));
				qquser.setPlace1(rs.getString(9));
				qquser.setPlace2(rs.getString(10));
				list.add(qquser);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
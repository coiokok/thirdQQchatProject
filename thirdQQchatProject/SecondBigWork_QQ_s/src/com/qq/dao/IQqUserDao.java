package com.qq.dao;

// 操作数据库中用户表qquser方法的接口

import java.util.List;

import com.qq.bean.Qquser;


public interface IQqUserDao {

	public int save(Qquser icqUser);             // 保存用户信息
	
	public int update(Qquser qquser);            // 根据传进来的qq用户对象中的账号更新该用户的 在线标识state，ip地址，port值（一般该用户登录之后就调用这个方法，然后数据库就改变该用户的信息为已登录）
	
	public int update(String SQL);               // 根据传进来的sql语句进行更新

	public Qquser findById(String qqNo);         // 根据qq账号去查找用户的所有信息，返回一个qquser对象
	
	public List findBySql(String sql);           // 根据sql语句进行查找用户，返回的可能是多个用户对象的集合
	
}


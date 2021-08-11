package com.qq.bean;

// qquser数据表的实体类
// 里面有 1.get and set方法   2.toString()方法

import java.io.Serializable;

public class Qquser implements Serializable {        // 因为需要网络传输对象信息所以需要实现序列化，就需要继承Serializable接口，这只是一个标识接口，并无方法需要重写
    
    private String account;         // 登录用户名实例
   
    private String name;            // 用户昵称实例
  
    private String password;        // 用户密码实例
    
    private String state;           // 用户是否在线标识，在线为1，离线为0
 
    private String ip;              // 用户ip地址实例

    private String port;            // 用户port值实例

    private String pic;             // 用户头像标识，就是图片名字

    // 三个备用实例
    private String info;
    private String place1;
    private String place2;
    
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPlace1() {
		return place1;
	}
	public void setPlace1(String place1) {
		this.place1 = place1;
	}
	public String getPlace2() {
		return place2;
	}
	public void setPlace2(String place2) {
		this.place2 = place2;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "Qquser [account=" + account + ", name=" + name + ", password="
				+ password + ", state=" + state + ", ip=" + ip + ", port="
				+ port + ", pic=" + pic + ", info=" + info + ", place1="
				+ place1 + ", place2=" + place2 + "]";
	}
}
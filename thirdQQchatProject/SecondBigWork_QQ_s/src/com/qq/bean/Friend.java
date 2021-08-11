package com.qq.bean;

// friend数据表的实体类
// 只用写出 get和set方法即可

import java.io.Serializable;

public class Friend implements Serializable {        // 因为需要网络传输对象信息所以需要实现序列化，就需要继承Serializable接口，这只是一个标识接口，并无方法需要重写

    private String userAccount;
 
    private String friendAccount;
    
    
	public String getFriendAccount() {
		return friendAccount;
	}
	public void setFriendAccount(String friendAccount) {
		this.friendAccount = friendAccount;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
}
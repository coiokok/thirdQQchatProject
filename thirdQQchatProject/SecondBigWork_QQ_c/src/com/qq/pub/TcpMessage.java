package com.qq.pub;

// 该类是报文类，像是一种传递工具但里面放的东西不一样，会根据报文头的不同情况去用不同的方法去操作体
// 头是要使用的方法，体是被使用的东西
// implements Serializable 这只是一个标识接口代表该类是报文类里面无方法需要实现

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// implements Serializable接口 就代表这是一个报文类了
public class TcpMessage implements Serializable {
	private String head = null;             // 报文头
	private Map<String, Object> map = new HashMap<String, Object>();            // 报文体，里面要有key还有对象
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	
	public void setBody(String key, Object value) {
		this.map.put(key, value);
	}
	public Object getBody(String key) {
		return this.map.get(key);
	}
}
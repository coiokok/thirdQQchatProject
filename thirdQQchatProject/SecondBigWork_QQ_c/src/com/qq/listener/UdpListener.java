package com.qq.listener;

// 用观察者模式实现事件的UdpListener接口，在主页面实现事件监听，用于获取报文头，参数就是接收到的报文

public interface UdpListener {
	public void exectue(String udpInfo);
}

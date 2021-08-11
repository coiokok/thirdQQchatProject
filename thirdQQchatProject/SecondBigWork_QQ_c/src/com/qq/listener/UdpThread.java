package com.qq.listener;

// 以防阻塞的UdpThread，这个类同时有三个作用，1：是个线程可以处理多个消息发送，2：用观察者模式实现事件，3：还通过UdpSocket实现发送消息

import com.qq.pub.UdpSocket;

public class UdpThread extends Thread {
	
	private UdpSocket udpSocket = null;
	private UdpListener l = null;
	
	
	// 重写构造方法将udpSocket作为参数传入
	public UdpThread() {
		// TODO Auto-generated constructor stub
	}
	public UdpThread(UdpSocket udpSocket) {
		this.udpSocket = udpSocket;
	}
	
	
	// addUdpListener方法获得事件处理l，当有好友登录就会实例化udpSocket传输信息然后就会触发
	public void addUdpListener(UdpListener l) {
		this.l = l;
	}
	
	
	// 重写线程的run方法
	@Override
	public void run() {
		while(true) {
			String udpInfo = this.udpSocket.receive();          // 获得udpSocket接收到的信息
			this.l.exectue(udpInfo);                            // 并将接收到的信息作为参数传给事件处理的l
		} 
	}
}
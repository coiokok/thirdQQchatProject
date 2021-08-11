package com.qq.pub;

// 与各终端之间进行通讯的UdpSocket
// 其实UdpSocket就负责各个客户端（用户）之间发送信息的功能

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpSocket {
	private DatagramSocket datagramSocket = null;                // DatagramSocket进行发送信息
	private DatagramPacket datagramPacket = null;                // DatagramPacket进行信息编写，根据发送还是接收实例化格式不同
	
	
	// 重写构造函数不传入参数ip和port值时就是使用接收信息的datagramPacket
	public UdpSocket() {
		byte[] pool = new byte[1024];
		try {
			this.datagramSocket = new DatagramSocket();
			this.datagramPacket = new DatagramPacket(pool, 0, pool.length);       // datagramPacket接收信息的格式
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// 重写构造函数传入参数ip和port值时就是使用发送信息的datagramPacket
	public UdpSocket(String ip, int port) {
		byte[] pool = new byte[1024];
		try {
			this.datagramSocket = new DatagramSocket();
			this.datagramPacket = new DatagramPacket(pool, 0, pool.length, InetAddress.getByName(ip), port);       // datagramPacket发送信息的格式
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// send发送信息方法，将传入的参数作为数据进行传输
	public void send(String udpInfo) {
		this.datagramPacket.setData(udpInfo.getBytes());
		try {
			this.datagramSocket.send(this.datagramPacket);           // socket.send(packet);  .send()方法 发送packet对象
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//  receive接收信息方法，该方法返回接收到的信息
	public String receive() {
		String data = null;
		try {
			this.datagramSocket.receive(this.datagramPacket);         // socket.receive(packet);  .receive()方法 接收packet对象
			data = new String(this.datagramPacket.getData());         // 将packet中的byte数据获取并转换为字符串
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;               // 返回接收到的信息
	}
	
	
	// getPort获得进行传输的终端的Port值的方法
	public int getPort() {
		return this.datagramSocket.getLocalPort();                    // .getLocalPort() 获得自己port值的方法
	}
	
}

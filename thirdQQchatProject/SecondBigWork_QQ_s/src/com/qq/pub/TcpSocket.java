package com.qq.pub;

// 封装客户端与服务器进行交流的代码，隐藏代码思路并使代码变得简洁
//其实TcpSocket相当于客户端登录了和服务器通信所用的工具
//与服务器通信了才能进行之后的各种操作

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpSocket {
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	
	// TcpSocket构造方法，当调用这个构造方法并传入参数ip和port值时，就将这两个值实例化成socket进行服务器网络传输连接请求
	public TcpSocket() {
		// TODO Auto-generated constructor stub
	}
	public TcpSocket(String ip, int port) {
		try {
			this.socket = new Socket(ip, port);
			out = new ObjectOutputStream(this.socket.getOutputStream());
			in = new ObjectInputStream(this.socket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// submit方法参数为一个TcpMessage类的报文实例sMessage为需要发送的报文
	public TcpMessage submit(TcpMessage sMessage) {
		
		TcpMessage rMessage = null;          // rMessage为需要接受的报文，他同样也是一个TcpMessage报文类的实例
		
		try {
			// 将sMessage对象通过writeObject传输对象的方法发送
			this.out.writeObject(sMessage);
			this.out.flush();
			
			//等待服务器处理
			rMessage = (TcpMessage)this.in.readObject();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rMessage;
	}
}

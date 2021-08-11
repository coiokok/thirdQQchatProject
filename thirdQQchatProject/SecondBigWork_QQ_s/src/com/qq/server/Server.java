package com.qq.server;

// 服务器需要对应多个客户端client所以服务器需要实现多线程

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.qq.pub.CommonUse;

public class Server {

public static void main(String[] args) {
		
		System.out.println("服务器启动");
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			
			 // 实例化ServerSocket参数传入的是port值，只是之前在pub的CommonUse类中给的SERVER_PORT变量
			serverSocket = new ServerSocket(CommonUse.SERVER_PORT);       // 这个实例化ServerSocket千万不能放进循环因为这样会一直新给一个port值
			
			
			while(true) {         // 要进行很多次的Socket通讯所以要放到死循环里，相当于一直启动着服务器
				socket = serverSocket.accept();                           // 有人通讯就接收通讯ServerSocket的一个Socket
				
				ServerThread thread = new ServerThread(socket);           // 每有一个Socket就实例一个新线程
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

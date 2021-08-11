package com.qq.pub;

// CommonUse类中存着需要报文头对比时的变量，需要比对时就调用该类的变量，可以保证不会因为打错字而出错

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class CommonUse implements Serializable{
	
	public static final String FLAG="FLAG";
	
	public static final String REGISTER="REGISTER";                 // 注册功能标识符
	
	public static final String LOGIN="LOGIN";                       // 登录功能标识符
	
	public static final String ONLINE="ONLINE";                     // 上线功能标识符
	
	public static final String OFFLINE="OFFLINE";                   // 下线功能标识符
	
	public static final String FIND="FIND";                         // 查找功能标识符
	
	public static final String QQ_USER="QQ_USER";                   // 用户对象标识符
	
	public static final String SUCCESSFUL="SUCCESSFUL";             // 成功标识符
	
	public static final String FAILURE="FAILURE";                   // 失败标识符
	
	public static final String FRIENDS_INFO="FRIENDS_INFO";         // 对象集合标识符
	
	public static final String SERVER_IP = "127.0.0.1";             // ip值
	
	public static final int SERVER_PORT = 9999;                     // port值
	
	public static final String FIND_FRIEND="FIND_FRIEND";           // 查找好友功能标识符
	
    public static final String UDP_PACKET_SYMBOL = "\n\r\n\r";      // 复杂间隔符
	
	public static final String MESSAGE = "MESSAGE";                 // 消息标识符

	
	// 获得当前类的绝对路径，在设置窗口背景时和背景图片路径配合使用
    public static String getSystempath() {
        File f = new File("");
        return f.getAbsolutePath();
    }
}
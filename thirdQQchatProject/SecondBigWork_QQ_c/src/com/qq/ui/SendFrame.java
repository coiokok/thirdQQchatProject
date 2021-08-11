package com.qq.ui;

// 发送消息的页面

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.qq.bean.Qquser;
import com.qq.pub.CommonUse;
import com.qq.pub.UdpSocket;

public class SendFrame extends JFrame implements ActionListener {
	private Qquser sender = null;
	private Qquser receiver = null;
	
	private JPanel topPanel;
    private JPanel bottomPanel;
    
    private JLabel receiverLabel = null;
   	private JTextField receiverField = null;
   	private JLabel sendercontentLabel = null;
    private JTextArea sendArea;
    
    private JButton sendButton;
    private JButton closeButton;
    
    private void init() {
    	JPanel bodyPanel = (JPanel)this.getContentPane();
		bodyPanel.setLayout(new BorderLayout());
		
		this.topPanel = new JPanel();
		this.bottomPanel = new JPanel();
		bodyPanel.add(this.topPanel, BorderLayout.CENTER);
		bodyPanel.add(this.bottomPanel, BorderLayout.SOUTH);
		
		// 发送框的第一行，显示发送给谁
		this.receiverLabel = new JLabel("接收者：");
        this.receiverField = new JTextField();
        this.receiverField.setPreferredSize(new Dimension(400, 25));
        this.receiverField.setEditable(false);                     // 设置文本框不可修改
        this.receiverField.setText(this.receiver.getName());
        
        // 发送框的发送消息区域
        this.sendercontentLabel = new JLabel("发送内容：");
        this.sendArea = new JTextArea();
        this.sendArea.setPreferredSize(new Dimension(400, 130));
        JScrollPane sendScrollPane = new JScrollPane(this.sendArea);
        
        Box box = Box.createVerticalBox();
        
        Box box1 = Box.createHorizontalBox();
        Box box2 = Box.createHorizontalBox();
        
        box1.add(this.receiverLabel);
        box1.add(this.receiverField);
        
        box2.add(this.sendercontentLabel);
        box2.add(sendScrollPane);
        
        box.add(Box.createVerticalStrut(10));
        box.add(box1);
        box.add(Box.createVerticalStrut(10)); 
        box.add(box2);
        
        this.topPanel.add(box);
        
        // 底部按钮部分
        this.bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        this.sendButton = new JButton("发送");
        this.sendButton.addActionListener(this);
        this.closeButton = new JButton("关闭");
        this.closeButton.addActionListener(this);
        this.bottomPanel.add(this.sendButton);
        this.bottomPanel.add(this.closeButton);
        
        this.setTitle(this.sender.getName() + "的发送框");
        this.setBounds(690, 400, 500, 280);
    }
    
    public SendFrame() {
		// TODO Auto-generated constructor stub
	}
    
    // 该构造方法传入的第一个参数是该用户的全部信息对象，第二个参数是接收方对象，这个构造函数会在主页面也就是好友列表页面被使用，点击哪个好友，哪个好友的对象就会被作为参数传入
    public SendFrame(Qquser sender, Qquser receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 点击发送按钮时
		if(e.getSource() == this.sendButton) {
			String message = this.sendArea.getText();
			// 这里调用的就是UdpSocket发送信息时建立UDP传输所要用的构造方法，通过接收者的ip和port值发送给接收者
			UdpSocket sendSocket = new UdpSocket(this.receiver.getIp(), Integer.parseInt(this.receiver.getPort()));
			// 这个报文udpInfo由 报文头MESSAGE标记、发送者的用户名、发送的信息 这三部分组成
			String udpInfo = CommonUse.MESSAGE + CommonUse.UDP_PACKET_SYMBOL
					+ this.sender.getAccount() + CommonUse.UDP_PACKET_SYMBOL
					+ message + CommonUse.UDP_PACKET_SYMBOL;
			// 再调用其中的send方法发送报文
			sendSocket.send(udpInfo);
			this.dispose();
		}
		// 点击关闭按钮时
		else if (e.getSource() == this.closeButton) {
			this.dispose();
		}
	}
}

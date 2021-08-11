package com.qq.ui;

// 接收消息的页面

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

public class ReceiveFrame extends JFrame implements ActionListener {
	private String message = null;
	private Qquser sender = null;
	private Qquser receiver = null;
	
	private JPanel topPanel;
    private JPanel bottomPanel;
    
    private JLabel fromLabel = null;
   	private JTextField fromField = null;
   	private JLabel contentLabel = null;
    private JTextArea contentArea;
    
    private JButton repeatButton;
    private JButton closeButton;
    
    private void init() {
    	JPanel bodyPanel = (JPanel)this.getContentPane();
		bodyPanel.setLayout(new BorderLayout());
		
		this.topPanel = new JPanel();
		this.bottomPanel = new JPanel();
		bodyPanel.add(this.topPanel, BorderLayout.CENTER);
		bodyPanel.add(this.bottomPanel, BorderLayout.SOUTH);
		
		// 接收框的第一行，显示谁发来的
		this.fromLabel = new JLabel("来自于：");
        this.fromField = new JTextField();
        this.fromField.setPreferredSize(new Dimension(400, 25));
        this.fromField.setEditable(false);
        this.fromField.setText(this.receiver.getName());
        
        // 接收框的接收消息区域
        this.contentLabel = new JLabel("接收内容：");
        this.contentArea = new JTextArea();
        this.contentArea.setPreferredSize(new Dimension(400, 130));
        this.contentArea.setText(this.message);
        this.contentArea.setEditable(false);
        JScrollPane contentScrollPane = new JScrollPane(this.contentArea);
        
        Box box = Box.createVerticalBox();
        
        Box box1 = Box.createHorizontalBox();
        Box box2 = Box.createHorizontalBox();
        
        box1.add(this.fromLabel);
        box1.add(this.fromField);
        
        box2.add(this.contentLabel);
        box2.add(contentScrollPane);
        
        box.add(Box.createVerticalStrut(10));
        box.add(box1);
        box.add(Box.createVerticalStrut(10)); 
        box.add(box2);
        
        this.topPanel.add(box);
        
        this.bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        this.repeatButton = new JButton("回复");
        this.repeatButton.addActionListener(this);
        this.closeButton = new JButton("关闭");
        this.closeButton.addActionListener(this);
        this.bottomPanel.add(this.repeatButton);
        this.bottomPanel.add(this.closeButton);
        
        this.setTitle(this.sender.getName() + "的接收框");
        this.setBounds(690, 400, 500, 280);
    }
    
    public ReceiveFrame() {
		// TODO Auto-generated constructor stub
	}
    
    // 该构造函数是获取发送消息的对象的信息，在MainFrame代码中当接收到的报文头是MESSAGE标记时，则将现在这个接收到信息的用户作为第一个参数sender传入、发来信息的对象作为第二个参数receiver传入、接收到的信息作为第三个参数message传入，这个逻辑关系大家一定要弄清
    public ReceiveFrame(Qquser sender, Qquser receiver, String message) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 当点击的是回复按钮时
		if(e.getSource() == this.repeatButton) {
			// 就实例化发送框，将刚接收框的构造方法第一个参数本用户作为sender发出去，然后发给刚给我们发消息的好友对象receiver（这个和刚才ReceiveFrame的构造方法是对应的，大家一定要想清楚这块，这个在各自的MainFrame接收到消息后角度就转换了）
			SendFrame sendFrame1 = new SendFrame(this.sender, this.receiver);
			sendFrame1.setVisible(true);
			this.dispose();
		}
		// 当点击的是关闭按钮时
		else if (e.getSource() == this.closeButton) {
			this.dispose();
		}
	}
}

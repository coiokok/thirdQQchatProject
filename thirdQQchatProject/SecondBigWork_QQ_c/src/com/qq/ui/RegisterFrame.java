package com.qq.ui;

// 用户进行注册的页面

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.qq.bean.Qquser;
import com.qq.component.ImgPanel;
import com.qq.pub.CommonUse;
import com.qq.pub.TcpMessage;
import com.qq.pub.TcpSocket;

public class RegisterFrame extends JFrame implements ActionListener {
	private TcpSocket tcpSocket = null;
	
	private JPanel bodyPanel = null;
	private JPanel centerPanel = null;
    private JPanel bottomPanel = null;
    
    private JLabel accountLabel = null;
    private JLabel nameLabel = null;
    private JLabel passwordLabel = null;
    private JLabel repasswordLabel = null;
    private JLabel imgLabel = null;
    
    private JTextField accountTextField = null;
    private JTextField nameTextField = null;
    private JPasswordField passwordField = null;
    private JPasswordField repasswordField = null;
    private JComboBox imgComboBox = null;
    
    private JButton registerButton = null;
    private JButton cancelButton = null;
    
    // 自动添加头像方法
    private void addHead() {
    	File[] files = new File("./selectimg").listFiles();       // 使用File的listFiles()方法获得该文件夹下的所有文件
		for (File file : files) {            // 遍历集合
			this.imgComboBox.addItem(new ImageIcon(file.getAbsolutePath()));        // 将每个图片文件的绝对定位通过getAbsolutePath()方法传入ImageIcon实例化并addItem给头像选择下拉列表
			}
    }
    
    // 中间输入信息界面的代码
    private void initCenter() {
    	// 通过ImgPanel的构造方法实例化panel背景
    	this.centerPanel = new ImgPanel("./register.png");
    	
    	this.accountLabel = new JLabel("账         号：", JLabel.RIGHT);
		this.accountLabel.setPreferredSize(new Dimension(70, 24));       // .setPreferredSize(new Dimension(宽, 高))    设置panel大小方法但是会随着界面大小变化而变化
    	this.nameLabel = new JLabel("昵         称：", JLabel.RIGHT);
		this.nameLabel.setPreferredSize(new Dimension(70, 24));
		this.passwordLabel = new JLabel("密         码：", JLabel.RIGHT);
		this.passwordLabel.setPreferredSize(new Dimension(70, 24));
		this.repasswordLabel = new JLabel("确认密码：", JLabel.RIGHT);
		this.repasswordLabel.setPreferredSize(new Dimension(70, 24));
		this.imgLabel = new JLabel("选择头像：", JLabel.RIGHT);
		this.imgLabel.setPreferredSize(new Dimension(70, 24));
		
		this.accountTextField = new JTextField();
	    this.accountTextField.setPreferredSize(new Dimension(160, 24));
		this.nameTextField = new JTextField();
		this.nameTextField.setPreferredSize(new Dimension(160, 24));
		this.passwordField = new JPasswordField();
		this.passwordField.setPreferredSize(new Dimension(160, 24));
		this.repasswordField = new JPasswordField();
		this.repasswordField.setPreferredSize(new Dimension(160, 24));
		this.imgComboBox = new JComboBox();
		
		this.addHead();                 // addHead()方法是上面写的自动添加头像方法
		
		// Box布局管理器，需要五个横着panel时需要声明一个大的竖着的box包裹住五个横着的box
		Box box0 = Box.createVerticalBox();              // .createVerticalBox()创建竖着的box方法
		Box box1 = Box.createHorizontalBox();            // .createHorizontalBox()创建横着的box方法
		Box box2 = Box.createHorizontalBox();
		Box box3 = Box.createHorizontalBox();
		Box box4 = Box.createHorizontalBox();
		Box box5 = Box.createHorizontalBox();
		
		box1.add(this.accountLabel);
		box1.add(this.accountTextField);
		
		box2.add(this.nameLabel);
		box2.add(this.nameTextField);
		
		box3.add(this.passwordLabel);
		box3.add(this.passwordField);
		
		box4.add(this.repasswordLabel);
		box4.add(this.repasswordField);
		
		box5.add(this.imgLabel);
		box5.add(this.imgComboBox);
		
		box0.add(Box.createVerticalStrut(90));           // .createVerticalStrut(间距)设置和上一个box的间距
		box0.add(box1);
		box0.add(Box.createVerticalStrut(10));
		box0.add(box2);
		box0.add(Box.createVerticalStrut(10));
		box0.add(box3);
		box0.add(Box.createVerticalStrut(10));
		box0.add(box4);
		box0.add(Box.createVerticalStrut(10));
		box0.add(box5);
		
		this.centerPanel.add(box0);
    }
    
    // 下方按钮panel的代码
    private void initBottom() {
    	this.bottomPanel = new JPanel();
    	
    	this.registerButton = new JButton("注 册");
    	this.registerButton.addActionListener(this);
    	this.cancelButton = new JButton("重 置");
    	this.cancelButton.addActionListener(this);
    	
    	this.bottomPanel.add(this.registerButton);
    	this.bottomPanel.add(this.cancelButton);
    }
    
    // 生成窗体的方法
    private void init() {
    	this.bodyPanel = (JPanel)this.getContentPane();
    	this.bodyPanel.setLayout(new BorderLayout());
    	
    	this.initCenter();
    	this.bodyPanel.add(this.centerPanel, BorderLayout.CENTER);
    
    	this.initBottom();
    	this.bodyPanel.add(this.bottomPanel, BorderLayout.SOUTH);
    	
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setTitle("QQ注册页面");
    }
    
    // 重写构造方法用TcpSocket构造方法实例化输入ip地址和port值完成与服务器连接
    public RegisterFrame() {
    	this.tcpSocket = new TcpSocket("127.0.0.1", CommonUse.SERVER_PORT);
    	this.init();
	}
   
    
    // 事件处理代码
	@Override
	public void actionPerformed(ActionEvent e) {
		// 如果点的是注册按钮
		if(e.getSource() == this.registerButton) {
			Qquser qquser = new Qquser();
			
			String account = this.accountTextField.getText();
			String name = this.nameTextField.getText();
			String password = new String(this.passwordField.getPassword());
			String repassword = new String(this.repasswordField.getPassword());
			
			// 获得头像图片的名字方法：先用lastIndexOf方法获得\的位置再获得.的位置然后再用substring方法取他们之间的头像图片的名字
			String head = this.imgComboBox.getSelectedItem().toString();
			int start = head.lastIndexOf("\\");
			int end = head.lastIndexOf(".");
			String headString = head.substring(start + 1, end);
			
			if(password.equals(repassword)) {
				// 给对象赋值
				qquser.setAccount(account);
				qquser.setName(name);
				qquser.setPassword(password);
				qquser.setState("0");
				qquser.setIp("0");
				qquser.setPort("0");
				qquser.setPic(headString);
				
				// 实例化报文类设置头和体，头为REGISTER，体为刚获得输入的qquser对象
				TcpMessage sMessage = new TcpMessage();
				
				sMessage.setHead(CommonUse.REGISTER);
				sMessage.setBody(CommonUse.QQ_USER, qquser);
				
				// 调用tcpSocket的submit方法将sMessage发送并接收返回的rMessage
				TcpMessage rMessage = this.tcpSocket.submit(sMessage);
				
				// 并通过报文头的内容判断是否注册成功
				String back = rMessage.getHead();
				if(CommonUse.SUCCESSFUL.equals(back)) {
					JOptionPane.showMessageDialog(this, "注册成功");
					this.dispose();
				} else {
					JOptionPane.showMessageDialog(this, "注册失败");
				}
			} else {
				JOptionPane.showMessageDialog(this, "两次密码输入不一致");
			}
		}
		// 如果点的是重置按钮
		else if(e.getSource() == this.cancelButton){
		    this.accountTextField.setText("");
			this.nameTextField.setText("");
			this.passwordField.setText("");
			this.repasswordField.setText("");
		}
	}

}

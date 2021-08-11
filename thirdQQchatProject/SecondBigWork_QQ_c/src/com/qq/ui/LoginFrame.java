package com.qq.ui;

// 用户进行登录的页面

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
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
import com.qq.pub.UdpSocket;

public class LoginFrame extends JFrame implements ActionListener {
	
	private TcpSocket tcpSocket = null;           // 实例化TcpSocket与服务器进行操作的对象
	private UdpSocket receiveSocket = null;       // 实例化UdpSocket与其他终端进行操作的对象
	
	private JPanel bodyPanel;                     // 所有界面panel，一个大body和中间的登录框centerPanel以及底部的按钮bottomPanel
	
	private JPanel centerPanel;
	private JPanel bottomPanel;
	
	private JLabel accountLabel;                  // 用户名和密码的输入panel
	private JTextField accountTextField;
	private JLabel passowrdLabel;
    private JPasswordField passwordField;
    
    private JButton loginButton;                  // 登录和注册按钮
    private JButton registerButton;
    
    // 中间的操作panel
    private void initCenter() {
    	this.centerPanel = new ImgPanel("./login.png");       // 实例化ImgPanel专门设背景的对象将登录页面的背景图片传入
    	
    	this.accountLabel = new JLabel("用 户 名：");
        this.accountTextField = new JTextField(16);
		this.accountLabel.setFont(new Font("微软雅黑", Font.BOLD, 17));        // setFont(new Font("字体", Font.样式, 字体大小))    设置字体样式的方法
        this.passowrdLabel = new JLabel("密     码：");
        this.passwordField = new JPasswordField(16);
		this.passowrdLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));        // setFont(new Font("字体", Font.样式, 字体大小))    设置字体样式的方法
        
        // 设置box布局管理器
        Box box0 = Box.createVerticalBox();
        Box box1 = Box.createHorizontalBox();
        Box box2 = Box.createHorizontalBox();
        
        box1.add(accountLabel);
        box1.add(accountTextField);
        
        box2.add(passowrdLabel);
        box2.add(passwordField);
        
        box0.add(Box.createVerticalStrut(90));
        box0.add(box1);
        box0.add(Box.createVerticalStrut(20));
        box0.add(box2);
        this.centerPanel.add(box0);
    }
    
    // 底部的按钮panel
    private void initBottom() {
    	this.bottomPanel = new JPanel();
    	
        this.loginButton = new JButton("登 陆");
        this.loginButton.addActionListener(this);
        this.bottomPanel.add(this.loginButton);
        
        this.registerButton = new JButton("注 册");
        this.registerButton.addActionListener(this);
        this.bottomPanel.add(this.registerButton); 
    }
    
    // 窗体代码
    private void init() {
    	this.bodyPanel = (JPanel)this.getContentPane();
    	this.bodyPanel.setLayout(new BorderLayout());
    	
    	this.initCenter();
    	this.bodyPanel.add(this.centerPanel, BorderLayout.CENTER);
    	
    	this.initBottom();
    	this.bodyPanel.add(this.bottomPanel, BorderLayout.SOUTH);
    	
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("QQ登录页面");
    }
    
    // 重写构造方法
    public LoginFrame() {
    	this.tcpSocket = new TcpSocket(CommonUse.SERVER_IP, CommonUse.SERVER_PORT);              // 通过TcpSocket类的构造方法来实例化请求服务器接收连接的socket，这里传入的参数是调用了CommonUse类中已经存好的变量，只会要用到这些属性都要调用CommonUse类中的变量，就能避免因为手抖写错的情况
    	this.receiveSocket = new UdpSocket();                // 实例化receiveSocket与终端之间进行传输
    	this.init();
	}
   
    // 主方法运行main方法
	public static void main(String[] args) {
		LoginFrame loginFrame1 = new LoginFrame();
		loginFrame1.setBounds(680, 340, 500, 320);
		loginFrame1.setVisible(true);
	}
	
	
	// 事件处理代码
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// 点击的是登录按钮时执行的操作
		if(e.getSource() == this.loginButton) {
			Qquser loginUser = new Qquser();            // 实例化登录的qquser
			
			String account = this.accountTextField.getText();          // 获得用户名以及密码的输入框内的信息
			String password = new String(this.passwordField.getPassword());
			
			// 将输入的信息设置给登录对象loginUser
			loginUser.setAccount(account);
			loginUser.setPassword(password);
			loginUser.setIp(this.tcpSocket.getIp());
			loginUser.setPort(String.valueOf(this.receiveSocket.getPort()));             // 调用receiveSocket中的getPort方法获得该终端的port值
			
			TcpMessage sMessage = new TcpMessage();                   // 实例化sMessage发送信息对象
			sMessage.setHead(CommonUse.LOGIN);                        // 将sMessage报文头设为登录
			sMessage.setBody(CommonUse.QQ_USER, loginUser);           // 将sMessage报文体传入登录的用户loginUser
			
			TcpMessage rMessage = this.tcpSocket.submit(sMessage);    // 调用tcpSocket里的submit方法就是将sMessage传过去服务器并等待处理完成然后接受返回的rMessage值
			
			String back = rMessage.getHead();                         // 获得传回的rMessage报文头赋值给back
			// 返回的报文头是成功时
			if(CommonUse.SUCCESSFUL.equals(back)) {
				JOptionPane.showMessageDialog(this, "登录成功");
				Qquser fullUser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);      // 获得传回的rMessage里的报文体也就是用户的全部信息fullUser对象
				
				MainFrame mainFrame = new MainFrame(fullUser, this.tcpSocket, this.receiveSocket);      // 实例化MainFrame用户列表界面，并将用户全部信息fullUser对象以及fullUser传入
				                                                                    // 如果不传入这个socket的话那该用户的下个界面将不能继续该用户的通讯
				mainFrame.setVisible(true); 
				this.dispose();                 // 关闭登录页面
			} 
			// 返回的报文头是失败时
			else if(CommonUse.FAILURE.equals(back)) {
				JOptionPane.showMessageDialog(this, "登录失败，用户名或者密码有误");
			}
		}
		
		// 点击的是注册按钮时执行的操作
		else if(e.getSource() == this.registerButton){
			RegisterFrame registerFrame = new RegisterFrame();
			registerFrame.setBounds(700, 320, 512, 390);
			registerFrame.setVisible(true);
		}
	}

}

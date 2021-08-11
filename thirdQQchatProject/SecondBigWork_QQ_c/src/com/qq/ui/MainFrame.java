package com.qq.ui;

// 好友列表页面
// 实现UdpListener接口方法来通过观察者实现事件，当有不同报文接收到时执行不同操作
// 实现MouseListener接口中的鼠标点击方法，当左键双击好友时就会实例化发送消息窗体

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.qq.bean.Qquser;
import com.qq.component.ClinetImgCell;
import com.qq.listener.UdpListener;
import com.qq.listener.UdpThread;
import com.qq.pub.CommonUse;
import com.qq.pub.TcpMessage;
import com.qq.pub.TcpSocket;
import com.qq.pub.UdpSocket;

public class MainFrame extends JFrame implements UdpListener, MouseListener {
	private TcpSocket tcpSocket = null;
	private UdpSocket receiveSocket = null;
	
	private Qquser fullUser = null;                   // 用户的全部信息fullUser对象，登录页面在实例化好友列表页面时会作为参数传入
	private List<Qquser> friends = null;              // 该用户的全部好友对象的集合friends
	
	private JLabel userLabel = null;                  // 该用户名的顶部panel
	
	private DefaultListModel<Qquser> listModel = null;     // 显示好友的DefaultListModel这个像是下拉列表，但是是直接展开的
	private JList<Qquser> friendList = null;               // 放入好友信息的集合
	
	private JPanel bodyPanel = null;
	
	
	// 重写构造方法
	public MainFrame() {
		// TODO Auto-generated constructor stub
	}
	public MainFrame(Qquser fullUser, TcpSocket tcpSocket, UdpSocket receiveSocket) {    // 参数传入登录页面的用户的全部信息fullUser对象，以及与服务器进行通讯的tcpSocket，以及与各好友终端进行通讯的udpSocket
		this.fullUser = fullUser;
		this.tcpSocket = tcpSocket;
		this.receiveSocket = receiveSocket;
		this.init();
	}
	
	
	// getFriends方法获得该用户的所有朋友对象集合
	private List<Qquser> getFriends() {
		List<Qquser> list = null;
		
		TcpMessage sMessage = new TcpMessage();                            // 实例化发送信息的对象sMessage
		sMessage.setHead(CommonUse.FIND);                                  // 将该sMessage的报文头设为查找
		sMessage.setBody(CommonUse.QQ_USER, this.fullUser);                // 将该sMessage的报文体设为用户的全部信息
		
		TcpMessage rMessage = this.tcpSocket.submit(sMessage);             // 返回等服务器处理完的rMessage，这里rMessage里是该用户所有朋友对象的集合friends
		
		list = (List<Qquser>)rMessage.getBody(CommonUse.FRIENDS_INFO);     // 将该报文体list返回出去
		return list;
	}
	
	
	// createFriendList生成好友列表方法
	private void createFriendList() {
		List<Qquser> beforeList = this.getFriends();                       // beforeList用来获得所有好友对象集合
		
		List<Qquser> afterList = new ArrayList<Qquser>();                  // afterList用来将在线的好友放在前头的进行排序的集合
		
		// 生成头像图片，遍历好友集合，将State为1的也就是在线的好友的头像设置为彩色，State为0的也就是不在线的好友的头像设置为灰色
		for (Qquser tempUser : beforeList) {
			if("1".equals(tempUser.getState())) {
				tempUser.setPlace1("./onimg/" + tempUser.getPic() + ".png");
			} else if("0".equals(tempUser.getState())) {
				tempUser.setPlace1("./outimg/" + tempUser.getPic() + ".png");
			}
		}
		
		// 将在线的好友放在列表前面的排序，原理是放入afterList的顺序，先放在线的后放不在线的即可
		for (Qquser tempUser : beforeList) {
			if("1".equals(tempUser.getState())) {
				afterList.add(tempUser);
			}
		}
		for (Qquser tempUser : beforeList) {
			if("0".equals(tempUser.getState())) {
				afterList.add(tempUser);
			}
		}
		
		this.friends = afterList;                            // friends是之后用于生成好友列表的集合，将排好序的好友列表集合赋值给他
		
		this.listModel = new DefaultListModel<Qquser>();     // 实例化显示好友信息的DefaultListModel列表
		
		for (Qquser tempUser : this.friends) {               // 遍历并给listModel添加所有的好友信息
			this.listModel.addElement(tempUser);
		}
		
		this.friendList = new JList<>();
		this.friendList.addMouseListener(this);                     // 给好友列表的每个好友都添加鼠标点击事件
		this.friendList.setCellRenderer(new ClinetImgCell());       // List的.setCellRenderer()方法 是可以设置以某种形式设置对象加进List，参数传入的必须要是一个实现ListCellRenderer接口的类
		this.friendList.setModel(this.listModel);
	}
	
	
	// refreshFriendList刷新好友列表方法
	private void refreshFriendList() {
		// 这里用生成好友列表里的friends集合即可，所以就不用再实例化一个beforeList了
		List<Qquser> afterList = new ArrayList<Qquser>();
		
		// 生成头像图片，遍历好友集合，将State为1的也就是在线的好友的头像设置为彩色，State为0的也就是不在线的好友的头像设置为灰色
		for (Qquser tempUser : this.friends) {
			if("1".equals(tempUser.getState())) {
				tempUser.setPlace1("./onimg/" + tempUser.getPic() + ".png");
			} else if("0".equals(tempUser.getState())) {
				tempUser.setPlace1("./outimg/" + tempUser.getPic() + ".png");
			}
		}
		
		// 遍历friends集合将在线的好友放在列表前面的排序，原理是放入afterList的顺序，先放在线的后放不在线的即可
		for (Qquser tempUser : this.friends) {
			if("1".equals(tempUser.getState())) {
				afterList.add(tempUser);
			}
		}
		for (Qquser tempUser : this.friends) {
			if("0".equals(tempUser.getState())) {
				afterList.add(tempUser);
			}
		}
		
		// 将排好序的afterList给friends集合去实例化好友列表DefaultListModel
		this.friends = afterList;
		
		this.listModel = new DefaultListModel<Qquser>();
		for (Qquser tempUser : this.friends) {
			this.listModel.addElement(tempUser);
		}
		this.friendList.setModel(this.listModel);
		
	}
	
	
	// init主页面方法
	private void init() {
		this.bodyPanel = (JPanel)this.getContentPane();
		this.bodyPanel.setLayout(new BorderLayout());
		
		this.userLabel = new JLabel(this.fullUser.getName());      // 获得该用户的昵称
		this.bodyPanel.add(this.userLabel, BorderLayout.NORTH);
		
		this.createFriendList();                                   // 调用createFriendList显示所有好友的方法
		this.bodyPanel.add(this.friendList, BorderLayout.CENTER);
		
		UdpThread udpThread = new UdpThread(this.receiveSocket);   // 实例化UdpThread线程将接收信息的receiveSocket传入就可以防止阻塞
		udpThread.addUdpListener(this);                            // 再将本类传入给addUdpListener方法，当有信息通讯过来时线程就会实现事件
		udpThread.start();
		
		this.setTitle(this.fullUser.getAccount() + "的好友列表");
		this.setBounds(1625, 0, 300, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	// 实现事件触发后的exectue方法，获取报文头并执行操作
	@Override
	public void exectue(String udpInfo) {
		
		// 将获得的报文以CommonUse.UDP_PACKET_SYMBOL分隔符进行切割，然后存给infos字符串数组
		String[] infos = udpInfo.split(CommonUse.UDP_PACKET_SYMBOL);
		
		// 其中infos[0]就是报文头
		String head = infos[0];
		
		
		// 如果报文头是在线的时候
		if(head.equals(CommonUse.ONLINE)) {             
			
			// 获取所有传过来的查找到的在线好友的信息
			String account = infos[1];
			String ip = infos[2];
			String port = infos[3];
			
			// 遍历该用户的所有好友，用户名相同的就把该好友对象的在线设为1，ip地址以及port值都设为真正的通过UdpSocket获取到的值
			for(Qquser tempUser : this.friends) {
				if(tempUser.getAccount().equals(account)) {
					tempUser.setState("1");
					tempUser.setIp(ip);
					tempUser.setPort(port);
					break;
				}
			}
			// 当有用户上线了也调用刷新好友列表方法让好友头像点亮
			this.refreshFriendList();
		} 
		
		
		// 如果报文头是离线的时候
		else if (head.equals(CommonUse.OFFLINE)) {
			String account = infos[1];             // 获取下线用户的用户名
			for (Qquser tempUser : this.friends) { // 遍历好友列表里的对象，将该对象的在线值，ip值以及port值清零
				if(account.equals(tempUser.getAccount())) {
					tempUser.setState("0");
					tempUser.setIp("0");
					tempUser.setPort("0");
					break;
				}
			}
			// 当有用户离线了也调用刷新好友列表方法让好友头像变灰
			this.refreshFriendList();
		} 
		
		
		// 如果报文头是有消息的时候，就是有别个用户给该用户发消息的时候，返回的就是这个报文头
		else if(head.equals(CommonUse.MESSAGE)) {
			Qquser receiver = null;
			String account = infos[1];
			String message = infos[2];
			// 遍历好友列表根据账号判断发送信息的用户是哪个好友
			for (Qquser tempUser : this.friends) {
				if(account.equals(tempUser.getAccount())) {
					receiver = tempUser;
					break;
				}
			}
			// 实例化接收消息的窗体
			ReceiveFrame receiveFrame = new ReceiveFrame(this.fullUser, receiver, message);
			receiveFrame.setVisible(true);
		}
	}

	
	// 当左键双击好友时就实例化发送消息的窗体，这后面都是MouseListener接口的实现方法，我们只用的上这个点击事件，所以只重写这个方法
	@Override
	public void mouseClicked(MouseEvent e) {
		if(1 == e.getButton() && 2 == e.getClickCount()) {
			Qquser receiver = this.friendList.getSelectedValue();
			SendFrame sendFrame = new SendFrame(this.fullUser, receiver);
			sendFrame.setVisible(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

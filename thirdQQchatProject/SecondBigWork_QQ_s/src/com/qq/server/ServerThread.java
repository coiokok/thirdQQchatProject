package com.qq.server;

// 供Server服务器实例化多线程终端时的类，每有一个用户登录，Server就会实例化一个ServerThread线程类，然后实例化时参数传入通讯的socket实现网络传输
// 然后ServerThread里的socket会传递信息（报文）给每个页面，然后每个页面根据传输的报文进行判断然后执行操作

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import com.qq.bean.Qquser;
import com.qq.dao.IQqUserDao;
import com.qq.dao.QqUserDaoImpl;
import com.qq.pub.CommonUse;
import com.qq.pub.TcpMessage;
import com.qq.pub.UdpSocket;

public class ServerThread extends Thread {
	private Qquser fullUser = null;                      // 接收登录用户完整信息的类
	
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	
	// onLine上线方法，查看所有该上线用户的在线的好友，并给所有在线的好友发送udpInfo报文以便之后的通讯
	private void onLine(Qquser qquser) {
		IQqUserDao dao = new QqUserDaoImpl();
		List<Qquser> list = dao.findBySql("select * from qquser where account"
				+ " in (select friendAccount from friend where userAccount = '" + qquser.getAccount() + "') and state = '1'");
		
		// udpInfo报文，报文头是在线，体是用户名，ip以及port值，都用复杂的断点CommonUse.UDP_PACKET_SYMBOL进行分割
		String udpInfo = CommonUse.ONLINE + CommonUse.UDP_PACKET_SYMBOL
				+ qquser.getAccount() + CommonUse.UDP_PACKET_SYMBOL
				+ qquser.getIp() + CommonUse.UDP_PACKET_SYMBOL
				+ qquser.getPort() + CommonUse.UDP_PACKET_SYMBOL;
		
		// 遍历查到的所有在线好友并用UdpSocket实例化，并传入ip值以及port值，这样就可以之后发送消息
		for (Qquser tempUser : list) {
			UdpSocket sendSocket = new UdpSocket(tempUser.getIp(), Integer.parseInt(tempUser.getPort()));         // Integer.parseInt() 将字符串转换为int型的方法
			sendSocket.send(udpInfo);                           // 调用UdpSocket的send方法将udpInfo报文发送过去
		}
	}
	
	
	
	// offLine下线方法，查看所有该准备下线用户的在线的好友，然后将ip和port值清零传输过去，在其他用户那里看该用户的头像就会变灰
	private void offLine(Qquser qquser) {
		IQqUserDao dao = new QqUserDaoImpl();
		List<Qquser> list = dao.findBySql("select * from qquser where account"
				+ " in (select friendAccount from friend where userAccount = '" + qquser.getAccount() + "') and state = '1'");
		String udpInfo = CommonUse.OFFLINE + CommonUse.UDP_PACKET_SYMBOL
				+ qquser.getAccount() + CommonUse.UDP_PACKET_SYMBOL;
		for (Qquser tempUser : list) {
			UdpSocket sendSocket = new UdpSocket(tempUser.getIp(), Integer.parseInt(tempUser.getPort()));
			sendSocket.send(udpInfo);
		}
	}
	
	
	
	// findFriends方法是通过登录用户的账号在朋友关系表里查找该用户的所有朋友的账号，再把这些所有朋友的账号在用户信息表里查他们的所有信息后放入List集合
	private List<Qquser> findFriends(Qquser qquser){     
		IQqUserDao dao = new QqUserDaoImpl();
		List<Qquser> list = dao.findBySql("select * from qquser where account in (select friendAccount from friend where userAccount = '" + qquser.getAccount() + "')");     // 调用dao里的findBySql根据传入的sql语句进行查询
		return list;
	}
	
	
	
	// findFullUser方法是通过用dao里的findById方法将登录用户的账号传进去返回该账号的所有信息对象
	private Qquser findFullUser(String account) {       
		return new QqUserDaoImpl().findById(account);
	}
	
	
	
	// midifyDB方法是将登录的用户传进去然后用dao中的update方法将数据库中的State状态信息改为1，ip和port值都做更改，实现用户的上线
	private void midifyDB(Qquser qquser) {              
		qquser.setState("1");
		new QqUserDaoImpl().update(qquser);
	}
	
	
	
	// registe用户注册方法调用IQqUserDao的save注册功能
	public boolean registe(Qquser qquser) {
		IQqUserDao dao = new QqUserDaoImpl();
		int n = dao.save(qquser);
		if(n > 0) {
			return true;
		}
		return false;
	}
	
	
	
	// checkUser方法传入用户登录时输入的账户密码去数据库查找是否有该用户并且判断密码是否正确
	public boolean checkUser(String id, String pass) {  
		IQqUserDao dao = new QqUserDaoImpl();
		
		Qquser qquser = dao.findById(id);               // dao中的findById是将id传入然后返回在数据库中查找到的对象给qquser
		
		if(qquser != null && qquser.getPassword().equals(pass)) {       // 如果返回了qquser也就是qquser != null并且返回的qquser的密码和用户输入的一样则登录成功返回true
			return true;
		}
		return false;
	}
	
	
	
	// 重写构造方法
	public ServerThread() {
		// TODO Auto-generated constructor stub
	}
	public ServerThread(Socket socket) {
		System.out.println("有一个客户登录了");
		this.socket = socket;
		try {
			// 跟传入的socket对象进行对象流传输
			this.in = new ObjectInputStream(this.socket.getInputStream());
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// 重写线程的run方法
	@Override
	public void run() {
		TcpMessage rMessage = null;
		try {
			while(true) {
				
				rMessage = (TcpMessage)this.in.readObject();
				String head = rMessage.getHead();                    // 获得读取的信息rMessage报文头
				
				
				// 如果获取的报文头是注册时
				if(CommonUse.REGISTER.equals(head)) {
					TcpMessage sMessage = new TcpMessage();          // 实例化进行发送信息对象sMessage
					Qquser registeUser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);        // 获得报文体给registeUser对象
					// 调用注册方法传入要注册的registeUser对象
					if(this.registe(registeUser)) {
						sMessage.setHead(CommonUse.SUCCESSFUL);      // 注册成功了就将成功传给发送信息对象sMessage
					} else {
						sMessage.setHead(CommonUse.FAILURE);         // 注册失败了就将失败传给发送信息对象sMessage
					}
					this.out.writeObject(sMessage);                  // 用对象流的书写方法将sMessage传输过去
					this.out.flush();
				} 
				
				
				// 如果获取的报文头是登录时
				else if(CommonUse.LOGIN.equals(head)) {
					TcpMessage sMessage = new TcpMessage();          // 实例化进行发送信息对象sMessage
					Qquser loginUser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);         // 获得报文体给loginUser对象
					if(this.checkUser(loginUser.getAccount(), loginUser.getPassword())) {   // 调用上面的checkUser方法将loginUser的账号以及密码传进去进行登录判断
						
						// 登录成功后调用上面的midifyDB方法将登录对象的数据库信息进行修改为在线状态
						this.midifyDB(loginUser);
						
						// 调用上面的findFullUser方法获得登录用户的全部信息
						this.fullUser = this.findFullUser(loginUser.getAccount());
						
						// 将发送信息对象sMessage报文头设为成功并将登录用户的所有信息对象设为报文体
						this.onLine(this.fullUser);                          // 调用online方法实例化所有在线的好友并用UdpSocket发送udpInfo报文以便之后的通讯
						
						sMessage.setHead(CommonUse.SUCCESSFUL);
						sMessage.setBody(CommonUse.QQ_USER, this.fullUser);
					} else {
						sMessage.setHead(CommonUse.FAILURE);
					}
					this.out.writeObject(sMessage);                  // 用对象流的书写方法将sMessage传输过去
					this.out.flush();
				} 
				
				
				// 如果获取的报文头是查找时
				else if(CommonUse.FIND.equals(head)) {
					TcpMessage sMessage = new TcpMessage();          // 实例化进行发送信息对象sMessage
					
					Qquser mainUser = (Qquser)rMessage.getBody(CommonUse.QQ_USER);         // 获得报文体给mainUser对象
					
					List<Qquser> friends = this.findFriends(mainUser);     // 调用上面的findFriends方法返回所有朋友对象的集合friends
					sMessage.setBody(CommonUse.FRIENDS_INFO, friends);     // 将对象集合friends设为报文体
					
					this.out.writeObject(sMessage);                  // 用对象流的书写方法将sMessage传输过去
					this.out.flush();
				}
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// 当有一个客户端关闭时
			System.out.println("有一个客户下线了");
			// 当窗口被关闭时，将该对象的在线值，ip值以及port值设为0
			this.fullUser.setState("0");
			this.fullUser.setIp("0");
			this.fullUser.setPort("0");
			// 调用更新数据库方法将数据传给数据库
			new QqUserDaoImpl().update(this.fullUser);
			// 然后将数据以及清零的该对象类进行传输
			this.offLine(this.fullUser);
		}
		
	}

}

package com.qq.component;

// 背景panel类，通过重写构造方法可以实现在实例化时传入图片路径直接就可以设置为panel背景

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.qq.pub.CommonUse;

public class ImgPanel extends JPanel {
	private ImageIcon img = null;
	
	public ImgPanel() {
		// TODO Auto-generated constructor stub
	}
	// 重写构造方法传入图片路径再用ImageIcon实例化，然后再通过paintComponent实例化图片背景
	public ImgPanel(String imgPath) {
		this.img = new ImageIcon(CommonUse.getSystempath() + imgPath );
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(this.img.getImage(), 0, 0, this);
	}
	
}

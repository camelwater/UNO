package com.uno;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Frame
{
	
	private double scrWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private double scrHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight(); 
	
//	private double wFactor = scrWidth/1920;
//	private double hFactor = scrHeight/1080;
	
	public Frame()
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				JFrame frame = new JFrame("UNO");
				
				frame.setSize((int)scrWidth, (int)scrHeight);
				System.out.println("Screen Dimension: "+scrWidth+" x "+scrHeight);
				frame.setResizable(true);
				frame.setAutoRequestFocus(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				frame.setVisible(true);	
				frame.toFront();
				
				
				try {
					frame.setIconImage(ImageIO.read(getClass().getResource("/card_back_large.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.add(new UnoGraphics(frame));
			}
		});
			
	}
}

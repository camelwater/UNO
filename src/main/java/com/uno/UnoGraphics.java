package com.uno;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.synth.SynthSeparatorUI;

import java.util.*;

public class UnoGraphics extends JPanel implements MouseListener, MouseMotionListener
{
	JFrame frame;
	Board game;
	GameState gamestate;
	
	private double scrWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private double scrHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight(); 
	
	private double wFactor = scrWidth/1920;
	private double hFactor = scrHeight/1080;
	
	private int cardHeight = 180;
	private int cardWidth = 130;
	private int page = 1;
	private int page2 = 1;
	private int page3 = 1;
	private int page4 = 1;
	
	private int maxPage = 0;
	private int maxPage2 = 0;
	private int maxPage3 = 0;
	private int maxPage4 = 0;
	
	private boolean colorPickerDraw = false;
	private boolean colorPickerPlay = false;
	private int colorPickerPlayCoord = 517;
	private int wildIndex = 0;
	
	private boolean start = true;
	private boolean enterNames = false;
	private boolean cpu = false;
	private boolean arrows = true;
	private boolean gameEndedBack = false;
	private boolean menu = false;
	
	private boolean hovering1 = false;
	private boolean hovering2 = false;
	private boolean hovering3 = false;
	private boolean hovering4 = false;
	private boolean confirm = false;
	
	JTextField nameText = new JTextField(20);
	
	private ArrayList<String> names = new ArrayList<String>();
	private Queue<String> history = new LinkedList<String>();
	
	AbstractAction action = new AbstractAction()
	{
	    //@Override
	    public void actionPerformed(ActionEvent e)
	    {
	    	if(enterNames)
	    	{
	    		if(names.size()<4 && nameText.getText().replace(" ", "").length()>0)
		    		names.add(nameText.getText());
		    		nameText.setText("");
		    		repaint();
	    	}
	    }
	};
	
	
	public UnoGraphics(Board game, GameState gamestate) 
	{
		frame = new JFrame("UNO");
		this.game = game;
		this.gamestate = gamestate;
		
		setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		try {
			frame.setIconImage(ImageIO.read(getClass().getResource("/card_back_large.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.add(this);
		frame.setSize((int)scrWidth, (int)scrHeight);
		System.out.println("Screen Dimension: "+scrWidth+" x "+scrHeight);
		frame.setResizable(true);
		frame.setAutoRequestFocus(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);	
		frame.toFront();
		
		
	}
	public void skipStart()
	{
		start = false;
	}
	public void setCPU(boolean x)
	{
		cpu = x;
	}
	public int x(int x)
	{
		x+=35;
		return (int)(x*wFactor);
	}
	public int font(int s)
	{
		return (int)(s*wFactor);
	}
	public int xs(int s)
	{
		return (int)(s*wFactor);
	}
	public int y(int y)
	{
		y-=25;
		return (int)(y*hFactor);
	}
	public int ys(int y)
	{
		return (int)(y*hFactor);
	}
	public void paintComponent(Graphics g)
	{
		if(start)
		{
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, xs(1920), ys(1080));
			g.setColor(Color.red);
			g.setFont(new Font("Trebuchet", Font.BOLD, font(75)));
			g.drawString("UNO", x(850), y(200));
			try {
				g.drawImage(ImageIO.read(getClass().getResource("/card_back_alt.png")), x(867), y(225), xs(cardWidth), ys(cardHeight), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			g.setColor(Color.white);
			g.setFont(new Font("Trebuchet", Font.BOLD, font(40)));
			g.fillRect(x(857),y(450), xs(150), ys(62));
			g.fillRect(x(857),y(530), xs(150), ys(62));
			g.fillRect(x(872),y(669), xs(120), ys(52));
			
			g.setColor(Color.black);
			g.drawRect(x(857),y(450), xs(150), ys(62));
			g.drawRect(x(857),y(530), xs(150), ys(62));
			g.drawRect(x(872),y(669), xs(120), ys(52));
			g.drawRect(x(857)+1,y(450)+1, xs(150)-2, ys(62)-2);
			g.drawRect(x(857)+1,y(530)+1, xs(150)-2, ys(62)-2);
			g.drawRect(x(872)+1,y(669)+1, xs(120)-2, ys(52)-2);
			
			g.drawString("LOCAL", x(865), y(495));
			g.setFont(new Font("Trebuchet", Font.BOLD, font(45)));
			g.drawString("EXIT", x(882), y(712));
			g.drawString("CPU", x(883), y(578));
			
		}
		else if(enterNames)
		{
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, xs(1920), ys(1080));
			this.add(nameText);
			nameText.setBounds(x(800),y(250), xs(300), ys(75));
			nameText.setFont(new Font("Times New Roman", Font.PLAIN, font(50)));
			if(names.size()>=4 || confirm)
			{
    			nameText.setEnabled(false);
			}
    		else
    		{
    			nameText.setEnabled(true);
    			nameText.addActionListener(action);
    			nameText.grabFocus();
			    nameText.requestFocusInWindow();
    		}
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource("/plus.png")), x(1100),y(250), xs(75), ys(75), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.setFont(new Font("Roboto", Font.BOLD| Font.CENTER_BASELINE, font(75)));
			g.setColor(new Color(222,219,246));
			g.drawString("PLAYER NAMES", x(657), y(170));
			
			g.setColor(Color.white);
			g.setFont(new Font("Trebuchet", Font.BOLD, font(35)));
			g.fillRect(x(725), y(400), xs(120), ys(50));
			g.fillRect(x(1070), y(400), xs(120), ys(50));
			
			g.setColor(Color.black);
			g.drawRect(x(725), y(400), xs(120), ys(50));
			g.drawRect(x(1070), y(400), xs(120), ys(50));
//			g.drawRect(726, 401, 118, 48);
//			g.drawRect(1071, 401, 118, 48);
			g.drawString("BACK", x(735), y(439));
			g.drawString("DONE", x(1081), y(439));
			
			for(int i = 0;i<names.size();i++)
			{
				g.setColor(Color.white);
				g.setFont(new Font("Times New Roman", Font.ITALIC, font(25)));
				if(i==0)
				{
					if(hovering1)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Times New Roman", Font.ITALIC |Font.BOLD, font(25)));
					}
					if(names.get(0).length()>10)
						g.drawString(names.get(0).substring(0,9)+"...", x(800), y(550));
					else
						g.drawString(names.get(0), x(800), y(550));
				}
				else if(i==1)
				{
					g.setColor(Color.white);
					g.setFont(new Font("Times New Roman", Font.ITALIC, font(25)));
					if(hovering2)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Times New Roman", Font.ITALIC |Font.BOLD, font(25)));
					}
					if(names.get(1).length()>10)
						g.drawString(names.get(1).substring(0,9)+"...", x(1010), y(550));
					else
						g.drawString(names.get(1), x(1010), y(550));
				}
				else if(i==2)
				{
					g.setColor(Color.white);
					g.setFont(new Font("Times New Roman", Font.ITALIC, font(25)));
					if(hovering3)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Times New Roman", Font.ITALIC |Font.BOLD, font(25)));
					}
					if(names.get(2).length()>10)
						g.drawString(names.get(2).substring(0,9)+"...", x(800), y(650));
					else
						g.drawString(names.get(2), x(800), y(650));
				}
				else if(i==3)
				{
					g.setColor(Color.white);
					g.setFont(new Font("Times New Roman", Font.ITALIC, font(25)));
					if(hovering4)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Times New Roman", Font.ITALIC |Font.BOLD, font(25)));
					}
					if(names.get(3).length()>10)
						g.drawString(names.get(3).substring(0,9)+"...", x(1010), y(650));
					else
						g.drawString(names.get(3), x(1010), y(650));
				}
			}
			
			if(confirm)
			{
				g.setColor(Color.white);
				g.fillRect(x(790), y(375), xs(320), ys(365));
				g.setColor(Color.black);
				g.drawRect(x(790), y(375), xs(320), ys(365));
				
				g.drawRect(x(840), y(670), xs(50), ys(30));
				g.drawRect(x(1000), y(670), xs(60), ys(30));
				g.setFont(new Font("Trebuchet", Font.PLAIN, font(25)));
				g.drawString("You didn't enter 4 names.", x(810), y(435));
				g.drawString("Continue?", x(895), y(460));
				
				g.setFont(new Font("Trebuchet", Font.BOLD, font(22)));
				g.drawString("YES", x(1008), y(695));
				g.drawString("NO", x(849), y(695));
			}
			
			
		}
		else if(gamestate.isOver() && !gameEndedBack)
		{
			
			paintEndText(g);

		}
		else
		{
			this.remove(nameText);
			if(cpu)
			{
				if(game.getTurn()==0 ||  gameEndedBack)
				{
					game.cpuActive = false;
					arrows = true;
				}
				else
				{
					game.cpuActive = true;
					arrows = false;
				}
			}
			
			
			g.setColor(new Color(0,138,138));
			g.fillRect(0, 0, x(1920), y(1080));
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource("/menu.png")), x(1715), y(10), xs(150), ys(100), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			if(gameEndedBack)
			{
				g.setColor(Color.white);
				g.fillRect(x(1645), y(915), xs(125), ys(53));
				g.setColor(Color.black);
				g.drawRect(x(1645), y(915), xs(125), ys(53));
				g.setFont(new Font("Trebuchet", Font.BOLD|Font.CENTER_BASELINE, font(35)));
				g.drawString("FINISH", x(1653), y(956));
			}
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource("/card_back.png")),x(670),y(400), xs(cardWidth), ys(cardHeight), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			paintDiscard(g,950, 400);
			if(cpu)
			{
				paintCPUHands(g, 475, 800);
				paintTurnArrow(g, game.getTurn());
			}
			else
		    	paintHands(g,475, 800);
			
		    paintExtra(g,game.getCurrentDirection(), game.showTopCard().getColor().toString());
		    paintHistory(g);
		    
		    colorPickerPlayCoord = 517+120*(wildIndex%7);
		    if (colorPickerDraw) //drew wild card color picker
		    {
		    	g.setColor(Color.black);
		    	g.setFont(new Font("Trebuchet", Font.BOLD, font(45)));
		    	g.drawString("V", x(722), y(642));
		    	g.setColor(new Color(246,72,72));
		    	g.fillRect(x(622), y(665), xs(50), ys(50));
		    	g.setColor(new Color(0,191,255));
		    	g.fillRect(x(682), y(665), xs(50), ys(50));
		    	g.setColor(new Color(0,229,145));
		    	g.fillRect(x(742),y(665), xs(50), ys(50));
		    	g.setColor(Color.yellow);
		    	g.fillRect(x(802),y(665), xs(50), ys(50));
		    }
		    else if(colorPickerPlay) //played wild card color picker
		    {
		    	g.setColor(Color.black);
		    	g.setFont(new Font("Trebuchet", Font.PLAIN, font(65)));
		    	g.drawString("^", x(colorPickerPlayCoord), y(810));
		    	g.setColor(new Color(246,72,72));
		    	g.fillRect(x(colorPickerPlayCoord-102), y(690), xs(50), ys(50));
		    	g.setColor(new Color(0,191,255));
		    	g.fillRect(x(colorPickerPlayCoord-102+60), y(690), xs(50), ys(50));
		    	g.setColor(new Color(0,229,145));
		    	g.fillRect(x(colorPickerPlayCoord-102+120),y(690), xs(50), ys(50));
		    	g.setColor(Color.yellow);
		    	g.fillRect(x(colorPickerPlayCoord-102+180),y(690), xs(50), ys(50));
		    }
		    if(menu)
			{
				
				g.setColor(Color.white);
				g.fillRect(x(1650), 0, xs(270), ys(1080));
				
				try {
					g.drawImage(ImageIO.read(getClass().getResource("/menu.png")), x(1715), y(10), xs(150), ys(100), null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				g.setColor(Color.black);
				g.drawRect(x(1700), y(115), xs(100), ys(45));
				g.drawRect(x(1650), 0, xs(267), ys(1076));
				g.setFont(new Font("Trebuchet", Font.BOLD|Font.CENTER_BASELINE, font(30)));
				g.drawString("HOME", x(1706), y(149));
			}
		    
		    
		    if(cpu && game.lastMove.contains("CPU") && !game.lastMove.contains("rid") && !(colorPickerPlay || colorPickerDraw)&& game.cpuActive)
			{
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(cpu && !(colorPickerPlay || colorPickerDraw) && !gamestate.isOver() && !gameEndedBack)
		    {
				
		    	if(game.current_player == game.playerList.get(1) || game.current_player == game.playerList.get(2) ||game.current_player == game.playerList.get(3))
		    	{
		    		cpuTurn();
		    	}
		    }
			
		}
	}
	public void cpuTurn()
	{
		game.cpuTurn();
		game.nextTurn();
		if(game.lastMove.contains("draw"))
			addHistory(game.getLastPlayerTurn("draw"));
		else
			addHistory(game.getLastPlayerTurn("play"));
		
		repaint();
	}
	public void paintHistory(Graphics g)
	{
		if(history.size()>0)
		{
			g.setColor(Color.black);
			int fontSize = 12;
			if(scrWidth == 1920.0)
				fontSize=11;
			g.setFont(new Font("Arial", Font.BOLD | Font.ROMAN_BASELINE, font(fontSize)));
			String[]x = new String[8];
			System.arraycopy(history.toArray(), 0, x, 0, history.size());
			for(int i = 0;i<x.length;i++)
			{
				if(x[i]!=null)
					g.drawString(x[i], x(25), y(45+15*i));
			}	
		}
	}
	public void paintTurnArrow(Graphics g, int turn)
	{
		g.setColor(Color.black);
		g.setFont(new Font("Trebuchet", Font.BOLD, font(35)));
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource("/arrow pointer.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(turn == 0)
		{
			g.drawImage(img, x(840), y(455), xs(75), ys(75), null);
		}
		else if(turn==1)
		{

			g.drawImage(rotateClockwise90(img), x(840), y(455), xs(75), ys(75), null);
		}
		else if(turn==2)
		{
			g.drawImage(rotate180(img), x(840), y(455), xs(75), ys(75), null);
		}
		else
		{

			g.drawImage(rotateClockwise270(img), x(840), y(455), xs(75), ys(75), null);
		}
	}
	public void addHistory(String add)
	{
		if(history.size()>=8)
			history.remove();
		history.add(add);
	}
	public void paintArrows(Graphics g,int handSize)
	{
		if(cpu)
			maxPage = Math.max(1,(int)(Math.ceil((double)game.playerList.get(0).getHandSize()/7)));
		else
			maxPage = Math.max(1,(int)(Math.ceil((double)game.current_player.getHandSize()/7)));
		if (page!=1 && arrows)
		{
		
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(new Font("Roboto", Font.BOLD, font(100)));
			g.drawString("<",x(385), y(937));
			
		}
		if (page!=maxPage && arrows)
		{
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(new Font("Roboto", Font.BOLD, font(100)));
			g.drawString(">",x(1360), y(937));
			
		}
		
		if(gameEndedBack)
		{
			if(cpu)
			{
				maxPage2 = Math.max(1,(int)(Math.ceil((double)game.playerList.get(1).getHandSize()/5)));
				maxPage3 = Math.max(1,(int)(Math.ceil((double)game.playerList.get(2).getHandSize()/5)));
				maxPage4 = Math.max(1,(int)(Math.ceil((double)game.playerList.get(3).getHandSize()/5)));
			}
			else
			{
				maxPage2 = Math.max(1,(int)(Math.ceil((double)game.playerList.get((game.getTurn()+1)%4).getHandSize()/5)));
				maxPage3 = Math.max(1,(int)(Math.ceil((double)game.playerList.get((game.getTurn()+2)%4).getHandSize()/5)));
				maxPage4 = Math.max(1,(int)(Math.ceil((double)game.playerList.get((game.getTurn()+3)%4).getHandSize()/5)));
		
			}
			System.out.println(maxPage2+" "+maxPage3+" "+maxPage4);
			
			if (page3!=1)
			{
			
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.BOLD, font(55)));
				g.drawString(">",x(1260), y(210));
				
			}
			if (page3!=maxPage3)
			{
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.BOLD, font(55)));
				g.drawString("<",x(650), y(210));
				
			}
			
			if (page2!=1)
			{
			
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.BOLD, font(70)));
				g.drawString("^",x(275), y(250));
				
			}
			if (page2!=maxPage2)
			{
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.BOLD, font(55)));
				g.drawString("v",x(275), y(825));
				
			}
			
			if (page4!=maxPage4)
			{
			
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.BOLD, font(70)));
				g.drawString("^", x(1550), y(250));
				
			}
			if (page4!=1)
			{
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.BOLD, font(55)));
				g.drawString("v", x(1550), y(825));
				
			}
		}
	}
	public void paintCPUHands(Graphics g, int x, int y)
	{
		if(game.getTurn()==0)
			game.playerList.get(0).sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());	
		int start = (page-1)*7;
		int end = Math.min(start+7, game.playerList.get(0).getHandSize());
		for(int i = start;i<end;i++)
		{
			String a = "/";
			UnoCard card = game.playerList.get(0).getHand().get(i);
			
			if(card.getValue().toString().equals("Reverse"))
				a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
			else if(card.getValue().toString().equals("DrawTwo"))
				a += card.getColor().toString().toLowerCase()+"_picker"+".png";
			else if(card.getValue().toString().equals("Wild"))
				a += "wild_color_changer.png";
			else if(card.getValue().toString().equals("Skip"))
				a += card.getColor().toString().toLowerCase()+"_skip"+".png";
			else if(card.getValue().toString().equals("Wild_Four"))
				a += "wild_pick_four.png";
			else
				a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y), xs(cardWidth), ys(cardHeight), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		paintArrows(g, game.playerList.get(0).getHandSize());
		g.setColor(Color.white);
		g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
		g.drawString(game.playerList.get(0).getPaintName(), x(775), y(775));
		g.setColor(Color.DARK_GRAY);
		g.drawString("Cards: "+Integer.toString((game.playerList.get(0).getHandSize())), x(925), y(775));
		if(gamestate.isUno(game.playerList.get(0)))
		{
			g.setColor(Color.red);
			g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
	    	g.drawString("UNO",x(850),y(750));
		}
		for(int i =1;i<game.playerList.size();i++)
		{
			Player p = game.playerList.get(i);
			if(i == 2)
			{
				if(gameEndedBack)
				{
					int s= (page3-1)*5;
					int en = Math.min(s+5, game.playerList.get(2).getHandSize());
					for(int h = s;h<en;h++)
					{
						String a = "/";
						UnoCard card = game.playerList.get(2).getHand().get(h);
						
						if(card.getValue().toString().equals("Reverse"))
							a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
						else if(card.getValue().toString().equals("DrawTwo"))
							a += card.getColor().toString().toLowerCase()+"_picker"+".png";
						else if(card.getValue().toString().equals("Wild"))
							a += "wild_color_changer.png";
						else if(card.getValue().toString().equals("Skip"))
							a += card.getColor().toString().toLowerCase()+"_skip"+".png";
						else if(card.getValue().toString().equals("Wild_Four"))
							a += "wild_pick_four.png";
						else
							a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
						
						try {
							g.drawImage(rotate180(ImageIO.read(getClass().getResource(a))),x(1115-cardWidth-100*(h%5)),y(100), xs(cardWidth), ys(cardHeight), null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
					{
						
						try {
							g.drawImage(rotate180(ImageIO.read(getClass().getResource("/card_back_alt.png"))),x(669+50*h),y(100), xs(cardWidth), ys(cardHeight), null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				g.setColor(Color.white);
				g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
				g.drawString(game.playerList.get(2).getPaintName(), x(800), y(310));
				g.setColor(Color.DARK_GRAY);
				g.drawString("Cards: "+Integer.toString(game.playerList.get(2).getHandSize()),x(900), y(310));
				if(gamestate.isUno(game.playerList.get(2)))
				{
					g.setColor(Color.red);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
			    	g.drawString("UNO",x(850),y(350));
				}
			}
			else if (i == 1)
			{
				BufferedImage im = null;
				if(gameEndedBack)
				{
					int s= (page2-1)*5;
					int en = Math.min(s+5, game.playerList.get(1).getHandSize());
					for(int h = s;h<en;h++)
					{
						String a = "/";
						UnoCard card = game.playerList.get(1).getHand().get(h);
						
						if(card.getValue().toString().equals("Reverse"))
							a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
						else if(card.getValue().toString().equals("DrawTwo"))
							a += card.getColor().toString().toLowerCase()+"_picker"+".png";
						else if(card.getValue().toString().equals("Wild"))
							a += "wild_color_changer.png";
						else if(card.getValue().toString().equals("Skip"))
							a += card.getColor().toString().toLowerCase()+"_skip"+".png";
						else if(card.getValue().toString().equals("Wild_Four"))
							a += "wild_pick_four.png";
						else
							a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
						
						try {
							im = rotateClockwise90(ImageIO.read(getClass().getResource(a)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						g.drawImage(im,x(200),y(250+100*(h%5)), xs(cardHeight), ys(cardWidth), null);
					}
				}
				else
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
					{
							try {
								im = rotateClockwise90(ImageIO.read(getClass().getResource("/card_back_alt.png")));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						g.drawImage(im,x(200),y(250+50*h), xs(cardHeight), ys(cardWidth), null);
						
					}
				}
				g.setColor(Color.white);
				g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
				g.drawString(game.playerList.get(1).getPaintName(), x(400), y(500));
				g.setColor(Color.DARK_GRAY);
				g.drawString("Cards: "+Integer.toString(game.playerList.get(1).getHandSize()), x(400), y(600));
				if(gamestate.isUno(game.playerList.get(1)))
				{
					g.setColor(Color.red);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
			    	g.drawString("UNO",x(450),y(550));
				}
			}
			else if(i==3)
			{
				BufferedImage im = null;
				
				if(gameEndedBack)
				{
					int s= (page4-1)*5;
					int en = Math.min(s+5, game.playerList.get(3).getHandSize());
					for(int h = s;h<en;h++)
					{
						String a = "/";
						UnoCard card = game.playerList.get(3).getHand().get(h);
						
						if(card.getValue().toString().equals("Reverse"))
							a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
						else if(card.getValue().toString().equals("DrawTwo"))
							a += card.getColor().toString().toLowerCase()+"_picker"+".png";
						else if(card.getValue().toString().equals("Wild"))
							a += "wild_color_changer.png";
						else if(card.getValue().toString().equals("Skip"))
							a += card.getColor().toString().toLowerCase()+"_skip"+".png";
						else if(card.getValue().toString().equals("Wild_Four"))
							a += "wild_pick_four.png";
						else
							a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
						
						try {
							im = rotateClockwise270(ImageIO.read(getClass().getResource(a)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						g.drawImage(im,x(1480),y(780-cardWidth-100*(h%5)), xs(cardHeight), ys(cardWidth), null);
					}

				}
				else
					for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
					{
						
						try {
							im = rotateClockwise270(ImageIO.read(getClass().getResource("/card_back_alt.png")));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
						g.drawImage(im,x(1480),y(250+50*h), xs(cardHeight), ys(cardWidth), null);
					}
				g.setColor(Color.white);
				g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
				g.drawString(game.playerList.get(3).getPaintName(),x(1370) ,y(500));
				g.setColor(Color.DARK_GRAY);
				g.drawString("Cards: "+Integer.toString(game.playerList.get(3).getHandSize()), x(1370), y(600));
				if(gamestate.isUno(game.playerList.get(3)))
				{
					g.setColor(Color.red);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
			    	g.drawString("UNO",x(1320),y(550));
				}
			}
			
		}	
	}
	public void paintHands(Graphics g, int x, int y)
	{
		game.current_player.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());	
		int start = (page-1)*7;
		int end = Math.min(start+7, game.current_player.getHandSize());
		for(int i = start;i<end;i++)
		{
			String a = "/";
			UnoCard card = game.current_player.getHand().get(i);
			
			if(card.getValue().toString().equals("Reverse"))
				a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
			else if(card.getValue().toString().equals("DrawTwo"))
				a += card.getColor().toString().toLowerCase()+"_picker"+".png";
			else if(card.getValue().toString().equals("Wild"))
				a += "wild_color_changer.png";
			else if(card.getValue().toString().equals("Skip"))
				a += card.getColor().toString().toLowerCase()+"_skip"+".png";
			else if(card.getValue().toString().equals("Wild_Four"))
				a += "wild_pick_four.png";
			else
				a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y), xs(cardWidth), ys(cardHeight), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		paintArrows(g, game.current_player.getHandSize());
		g.setColor(Color.white);
		g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
		g.drawString(game.current_player.getPaintName(), x(775), y(775));
		g.setColor(Color.DARK_GRAY);
		g.drawString("Cards: "+Integer.toString((game.current_player.getHandSize())), x(925), y(775));
		if(gamestate.isUno(game.current_player))
		{
			g.setColor(Color.red);
			g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
	    	g.drawString("UNO",x(850),y(750));
		}
		for(int i =0;i<game.playerList.size();i++)
		{
			if(i !=game.getTurn())
			{
				Player p = game.playerList.get(i);
				if(i == (game.getTurn()+2)%4)
				{
					if(gameEndedBack)
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 5);h++)
						{
							String a = "/";
							UnoCard card = p.getHand().get(h);
							p.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());
							
							if(card.getValue().toString().equals("Reverse"))
								a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
							else if(card.getValue().toString().equals("DrawTwo"))
								a += card.getColor().toString().toLowerCase()+"_picker"+".png";
							else if(card.getValue().toString().equals("Wild"))
								a += "wild_color_changer.png";
							else if(card.getValue().toString().equals("Skip"))
								a += card.getColor().toString().toLowerCase()+"_skip"+".png";
							else if(card.getValue().toString().equals("Wild_Four"))
								a += "wild_pick_four.png";
							else
								a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
							
							try {
								g.drawImage(rotate180(ImageIO.read(getClass().getResource(a))),x(669+100*h),y(100), xs(cardWidth), ys(cardHeight), null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
						{
							
							try {
								g.drawImage(rotate180(ImageIO.read(getClass().getResource("/card_back_alt.png"))),x(669+50*h),y(100), xs(cardWidth), ys(cardHeight), null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
					g.drawString(game.playerList.get((game.getTurn()+2)%4).getPaintName(), x(800), y(310));
					g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get((game.getTurn()+2)%4).getHandSize()), x(900), y(310));
					if(gamestate.isUno(game.playerList.get((game.getTurn()+2)%4)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
				    	g.drawString("UNO",x(850),y(350));
					}
				}
				else if ( i == (game.getTurn()+1)%4)
				{
					BufferedImage im = null;
					
					if(gameEndedBack)
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
						{
							String a = "/";
							UnoCard card = p.getHand().get(h);
							p.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());
							
							if(card.getValue().toString().equals("Reverse"))
								a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
							else if(card.getValue().toString().equals("DrawTwo"))
								a += card.getColor().toString().toLowerCase()+"_picker"+".png";
							else if(card.getValue().toString().equals("Wild"))
								a += "wild_color_changer.png";
							else if(card.getValue().toString().equals("Skip"))
								a += card.getColor().toString().toLowerCase()+"_skip"+".png";
							else if(card.getValue().toString().equals("Wild_Four"))
								a += "wild_pick_four.png";
							else
								a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
							
							try {
								im = rotateClockwise90(ImageIO.read(getClass().getResource(a)));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							g.drawImage(im,x(200),y(250+100*h), xs(cardHeight), ys(cardWidth), null);
						}
					}
					else
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
						{
							try {
								im = rotateClockwise90(ImageIO.read(getClass().getResource("/card_back_alt.png")));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							g.drawImage(im,x(200),y(250+50*h), xs(cardHeight), ys(cardWidth), null);
							
						}
					}
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
					g.drawString(game.playerList.get((game.getTurn()+1)%4).getPaintName(), x(400), y(500));
					g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get((game.getTurn()+1)%4).getHandSize()), x(400), y(600));
					if(gamestate.isUno(game.playerList.get((game.getTurn()+1)%4)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
				    	g.drawString("UNO",x(450),y(550));
					}
				}
				else if(i==(game.getTurn()+3)%4)
				{
					BufferedImage im = null;
					
					if(gameEndedBack)
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 5);h++)
						{
							String a = "/";
							UnoCard card = p.getHand().get(h);
							p.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());
							
							if(card.getValue().toString().equals("Reverse"))
								a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
							else if(card.getValue().toString().equals("DrawTwo"))
								a += card.getColor().toString().toLowerCase()+"_picker"+".png";
							else if(card.getValue().toString().equals("Wild"))
								a += "wild_color_changer.png";
							else if(card.getValue().toString().equals("Skip"))
								a += card.getColor().toString().toLowerCase()+"_skip"+".png";
							else if(card.getValue().toString().equals("Wild_Four"))
								a += "wild_pick_four.png";
							else
								a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
							
							try {
								im = rotateClockwise270(ImageIO.read(getClass().getResource(a)));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							g.drawImage(im,x(1480),y(250+100*h), xs(cardHeight), ys(cardWidth), null);
						}
					}
					else
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
						{
							try {
								im = rotateClockwise270(ImageIO.read(getClass().getResource("/card_back_alt.png")));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
							g.drawImage(im,x(1480),y(250+50*h), xs(cardHeight), ys(cardWidth), null);
						}
					}
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
					g.drawString(game.playerList.get((game.getTurn()+3)%4).getPaintName(),x(1370) ,y(500));
					g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get((game.getTurn()+3)%4).getHandSize()), x(1370), y(600));
					if(gamestate.isUno(game.playerList.get((game.getTurn()+3)%4)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
				    	g.drawString("UNO",x(1320),y(550));
					}
				}
			}
		}	
	}
	public BufferedImage rotateClockwise90(BufferedImage src) {
	    int width = src.getWidth();
	    int height = src.getHeight();

	    BufferedImage img = new BufferedImage(height, width, src.getType()==0?5:src.getType());

	    Graphics2D graphics2D = img.createGraphics();
	    graphics2D.translate((height - width) / 2, (height - width) / 2);
	    graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
	    graphics2D.drawRenderedImage(src, null);

	    return img;
	}
	public BufferedImage rotate180(BufferedImage src) {
	    int width = src.getWidth();
	    int height = src.getHeight();

	    BufferedImage img = new BufferedImage(width, height, src.getType()==0?5:src.getType());

	    Graphics2D graphics2D = img.createGraphics();
	    graphics2D.translate(0, 0);
	    graphics2D.rotate(Math.PI, width / 2, height / 2);
	    graphics2D.drawRenderedImage(src, null);

	    return img;
	}
	public BufferedImage rotateClockwise270(BufferedImage src) {
	    int width = src.getWidth();
	    int height = src.getHeight();

	    BufferedImage img = new BufferedImage(height, width, src.getType()==0?5:src.getType());

	    Graphics2D graphics2D = img.createGraphics();
	    graphics2D.translate((width-height) / 2, (width-height) / 2);
	    graphics2D.rotate(Math.PI*3 / 2, height / 2, width / 2);
	    graphics2D.drawRenderedImage(src, null);

	    return img;
	}
	public void paintDiscard(Graphics g, int x, int y)
	{
		String a = "/";
		UnoCard card = game.deck.discard.get(game.deck.discard.size()-1);
		
		if(card.getValue().toString().equals("Reverse"))
			a += card.getColor().toString().toLowerCase()+"_reverse"+".png";
		else if(card.getValue().toString().equals("DrawTwo"))
			a += card.getColor().toString().toLowerCase()+"_picker"+".png";
		else if(card.getValue().toString().equals("Wild"))
			a += "wild_color_changer.png";
		else if(card.getValue().toString().equals("Skip"))
			a += card.getColor().toString().toLowerCase()+"_skip.png";
		else if(card.getValue().toString().equals("Wild_Four"))
			a += "wild_pick_four.png";
		else 
			a += card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
		
		try {
			g.drawImage(ImageIO.read(getClass().getResource(a)),x(x),y(y), xs(cardWidth), ys(cardHeight), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void paintExtra(Graphics g, String direction, String color)
	{
		int x = 817;
		int y = 560;
		if(!cpu)
		{
			y = 460;
		}
		//g.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		g.setColor(Color.white);
		//g.drawString(direction, x, y);
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource("/direction arrow.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(direction.contains("Counter"))
			g.drawImage(img, x(x+125),y(y), xs(-125), ys(75), null);
		else
			g.drawImage(img, x(x), y(y), xs(125), ys(75), null);
		
			
		x = 951;
		y = 587;
		
		g.setFont(new Font("Roboto", Font.BOLD, font(30)));
		if(color.equals("Yellow"))
			g.setColor(Color.YELLOW);
		else if(color.equals("Blue"))
			g.setColor(new Color(0,191,255));
		else if(color.equals("Red"))
			g.setColor(new Color(246,72,72));
		else
			g.setColor(new Color(0,229,145));
		
		g.fillRect(x(x), y(y),xs(cardWidth), ys(7));
	}
	
	public void paintEndText(Graphics g)
	{
		int x = 555+((Math.max(5-game.getLastTurn().getPaintName().length(), 0))*40);
		int y = 350;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,xs(1920),ys(1080));
		
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,font(75)));
		g.setColor(Color.RED);
		g.drawString(game.current_player.getPaintName()+" is the winner!!!", x(x), y(y));
		
		g.setColor(Color.white);

		g.drawRect(x(785), y(491), xs(310), ys(80));
		g.drawRect(x(804), y(641), xs(275), ys(80));
		g.fillRect(x(880), y(395), xs(110), ys(50));
		g.setColor(Color.black);
		g.drawRect(x(880), y(395), xs(110), ys(50));
		g.drawRect(x(881), y(396), xs(108), ys(48));
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,font(50)));
		g.setColor(Color.green);
		g.drawString("NEW GAME", x(800), y(550));
		g.drawString("REMATCH", x(818), y(700));
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,font(35)));
		g.setColor(Color.black);
		g.drawString("BACK", x(887), y(435));
		
	}
	
	//@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	//@Override
	public void mouseEntered(MouseEvent e) {
			
	}
	//@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	//@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	//@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("X: "+e.getX()+" Y: "+e.getY());
		
		if(e.getX()>= x(1760) && e.getX()<=x(1810) && e.getY()>=y(40) && e.getY()<= y(75) && game.started) //open menu
		{
			menu = !menu;
			repaint();
		}
		
		
		else if(e.getX()>= x(1675) && e.getX()<=x(1770) && e.getY()>=y(115) && e.getY()<= y(160) && game.started && menu) //go to home
		{
			game = new Board();
			gamestate = new GameState(game);
			frame.setVisible(false);
			frame.dispose();
			UnoGraphics g = new UnoGraphics(game, gamestate);
		}
		
		if(colorPickerDraw || colorPickerPlay)
		{
			//drew then played wild card
			if(e.getX()>= x(622) && e.getX()<=x(672) && e.getY()>=y(665) && e.getY()<=y(715) && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("red");
				game.nextTurn();
				
	    		page = 1;
	    	    addHistory(game.getLastTurn().getName()+" drew a card.");
	    	  
	    	    colorPickerDraw = false;
	    	    repaint();
			}
			else if(e.getX()>= x(682) && e.getX()<=x(732) && e.getY()>=y(665) && e.getY()<=y(715) && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("blue");
				game.nextTurn();
				
	    		page = 1;
	    	    addHistory(game.getLastTurn().getName()+" drew a card.");
	    	  
	    	    colorPickerDraw = false;
	    	    repaint();
			}
			else if(e.getX()>= x(742) && e.getX()<=x(792) && e.getY()>=y(665) && e.getY()<=y(715) && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("green");
				game.nextTurn();
				
	    		page = 1;
	    	    addHistory(game.getLastTurn().getName()+" drew a card.");
	    	   
	    	    colorPickerDraw = false;
	    	    repaint();
			}
			else if(e.getX()>= x(802) && e.getX()<=x(852) && e.getY()>=y(665) && e.getY()<=y(715) && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("yellow");
				game.nextTurn();
				
	    		page = 1;
	    	    addHistory(game.getLastTurn().getName()+" drew a card.");
	    	    
	    	    colorPickerDraw = false;
	    	    repaint();
			}
			//played wild card
			if(e.getX()>= x(colorPickerPlayCoord-102) && e.getX()<=x(colorPickerPlayCoord-102+50) && e.getY()>=y(690) && e.getY()<=y(740) && colorPickerPlay)
			{
				game.play(wildIndex);
				game.changeColor("red");
				game.nextTurn();
	    		page = 1;
	    		addHistory(game.getLastPlayerTurn("play"));
	    		
	    	    colorPickerPlay = false;
	    	    repaint();
			}
			else if(e.getX()>= x(colorPickerPlayCoord-102+60) && e.getX()<=x(colorPickerPlayCoord-102+60+50) && e.getY()>=y(690) && e.getY()<=y(740) && colorPickerPlay)
			{
				game.play(wildIndex);
				game.changeColor("blue");
				game.nextTurn();
	    		page = 1;
	    		addHistory(game.getLastPlayerTurn("play"));
	    		
	    	    colorPickerPlay = false;
	    	    repaint();
			}
			else if(e.getX()>= x(colorPickerPlayCoord-102+120) && e.getX()<=x(colorPickerPlayCoord-102+120+50) && e.getY()>=y(690) && e.getY()<=y(740) && colorPickerPlay)
			{
				game.play(wildIndex);
				game.changeColor("green");
				game.nextTurn();
	    		page = 1;
	    		addHistory(game.getLastPlayerTurn("play"));
	    		
	    	    colorPickerPlay = false;
	    	    repaint();
			}
			else if(e.getX()>= x(colorPickerPlayCoord-102+180) && e.getX()<=x(colorPickerPlayCoord-102+180+50) && e.getY()>=y(690) && e.getY()<=y(740) && colorPickerPlay)
			{
				game.play(wildIndex);
				game.changeColor("yellow");
				game.nextTurn();
	    		page = 1;
	    		addHistory(game.getLastPlayerTurn("play"));
	    		
	    	    colorPickerPlay = false;
	    	    repaint();
			}
		}
		else if(start || enterNames)
		{
			if(confirm) //confirmation popup
			{
				if(e.getX()>= x(840) && e.getX()<=x(890) && e.getY()>=y(670) && e.getY()<=y(700) && enterNames && confirm)
				{
					this.remove(nameText);
					confirm = false;
					repaint();
					
				}
				else if(e.getX()>= x(1000) && e.getX()<=x(1060) && e.getY()>=y(670) && e.getY()<=y(700) && enterNames && confirm)
				{
					this.remove(nameText);
					enterNames = false;
					confirm = false;
					int size = names.size();
					for(int i = 1;i<=4-size;i++)
						names.add("Player "+i);
					game.start(names);
					repaint();
				}
			}
			else
			{
				//beginning screen
				if(e.getX()>= x(865) && e.getX()<=x(1015) && e.getY()>=y(450) && e.getY()<=y(512) && start)
				{
					start = false;
					enterNames = true;
					//game.start(names);
					repaint();
				}
				else if(e.getX()>= x(865) && e.getX()<=x(1015) && e.getY()>=y(530) && e.getY()<=y(592) && start)
				{
					start = false;
					cpu = true;
					game.start(cpu);
					repaint();
				}
				else if(e.getX()>= x(869) && e.getX()<=x(992) && e.getY()>=y(669) && e.getY()<=y(722) && start)
				{
					frame.setVisible(false); 
					frame.dispose();
				}
				
				
				//entering names screen
				if(e.getX()>= x(1110) && e.getX()<=x(1160) && e.getY()>=y(260) && e.getY()<=y(313) && enterNames && names.size()<4) //add name (plus png)
				{
					this.remove(nameText);
					if(nameText.getText().replace(" ", "").length()>0)
						names.add(nameText.getText());
					nameText.setText("");
					repaint();
				}
				else if(e.getX()>= x(725) && e.getX()<=x(845) && e.getY()>=y(400) && e.getY()<=y(450) && enterNames)// back button
				{
					this.remove(nameText);
					enterNames = false;
					start = true;
					names.clear();
					repaint();
				}
				else if(e.getX()>= x(1070) && e.getX()<=x(1190) && e.getY()>=y(400) && e.getY()<=y(450) && enterNames)// done button
				{
					if(names.size()==4)
					{
						this.remove(nameText);
						enterNames = false;
						game.start(names);
						repaint();
					}
					else
					{
						confirm = true;
						repaint();
					}
				}

				else if(names.size()>0 && e.getX()>= x(800) && e.getX()<=x(800+(Math.min(13*names.get(0).length(), 12*10))) && e.getY()>=y(530) && e.getY()<=y(550) && enterNames)//delete first name
				{
					
					this.remove(nameText);
					names.remove(0);
					repaint();
				}
				else if(names.size()>1&& e.getX()>= x(1010) && e.getX()<=x(1010+(Math.min(13*names.get(1).length(), 12*10))) && e.getY()>=y(530) && e.getY()<=y(550) && enterNames)//second name
				{
					
					this.remove(nameText);
					names.remove(1);
					repaint();
				}
				else if(names.size()>2&& e.getX()>= x(800) && e.getX()<=x(800+(Math.min(13*names.get(2).length(), 12*10))) && e.getY()>=y(630) && e.getY()<=y(650) && enterNames)//third name
				{
					this.remove(nameText);
					names.remove(2);
					repaint();
				}
				else if(names.size()>3&& e.getX()>= x(1010) && e.getX()<=x(1010+(Math.min(13*names.get(3).length(), 12*10))) && e.getY()>=y(630) && e.getY()<=y(650) && enterNames)//fourth name
				{
					this.remove(nameText);
					names.remove(3);
					repaint();
				}
			}	
		}
		else if(gamestate.isOver())
		{
			if(gameEndedBack)
			{
				if(e.getX()>= x(1645) && e.getX()<=x(1770) && e.getY()>=y(915) && e.getY()<=y(969)) //go back to end screen from uno board
				{
					gameEndedBack = false;
					page = 1;
					page2 = 1;
					page3 = 1;
					page4 = 1;
					
					maxPage = 0;
					maxPage2 = 0;
					maxPage3 = 0;
					maxPage4 = 0;
					repaint();
				}
				else if(e.getX()>= x(302) && e.getX()<= x(345) && e.getY()>=y(170) && e.getY()<=y(205) && page2>1 && gameEndedBack)
				{
					//System.out.println("asjdh");
					page2-=1;
					repaint();
				}
				else if(e.getX()>= x(302) && e.getX()<=x(345) && e.getY()>=y(770) && e.getY()<=y(800) && page2<maxPage2 && gameEndedBack)
				{
					//System.out.println("asjdh");
					page2+=1;
					repaint();
				}
				else if(e.getX()>= x(975) && e.getX()<= x(1025) && e.getY()>=y(125) && e.getY()<=y(175) && page3>1 && gameEndedBack)
				{
					//System.out.println("asjdh");
					page3-=1;
					repaint();
				}
				else if(e.getX()>= x(475) && e.getX()<=x(525) && e.getY()>=y(125) && e.getY()<=y(175) && page3<maxPage3 && gameEndedBack)
				{
					//System.out.println("asjdh");
					page3+=1;
					repaint();
				}
				else if(e.getX()>=x(1580)  && e.getX()<= x(1625) && e.getY()>= y(770) && e.getY()<=y(800) && page4>1 && gameEndedBack)
				{
					//.out.println("asjdh");
					page4-=1;
					repaint();
				}
				else if(e.getX()>= x(1580) && e.getX()<=x(1625) && e.getY()>=y(170) && e.getY()<=y(205) && page4<maxPage4 && gameEndedBack)
				{
					//System.out.println("asjdh");
					page4+=1;
					repaint();
				}
				else if(e.getX()>= x(385) && e.getX()<= x(440) && e.getY()>=y(850) && e.getY()<=y(937) && page>1)
				{
					page-=1;
					repaint();
				}
				else if(e.getX()>= x(1360) && e.getX()<=x(1415) && e.getY()>=y(850) && e.getY()<=y(937) && page<maxPage)
				{
					page+=1;
					repaint();
				}
			}
			else
			{
				if(e.getX()>= x(785) && e.getX()<=x(1095) && e.getY()>=y(491) && e.getY()<=y(571))//new game
				{
					game = new Board();
					gamestate = new GameState(game);
					frame.setVisible(false);
					frame.dispose();
					UnoGraphics g = new UnoGraphics(game, gamestate);
				}
				else if(e.getX()>= x(804) && e.getX()<=x(1080) && e.getY()>=y(641) && e.getY()<=y(721)) //rematch
				{
					game = new Board();
					gamestate = new GameState(game);
					frame.setVisible(false);
					frame.dispose();
					UnoGraphics g = new UnoGraphics(game, gamestate);
					if(cpu)
					{
						game.start(cpu);
						g.setCPU(true);
					}
					else
						game.start(names);
					g.skipStart();
				}
				else if(e.getX()>= x(880) && e.getX()<=x(990) && e.getY()>=y(395) && e.getY()<=y(445)) //see uno board
				{
					gameEndedBack = true;
					repaint();
				}
			}
			
		}
		else
		{
			if(!game.cpuActive)
			{
				if(!gameEndedBack)
				{
					if(e.getX()>= x(670) && e.getX()<=x(800) && e.getY()>=y(400) && e.getY()<=y(580))
					{
						if(game.deck.deck.get(game.deck.deck.size()-1).getColor().equals(UnoCard.Color.Wild))
						{
							game.drawTempCard();
			    			colorPickerDraw = true;
			    			
			    			repaint();
						}
						else
						{
							game.drawCard();
							System.out.println("drew a "+game.current_player.getHand().get(game.current_player.getHandSize()-1));
				    		game.checkPlayable();
				    		
				    		
				    		System.out.println(game.current_player.getName());
				    		System.out.println(game.current_player.getHand());
				    		System.out.println("top card is now "+game.deck.discard.get(game.deck.discard.size()-1));
				    		System.out.println(game.deck.deck.size()+" cards left");
				    		game.nextTurn();
				    		page = 1;
				    	    addHistory(game.getLastPlayerTurn("draw"));
				    	    repaint();
						}
					}
					
					if(e.getX()>= x(475) && e.getX()<=x(595) && e.getY()>=y(800) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>7*(page-1))
						{
							if(game.current_player.getHand().get(7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								colorPickerPlay = true;
								wildIndex = 7*(page-1);
								colorPickerPlayCoord = 517+120*(wildIndex%7);
								repaint();
							}
							else
							{
								game.play(7*(page-1));
								
					    		
					    	    if(game.canPlay())
					    	    {
					    	    	page = 1;
					    	    	game.nextTurn();
					    	    	addHistory(game.getLastPlayerTurn("play"));
					    	    }
								repaint();
							}
						}
						
					}
					else if(e.getX()>= x(595) && e.getX()<=x(715) && e.getY()>=y(800) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>1+7*(page-1))
						{
							if(game.current_player.getHand().get(1+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								colorPickerPlay = true;
								wildIndex = 1+7*(page-1);
								colorPickerPlayCoord = 517+120*(wildIndex%7);
								repaint();
							}
							else
							{
								game.play(1+7*(page-1));
								
					    		
					    	    if(game.canPlay())
					    	    {
					    	    	page = 1;
					    	    	game.nextTurn();
					    	    	addHistory(game.getLastPlayerTurn("play"));
					    	    }
								repaint();
							}
						}
					}
					else if(e.getX()>= x(715) && e.getX()<=x(835) && e.getY()>=y(800) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>2+7*(page-1))
						{
							if(game.current_player.getHand().get(2+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								colorPickerPlay = true;
								wildIndex = 2+7*(page-1);
								colorPickerPlayCoord = 517+120*(wildIndex%7);
								repaint();
							}
							else
							{
								game.play(2+7*(page-1));
							
					    		
					    		if(game.canPlay())
					    	    {
					    			page = 1;
					    	    	game.nextTurn();
					    	    	addHistory(game.getLastPlayerTurn("play"));
					    	    }
								repaint();
							}
						}
					}
					else if(e.getX()>= x(835) && e.getX()<=x(955) && e.getY()>=y(800) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>3+7*(page-1))
						{
							if(game.current_player.getHand().get(3+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								colorPickerPlay = true;
								wildIndex = 3+7*(page-1);
								colorPickerPlayCoord = 517+120*(wildIndex%7);
								repaint();
							}
							else
							{
								game.play(3+7*(page-1));
					
					    		
					    		if(game.canPlay())
					    	    {
					    			page = 1;
					    	    	game.nextTurn();
					    	    	addHistory(game.getLastPlayerTurn("play"));
					    	    }
								repaint();
							}
						}
					}
					else if(e.getX()>= x(955) && e.getX()<=x(1075) && e.getY()>=y(800) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>4+7*(page-1))
						{
							if(game.current_player.getHand().get(4+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								colorPickerPlay = true;
								wildIndex = 4+7*(page-1);
								colorPickerPlayCoord = 517+120*(wildIndex%7);
								repaint();
							}
							else
							{
								game.play(4+7*(page-1));
						
					    		
					    		if(game.canPlay())
					    	    {
					    			page = 1;
					    	    	game.nextTurn();
					    	    	addHistory(game.getLastPlayerTurn("play"));
					    	    }
								repaint();
							}
						}
					}
					else if(e.getX()>= x(1075) && e.getX()<=x(1195) && e.getY()>=y(800) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>5+7*(page-1))
						{
							if(game.current_player.getHand().get(5+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								colorPickerPlay = true;
								wildIndex = 5+7*(page-1);
								colorPickerPlayCoord = 517+120*(wildIndex%7);
								repaint();
							}
							else
							{
								game.play(5+7*(page-1));
							
					    		
					    		if(game.canPlay())
					    	    {
					    			page = 1;
					    	    	game.nextTurn();
					    	    	addHistory(game.getLastPlayerTurn("play"));
					    	    }
								repaint();
							}
						}
					}
					else if(e.getX()>= x(1195) && e.getX()<=x(1315) && e.getY()>=y(800) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>6+7*(page-1))
						{
							if(game.current_player.getHand().get(6+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								colorPickerPlay = true;
								wildIndex = 6+7*(page-1);
								colorPickerPlayCoord = 517+120*(wildIndex%7);
								repaint();
							}
							else
							{
								game.play(6+7*(page-1));
								
					    		
					    		if(game.canPlay())
					    	    {
					    			page = 1;
					    	    	game.nextTurn();
					    	    	addHistory(game.getLastPlayerTurn("play"));
					    	    }
								repaint();
							}
						}
					}
				}
					
				if(e.getX()>= x(385) && e.getX()<= x(440) && e.getY()>=y(850) && e.getY()<=y(937) && page>1)
				{
					page-=1;
					repaint();
				}
				else if(e.getX()>= x(1360) && e.getX()<=x(1415) && e.getY()>=y(850) && e.getY()<=y(937) && page<maxPage)
				{
					page+=1;
					repaint();
				}
				
			}
		}
	}

	//@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("HOVERING X: "+e.getX()+" Y: "+e.getY());
		if(enterNames && !confirm && e.getX()>= x(725) && e.getX()<=x(1200) && e.getY()>=y(475) && e.getY()<=y(700))
		{
			if(names.size()>0 && e.getX()>= x(800) && e.getX()<=x(800+(Math.min(13*names.get(0).length(), 12*10)))&& e.getY()>=y(530) && e.getY()<=y(550))//first name
			{
				
				this.remove(nameText);
				hovering1 = true;
				repaint();
			}
			else if(names.size()>1 && e.getX()>= x(1010) && e.getX()<=x(1010+(Math.min(12*names.get(1).length(), 12*10))) && e.getY()>=y(530) && e.getY()<=y(550))//second name
			{
				
				this.remove(nameText);
				hovering2 = true;
				repaint();
			}
			else if(names.size()>2 && e.getX()>= x(800) && e.getX()<=x(800+(Math.min(12*names.get(2).length(), 12*10))) && e.getY()>=y(630) && e.getY()<=y(650))//third name
			{
				
				this.remove(nameText);
				hovering3 = true;
				repaint();
			}
			else if(names.size()>3 && e.getX()>= x(1010) && e.getX()<=x(1010+(Math.min(12*names.get(3).length(), 12*10))) && e.getY()>=y(630) && e.getY()<=y(650))//fourth name
			{
				
				this.remove(nameText);
				hovering4 = true;
				repaint();
			}
			else
			{
				hovering1 = false;
				hovering2 = false;
				hovering3 = false;
				hovering4 = false;
				repaint();
			}
		}
	}
	//@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}


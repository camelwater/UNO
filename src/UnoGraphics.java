import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	
	private int cardHeight = 180;
	private int cardWidth = 130;
	private int page = 1;
	
	private boolean colorPickerDraw = false;
	private boolean colorPickerPlay = false;
	private int colorPickerPlayCoord = 517;
	private int wildIndex = 0;
	private int maxPage = 0;
	
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
	    @Override
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
			frame.setIconImage(ImageIO.read(getClass().getResource("card_back_large.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.add(this);
		frame.setSize(1920,1080);
		frame.setResizable(true);
		//frame.setFocusable(true);
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
	public void paintComponent(Graphics g)
	{
		if(start)
		{
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 1920, 1080);
			g.setColor(Color.red);
			g.setFont(new Font("Trebuchet", Font.BOLD, 75));
			g.drawString("UNO", 850, 200);
			try {
				g.drawImage(ImageIO.read(getClass().getResource("card_back_alt.png")), 867, 225, cardWidth, cardHeight, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			g.setColor(Color.white);
			g.setFont(new Font("Trebuchet", Font.BOLD, 40));
			g.fillRect(857,450, 150, 62);
			g.fillRect(857,530, 150, 62);
			g.fillRect(872,669, 120, 52);
			
			g.setColor(Color.black);
			g.drawRect(857,450, 150, 62);
			g.drawRect(857,530, 150, 62);
			g.drawRect(872,669, 120, 52);
			g.drawRect(858,451, 148, 60);
			g.drawRect(858,531, 148, 60);
			g.drawRect(873,670, 118, 50);
			
			g.drawString("LOCAL", 865, 495);
			g.setFont(new Font("Trebuchet", Font.BOLD, 45));
			g.drawString("EXIT", 882, 712);
			g.drawString("CPU", 883, 578);
			
		}
		else if(enterNames)
		{
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 1920, 1080);
			this.add(nameText);
			nameText.setBounds(800,250, 300, 75);
			nameText.setFont(new Font("Times New Roman", Font.PLAIN, 50));
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
				g.drawImage(ImageIO.read(getClass().getResource("plus.png")), 1100,250, 75, 75, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.setFont(new Font("Roboto", Font.BOLD| Font.CENTER_BASELINE, 75));
			g.setColor(new Color(222,219,246));
			g.drawString("PLAYER NAMES", 657, 170);
			
			g.setColor(Color.white);
			g.setFont(new Font("Trebuchet", Font.BOLD, 35));
			g.fillRect(725, 400, 120, 50);
			g.fillRect(1070, 400, 120, 50);
			
			g.setColor(Color.black);
			g.drawRect(725, 400, 120, 50);
			g.drawRect(1070, 400, 120, 50);
//			g.drawRect(726, 401, 118, 48);
//			g.drawRect(1071, 401, 118, 48);
			g.drawString("BACK", 735, 439);
			g.drawString("DONE", 1081, 439);
			
			for(int i = 0;i<names.size();i++)
			{
				g.setColor(Color.white);
				g.setFont(new Font("Times New Roman", Font.ITALIC, 25));
				if(i==0)
				{
					if(hovering1)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Times New Roman", Font.ITALIC |Font.BOLD, 25));
					}
					if(names.get(0).length()>10)
						g.drawString(names.get(0).substring(0,9)+"...", 800, 550);
					else
						g.drawString(names.get(0), 800, 550);
				}
				else if(i==1)
				{
					g.setColor(Color.white);
					g.setFont(new Font("Times New Roman", Font.ITALIC, 25));
					if(hovering2)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Times New Roman", Font.ITALIC |Font.BOLD, 25));
					}
					if(names.get(1).length()>10)
						g.drawString(names.get(1).substring(0,9)+"...", 1010, 550);
					else
						g.drawString(names.get(1), 1010, 550);
				}
				else if(i==2)
				{
					g.setColor(Color.white);
					g.setFont(new Font("Times New Roman", Font.ITALIC, 25));
					if(hovering3)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Times New Roman", Font.ITALIC |Font.BOLD, 25));
					}
					if(names.get(2).length()>10)
						g.drawString(names.get(2).substring(0,9)+"...", 800, 650);
					else
						g.drawString(names.get(2), 800, 650);
				}
				else if(i==3)
				{
					g.setColor(Color.white);
					g.setFont(new Font("Times New Roman", Font.ITALIC, 25));
					if(hovering4)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Times New Roman", Font.ITALIC |Font.BOLD, 25));
					}
					if(names.get(3).length()>10)
						g.drawString(names.get(3).substring(0,9)+"...", 1010, 650);
					else
						g.drawString(names.get(3), 1010, 650);
				}
			}
			
			if(confirm)
			{
				g.setColor(Color.white);
				g.fillRect(790, 375, 320, 365);
				g.setColor(Color.black);
				g.drawRect(790, 375, 320, 365);
				
				g.drawRect(840, 670, 50, 30);
				g.drawRect(1000, 670, 60, 30);
				g.setFont(new Font("Trebuchet", Font.PLAIN, 25));
				g.drawString("You didn't enter 4 names.", 810, 435);
				g.drawString("Continue?", 895, 460);
				
				g.setFont(new Font("Trebuchet", Font.BOLD, 22));
				g.drawString("YES", 1008, 695);
				g.drawString("NO", 849, 695);
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
				if(game.getTurn()==0)
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
			g.fillRect(0, 0, 1920, 1080);
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource("menu.png")), 1715, 10, 150, 100, null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			if(gameEndedBack)
			{
				g.setColor(Color.white);
				g.fillRect(1645, 915, 125, 53);
				g.setColor(Color.black);
				g.drawRect(1645, 915, 125, 53);
				g.setFont(new Font("Trebuchet", Font.BOLD|Font.CENTER_BASELINE, 35));
				g.drawString("FINISH", 1653, 956);
			}
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource("card_back.png")),670,400, cardWidth, cardHeight, null);
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
		    	g.setFont(new Font("Trebuchet", Font.BOLD, 45));
		    	g.drawString("V", 722, 642);
		    	g.setColor(new Color(246,72,72));
		    	g.fillRect(622, 665, 50, 50);
		    	g.setColor(new Color(0,191,255));
		    	g.fillRect(682, 665, 50, 50);
		    	g.setColor(new Color(0,229,145));
		    	g.fillRect(742,665, 50, 50);
		    	g.setColor(Color.yellow);
		    	g.fillRect(802,665, 50, 50);
		    }
		    else if(colorPickerPlay) //played wild card color picker
		    {
		    	g.setColor(Color.black);
		    	g.setFont(new Font("Trebuchet", Font.PLAIN, 65));
		    	g.drawString("^", colorPickerPlayCoord, 810);
		    	g.setColor(new Color(246,72,72));
		    	g.fillRect(colorPickerPlayCoord-102, 690, 50, 50);
		    	g.setColor(new Color(0,191,255));
		    	g.fillRect(colorPickerPlayCoord-102+60, 690, 50, 50);
		    	g.setColor(new Color(0,229,145));
		    	g.fillRect(colorPickerPlayCoord-102+120,690, 50, 50);
		    	g.setColor(Color.yellow);
		    	g.fillRect(colorPickerPlayCoord-102+180,690, 50, 50);
		    }
		    if(menu)
			{
				
				g.setColor(Color.white);
				g.fillRect(1650, 0, 270, 1080);
				
				try {
					g.drawImage(ImageIO.read(getClass().getResource("menu.png")), 1715, 10, 150, 100, null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				g.setColor(Color.black);
				g.drawRect(1700, 115, 100, 45);
				g.drawRect(1650, 0, 267, 1076);
				g.setFont(new Font("Trebuchet", Font.BOLD|Font.CENTER_BASELINE, 30));
				g.drawString("HOME", 1706, 149);
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
			g.setFont(new Font("Arial", Font.BOLD, 11));
			String[]x = new String[8];
			System.arraycopy(history.toArray(), 0, x, 0, history.size());
			for(int i = 0;i<x.length;i++)
			{
				if(x[i]!=null)
					g.drawString(x[i], 25, 25+15*i);
			}	
		}
	}
	public void paintTurnArrow(Graphics g, int turn)
	{
		g.setColor(Color.black);
		g.setFont(new Font("Trebuchet", Font.BOLD, 35));
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource("arrow pointer.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(turn == 0)
		{
			g.drawImage(img, 840, 455, 75, 75, null);
		}
		else if(turn==1)
		{

			g.drawImage(rotateClockwise90(img), 840, 455, 75, 75, null);
		}
		else if(turn==2)
		{
			g.drawImage(rotate180(img), 840, 455, 75, 75, null);
		}
		else
		{

			g.drawImage(rotateClockwise270(img), 840, 455, 75, 75, null);
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
			g.setFont(new Font("Roboto", Font.BOLD, 100));
			g.drawString("<",385, 937);
			
		}
		if (page!=maxPage && arrows)
		{
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(new Font("Roboto", Font.BOLD, 100));
			g.drawString(">",1360, 937);
			
		}
		
		//make arrows for gameEndedBack
	}
	public void paintCPUHands(Graphics g, int x, int y)
	{
		if(game.getTurn()==0)
			game.playerList.get(0).sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());	
		int start = (page-1)*7;
		int end = Math.min(start+7, game.playerList.get(0).getHandSize());
		for(int i = start;i<end;i++)
		{
			String a = "";
			UnoCard card = game.playerList.get(0).getHand().get(i);
			
			if(card.getValue().toString().equals("Reverse"))
				a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
			else if(card.getValue().toString().equals("DrawTwo"))
				a = card.getColor().toString().toLowerCase()+"_picker"+".png";
			else if(card.getValue().toString().equals("Wild"))
				a = "wild_color_changer.png";
			else if(card.getValue().toString().equals("Skip"))
				a = card.getColor().toString().toLowerCase()+"_skip"+".png";
			else if(card.getValue().toString().equals("Wild_Four"))
				a = "wild_pick_four.png";
			else
				a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource(a)),x+120*(i%7),y, cardWidth, cardHeight, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		paintArrows(g, game.playerList.get(0).getHandSize());
		g.setColor(Color.white);
		g.setFont(new Font("Trebuchet", Font.BOLD, 13));
		g.drawString(game.playerList.get(0).getPaintName(), 775, 775);
		g.setColor(Color.DARK_GRAY);
		g.drawString("Cards: "+Integer.toString((game.playerList.get(0).getHandSize())), 925, 775);
		if(gamestate.isUno(game.playerList.get(0)))
		{
			g.setColor(Color.red);
			g.setFont(new Font("Trebuchet", Font.BOLD, 15));
	    	g.drawString("UNO",850,750);
		}
		for(int i =1;i<game.playerList.size();i++)
		{
			Player p = game.playerList.get(i);
			if(i == 2)
			{
				if(gameEndedBack)
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 5);h++)
					{
						String a = "";
						UnoCard card = game.playerList.get(2).getHand().get(h);
						
						if(card.getValue().toString().equals("Reverse"))
							a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
						else if(card.getValue().toString().equals("DrawTwo"))
							a = card.getColor().toString().toLowerCase()+"_picker"+".png";
						else if(card.getValue().toString().equals("Wild"))
							a = "wild_color_changer.png";
						else if(card.getValue().toString().equals("Skip"))
							a = card.getColor().toString().toLowerCase()+"_skip"+".png";
						else if(card.getValue().toString().equals("Wild_Four"))
							a = "wild_pick_four.png";
						else
							a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
						
						try {
							g.drawImage(rotate180(ImageIO.read(getClass().getResource(a))),669+100*h,100, cardWidth, cardHeight, null);
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
							g.drawImage(rotate180(ImageIO.read(getClass().getResource("card_back_alt.png"))),669+50*h,100, cardWidth, cardHeight, null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				g.setColor(Color.white);
				g.setFont(new Font("Trebuchet", Font.BOLD, 13));
				g.drawString(game.playerList.get(2).getPaintName(), 800, 310);
				g.setColor(Color.DARK_GRAY);
				g.drawString("Cards: "+Integer.toString(game.playerList.get(2).getHandSize()), 900, 310);
				if(gamestate.isUno(game.playerList.get(2)))
				{
					g.setColor(Color.red);
					g.setFont(new Font("Trebuchet", Font.BOLD, 15));
			    	g.drawString("UNO",850,350);
				}
			}
			else if (i == 1)
			{
				BufferedImage im = null;
				if(gameEndedBack)
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 5);h++)
					{
						String a = "";
						UnoCard card = game.playerList.get(1).getHand().get(h);
						
						if(card.getValue().toString().equals("Reverse"))
							a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
						else if(card.getValue().toString().equals("DrawTwo"))
							a = card.getColor().toString().toLowerCase()+"_picker"+".png";
						else if(card.getValue().toString().equals("Wild"))
							a = "wild_color_changer.png";
						else if(card.getValue().toString().equals("Skip"))
							a = card.getColor().toString().toLowerCase()+"_skip"+".png";
						else if(card.getValue().toString().equals("Wild_Four"))
							a = "wild_pick_four.png";
						else
							a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
						
						try {
							im = rotateClockwise90(ImageIO.read(getClass().getResource(a)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						g.drawImage(im,200,290+100*h, cardHeight, cardWidth, null);
					}
				}
				else
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
					{
							try {
								im = rotateClockwise90(ImageIO.read(getClass().getResource("card_back_alt.png")));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						g.drawImage(im,200,290+50*h, cardHeight, cardWidth, null);
						
					}
				}
				g.setColor(Color.white);
				g.setFont(new Font("Trebuchet", Font.BOLD, 13));
				g.drawString(game.playerList.get(1).getPaintName(), 400, 500);
				g.setColor(Color.DARK_GRAY);
				g.drawString("Cards: "+Integer.toString(game.playerList.get(1).getHandSize()), 400, 600);
				if(gamestate.isUno(game.playerList.get(1)))
				{
					g.setColor(Color.red);
					g.setFont(new Font("Trebuchet", Font.BOLD, 15));
			    	g.drawString("UNO",450,550);
				}
			}
			else if(i==3)
			{
				BufferedImage im = null;
				
				if(gameEndedBack)
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 5);h++)
					{
						String a = "";
						UnoCard card = game.playerList.get(3).getHand().get(h);
						
						if(card.getValue().toString().equals("Reverse"))
							a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
						else if(card.getValue().toString().equals("DrawTwo"))
							a = card.getColor().toString().toLowerCase()+"_picker"+".png";
						else if(card.getValue().toString().equals("Wild"))
							a = "wild_color_changer.png";
						else if(card.getValue().toString().equals("Skip"))
							a = card.getColor().toString().toLowerCase()+"_skip"+".png";
						else if(card.getValue().toString().equals("Wild_Four"))
							a = "wild_pick_four.png";
						else
							a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
						
						try {
							im = rotateClockwise270(ImageIO.read(getClass().getResource(a)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						g.drawImage(im,1480,290+100*h, cardHeight, cardWidth, null);
					}

				}
				else
					for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
					{
						
						try {
							im = rotateClockwise270(ImageIO.read(getClass().getResource("card_back_alt.png")));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
						g.drawImage(im,1480,290+50*h, cardHeight, cardWidth, null);
					}
				g.setColor(Color.white);
				g.setFont(new Font("Trebuchet", Font.BOLD, 13));
				g.drawString(game.playerList.get(3).getPaintName(),1370 ,500);
				g.setColor(Color.DARK_GRAY);
				g.drawString("Cards: "+Integer.toString(game.playerList.get(3).getHandSize()), 1370, 600);
				if(gamestate.isUno(game.playerList.get(3)))
				{
					g.setColor(Color.red);
					g.setFont(new Font("Trebuchet", Font.BOLD, 15));
			    	g.drawString("UNO",1320,550);
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
			String a = "";
			UnoCard card = game.current_player.getHand().get(i);
			
			if(card.getValue().toString().equals("Reverse"))
				a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
			else if(card.getValue().toString().equals("DrawTwo"))
				a = card.getColor().toString().toLowerCase()+"_picker"+".png";
			else if(card.getValue().toString().equals("Wild"))
				a = "wild_color_changer.png";
			else if(card.getValue().toString().equals("Skip"))
				a = card.getColor().toString().toLowerCase()+"_skip"+".png";
			else if(card.getValue().toString().equals("Wild_Four"))
				a = "wild_pick_four.png";
			else
				a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
			
			try {
				g.drawImage(ImageIO.read(getClass().getResource(a)),x+120*(i%7),y, cardWidth, cardHeight, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		paintArrows(g, game.current_player.getHandSize());
		g.setColor(Color.white);
		g.setFont(new Font("Trebuchet", Font.BOLD, 13));
		g.drawString(game.current_player.getPaintName(), 775, 775);
		g.setColor(Color.DARK_GRAY);
		g.drawString("Cards: "+Integer.toString((game.current_player.getHandSize())), 925, 775);
		if(gamestate.isUno(game.current_player))
		{
			g.setColor(Color.red);
			g.setFont(new Font("Trebuchet", Font.BOLD, 15));
	    	g.drawString("UNO",850,750);
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
							String a = "";
							UnoCard card = p.getHand().get(h);
							p.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());
							
							if(card.getValue().toString().equals("Reverse"))
								a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
							else if(card.getValue().toString().equals("DrawTwo"))
								a = card.getColor().toString().toLowerCase()+"_picker"+".png";
							else if(card.getValue().toString().equals("Wild"))
								a = "wild_color_changer.png";
							else if(card.getValue().toString().equals("Skip"))
								a = card.getColor().toString().toLowerCase()+"_skip"+".png";
							else if(card.getValue().toString().equals("Wild_Four"))
								a = "wild_pick_four.png";
							else
								a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
							
							try {
								g.drawImage(rotate180(ImageIO.read(getClass().getResource(a))),669+100*h,100, cardWidth, cardHeight, null);
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
								g.drawImage(rotate180(ImageIO.read(getClass().getResource("card_back_alt.png"))),669+50*h,100, cardWidth, cardHeight, null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, 13));
					g.drawString(game.playerList.get((game.getTurn()+2)%4).getPaintName(), 800, 310);
					g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get((game.getTurn()+2)%4).getHandSize()), 900, 310);
					if(gamestate.isUno(game.playerList.get((game.getTurn()+2)%4)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, 15));
				    	g.drawString("UNO",850,350);
					}
				}
				else if ( i == (game.getTurn()+1)%4)
				{
					BufferedImage im = null;
					
					if(gameEndedBack)
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
						{
							String a = "";
							UnoCard card = p.getHand().get(h);
							p.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());
							
							if(card.getValue().toString().equals("Reverse"))
								a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
							else if(card.getValue().toString().equals("DrawTwo"))
								a = card.getColor().toString().toLowerCase()+"_picker"+".png";
							else if(card.getValue().toString().equals("Wild"))
								a = "wild_color_changer.png";
							else if(card.getValue().toString().equals("Skip"))
								a = card.getColor().toString().toLowerCase()+"_skip"+".png";
							else if(card.getValue().toString().equals("Wild_Four"))
								a = "wild_pick_four.png";
							else
								a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
							
							try {
								im = rotateClockwise90(ImageIO.read(getClass().getResource(a)));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							g.drawImage(im,200,290+100*h, cardHeight, cardWidth, null);
						}
					}
					else
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
						{
							try {
								im = rotateClockwise90(ImageIO.read(getClass().getResource("card_back_alt.png")));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							g.drawImage(im,200,290+50*h, cardHeight, cardWidth, null);
							
						}
					}
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, 13));
					g.drawString(game.playerList.get((game.getTurn()+1)%4).getPaintName(), 400, 500);
					g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get((game.getTurn()+1)%4).getHandSize()), 400, 600);
					if(gamestate.isUno(game.playerList.get((game.getTurn()+1)%4)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, 15));
				    	g.drawString("UNO",450,550);
					}
				}
				else if(i==(game.getTurn()+3)%4)
				{
					BufferedImage im = null;
					
					if(gameEndedBack)
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 5);h++)
						{
							String a = "";
							UnoCard card = p.getHand().get(h);
							p.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());
							
							if(card.getValue().toString().equals("Reverse"))
								a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
							else if(card.getValue().toString().equals("DrawTwo"))
								a = card.getColor().toString().toLowerCase()+"_picker"+".png";
							else if(card.getValue().toString().equals("Wild"))
								a = "wild_color_changer.png";
							else if(card.getValue().toString().equals("Skip"))
								a = card.getColor().toString().toLowerCase()+"_skip"+".png";
							else if(card.getValue().toString().equals("Wild_Four"))
								a = "wild_pick_four.png";
							else
								a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
							
							try {
								im = rotateClockwise270(ImageIO.read(getClass().getResource(a)));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							g.drawImage(im,1480,290+100*h, cardHeight, cardWidth, null);
						}
					}
					else
					{
						for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
						{
							try {
								im = rotateClockwise270(ImageIO.read(getClass().getResource("card_back_alt.png")));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
							g.drawImage(im,1480,290+50*h, cardHeight, cardWidth, null);
						}
					}
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, 13));
					g.drawString(game.playerList.get((game.getTurn()+3)%4).getPaintName(),1370 ,500);
					g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get((game.getTurn()+3)%4).getHandSize()), 1370, 600);
					if(gamestate.isUno(game.playerList.get((game.getTurn()+3)%4)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, 15));
				    	g.drawString("UNO",1320,550);
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
		String a = "";
		UnoCard card = game.deck.discard.get(game.deck.discard.size()-1);
		
		if(card.getValue().toString().equals("Reverse"))
			a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
		else if(card.getValue().toString().equals("DrawTwo"))
			a = card.getColor().toString().toLowerCase()+"_picker"+".png";
		else if(card.getValue().toString().equals("Wild"))
			a = "wild_color_changer.png";
		else if(card.getValue().toString().equals("Skip"))
			a = card.getColor().toString().toLowerCase()+"_skip.png";
		else if(card.getValue().toString().equals("Wild_Four"))
			a = "wild_pick_four.png";
		else 
			a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
		
		try {
			g.drawImage(ImageIO.read(getClass().getResource(a)),x,y, cardWidth, cardHeight, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void paintExtra(Graphics g, String direction, String color)
	{
		int x = 817;
		int y = 560;
		//g.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		g.setColor(Color.white);
		//g.drawString(direction, x, y);
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource("direction arrow.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(direction.contains("Counter"))
			g.drawImage(img, x+125, y, -125, 75, null);
		else
			g.drawImage(img, x, y, 125, 75, null);
		
			
		x = 951;
		y = 587;
		
		g.setFont(new Font("Roboto", Font.BOLD, 30));
		if(color.equals("Yellow"))
			g.setColor(Color.YELLOW);
		else if(color.equals("Blue"))
			g.setColor(new Color(0,191,255));
		else if(color.equals("Red"))
			g.setColor(new Color(246,72,72));
		else
			g.setColor(new Color(0,229,145));
		
		g.fillRect(x, y,cardWidth, 7);
	}
	
	public void paintEndText(Graphics g)
	{
		int x = 555+((Math.max(5-game.getLastTurn().getPaintName().length(), 0))*40);
		int y = 350;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,1920,1080);
		
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,75));
		g.setColor(Color.RED);
		g.drawString(game.current_player.getPaintName()+" is the winner!!!", x, y);
		
		g.setColor(Color.white);
//		790 && e.getX()<=1100 && e.getY()>=480 && e.getY()<=590
		//
		g.drawRect(785, 491, 310, 80);
		g.drawRect(804, 641, 275, 80);
		g.fillRect(880, 395, 110, 50);
		g.setColor(Color.black);
		g.drawRect(880, 395, 110, 50);
		g.drawRect(881, 396, 108, 48);
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,50));
		g.setColor(Color.green);
		g.drawString("NEW GAME", 800, 550);
		g.drawString("REMATCH", 818, 700);
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,35));
		g.setColor(Color.black);
		g.drawString("BACK", 887, 435);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
			
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("X: "+e.getX()+" Y: "+e.getY());
		
		if(e.getX()>= 1760 && e.getX()<=1810 && e.getY()>=40 && e.getY()<= 75 && game.started) //open menu
		{
			menu = !menu;
			repaint();
		}
		
		
		else if(e.getX()>= 1675 && e.getX()<=1770 && e.getY()>=115 && e.getY()<= 160 && game.started && menu) //go to home
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
			if(e.getX()>= 622 && e.getX()<=672 && e.getY()>=665 && e.getY()<=715 && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("red");
				game.nextTurn();
				
	    		page = 1;
	    	    addHistory(game.getLastTurn().getName()+" drew a card.");
	    	  
	    	    colorPickerDraw = false;
	    	    repaint();
			}
			else if(e.getX()>= 682 && e.getX()<=732 && e.getY()>=665 && e.getY()<=715 && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("blue");
				game.nextTurn();
				
	    		page = 1;
	    	    addHistory(game.getLastTurn().getName()+" drew a card.");
	    	  
	    	    colorPickerDraw = false;
	    	    repaint();
			}
			else if(e.getX()>= 742 && e.getX()<=792 && e.getY()>=665 && e.getY()<=715 && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("green");
				game.nextTurn();
				
	    		page = 1;
	    	    addHistory(game.getLastTurn().getName()+" drew a card.");
	    	   
	    	    colorPickerDraw = false;
	    	    repaint();
			}
			else if(e.getX()>= 802 && e.getX()<=852 && e.getY()>=665 && e.getY()<=715 && colorPickerDraw)
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
			if(e.getX()>= colorPickerPlayCoord-102 && e.getX()<=colorPickerPlayCoord-102+50 && e.getY()>=690 && e.getY()<=740 && colorPickerPlay)
			{
				game.play(wildIndex);
				game.changeColor("red");
				game.nextTurn();
	    		page = 1;
	    		addHistory(game.getLastPlayerTurn("play"));
	    		
	    	    colorPickerPlay = false;
	    	    repaint();
			}
			else if(e.getX()>= colorPickerPlayCoord-102+60 && e.getX()<=colorPickerPlayCoord-102+60+50 && e.getY()>=690 && e.getY()<=740 && colorPickerPlay)
			{
				game.play(wildIndex);
				game.changeColor("blue");
				game.nextTurn();
	    		page = 1;
	    		addHistory(game.getLastPlayerTurn("play"));
	    		
	    	    colorPickerPlay = false;
	    	    repaint();
			}
			else if(e.getX()>= colorPickerPlayCoord-102+120 && e.getX()<=colorPickerPlayCoord-102+120+50 && e.getY()>=690 && e.getY()<=740 && colorPickerPlay)
			{
				game.play(wildIndex);
				game.changeColor("green");
				game.nextTurn();
	    		page = 1;
	    		addHistory(game.getLastPlayerTurn("play"));
	    		
	    	    colorPickerPlay = false;
	    	    repaint();
			}
			else if(e.getX()>= colorPickerPlayCoord-102+180 && e.getX()<=colorPickerPlayCoord-102+180+50 && e.getY()>=690 && e.getY()<=740 && colorPickerPlay)
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
				if(e.getX()>= 840 && e.getX()<=890 && e.getY()>=670 && e.getY()<=700 && enterNames && confirm)
				{
					this.remove(nameText);
					confirm = false;
					repaint();
					
				}
				else if(e.getX()>= 1000 && e.getX()<=1060 && e.getY()>=670 && e.getY()<=700 && enterNames && confirm)
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
				if(e.getX()>= 865 && e.getX()<=1015 && e.getY()>=450 && e.getY()<=512 && start)
				{
					start = false;
					enterNames = true;
					//game.start(names);
					repaint();
				}
				else if(e.getX()>= 865 && e.getX()<=1015 && e.getY()>=530 && e.getY()<=592 && start)
				{
					start = false;
					cpu = true;
					game.start(cpu);
					repaint();
				}
				else if(e.getX()>= 869 && e.getX()<=992 && e.getY()>=669 && e.getY()<=722 && start)
				{
					frame.setVisible(false); 
					frame.dispose();
				}
				
				
				//entering names screen
				if(e.getX()>= 1110 && e.getX()<=1160 && e.getY()>=260 && e.getY()<=313 && enterNames && names.size()<4) //add name (plus png)
				{
					this.remove(nameText);
					if(nameText.getText().replace(" ", "").length()>0)
						names.add(nameText.getText());
					nameText.setText("");
					repaint();
				}
				else if(e.getX()>= 725 && e.getX()<=845 && e.getY()>=400 && e.getY()<=450 && enterNames)// back button
				{
					this.remove(nameText);
					enterNames = false;
					start = true;
					names.clear();
					repaint();
				}
				else if(e.getX()>= 1070 && e.getX()<=1190 && e.getY()>=400 && e.getY()<=450 && enterNames)// done button
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

				else if(names.size()>0 && e.getX()>= 800 && e.getX()<=800+(Math.min(13*names.get(0).length(), 12*10)) && e.getY()>=530 && e.getY()<=550 && enterNames)//delete first name
				{
					
					this.remove(nameText);
					names.remove(0);
					repaint();
				}
				else if(names.size()>1&& e.getX()>= 1010 && e.getX()<=1010+(Math.min(13*names.get(1).length(), 12*10)) && e.getY()>=530 && e.getY()<=550 && enterNames)//second name
				{
					
					this.remove(nameText);
					names.remove(1);
					repaint();
				}
				else if(names.size()>2&& e.getX()>= 800 && e.getX()<=800+(Math.min(13*names.get(2).length(), 12*10)) && e.getY()>=630 && e.getY()<=650 && enterNames)//third name
				{
					this.remove(nameText);
					names.remove(2);
					repaint();
				}
				else if(names.size()>3&& e.getX()>= 1010 && e.getX()<=1010+(Math.min(13*names.get(3).length(), 12*10)) && e.getY()>=630 && e.getY()<=650 && enterNames)//fourth name
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
				if(e.getX()>= 1645 && e.getX()<=1770 && e.getY()>=915 && e.getY()<=969) //go back to end screen from uno board
				{
					gameEndedBack = false;
					repaint();
				}
			}
			else
			{
				if(e.getX()>= 785 && e.getX()<=1095 && e.getY()>=491 && e.getY()<=571)//new game
				{
					game = new Board();
					gamestate = new GameState(game);
					frame.setVisible(false);
					frame.dispose();
					UnoGraphics g = new UnoGraphics(game, gamestate);
				}
				else if(e.getX()>= 804 && e.getX()<=1080 && e.getY()>=641 && e.getY()<=721) //rematch
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
				else if(e.getX()>= 880 && e.getX()<=990 && e.getY()>=395 && e.getY()<=445) //see uno board
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
					if(e.getX()>= 670 && e.getX()<=800 && e.getY()>=400 && e.getY()<=580)
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
					
					if(e.getX()>= 475 && e.getX()<=595 && e.getY()>=800 && e.getY()<=980)
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
					else if(e.getX()>= 595 && e.getX()<=715 && e.getY()>=800 && e.getY()<=980)
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
					else if(e.getX()>= 715 && e.getX()<=835 && e.getY()>=800 && e.getY()<=980)
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
					else if(e.getX()>= 835 && e.getX()<=955 && e.getY()>=800 && e.getY()<=980)
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
					else if(e.getX()>= 955 && e.getX()<=1075 && e.getY()>=800 && e.getY()<=980)
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
					else if(e.getX()>= 1075 && e.getX()<=1195 && e.getY()>=800 && e.getY()<=980)
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
					else if(e.getX()>= 1195 && e.getX()<=1315 && e.getY()>=800 && e.getY()<=980)
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
					
				if(e.getX()>= 385 && e.getX()<= 440 && e.getY()>=850 && e.getY()<=937 && page>1)
				{
					page-=1;
					repaint();
				}
				else if(e.getX()>= 1360 && e.getX()<=1415 && e.getY()>=850 && e.getY()<=937 && page<maxPage)
				{
					page+=1;
					repaint();
				}
//				else if(e.getX()>= 385 && e.getX()<= 440 && e.getY()>=850 && e.getY()<=937 && page2>1 && gameEndedBack)
//				{
//					page2-=1;
//					repaint();
//				}
//				else if(e.getX()>= 1360 && e.getX()<=1415 && e.getY()>=850 && e.getY()<=937 && page2<maxPage2 && gameEndedBack)
//				{
//					page2+=1;
//					repaint();
//				}
//				else if(e.getX()>= 385 && e.getX()<= 440 && e.getY()>=850 && e.getY()<=937 && page3>1 && gameEndedBack)
//				{
//					page3-=1;
//					repaint();
//				}
//				else if(e.getX()>= 1360 && e.getX()<=1415 && e.getY()>=850 && e.getY()<=937 && page3<maxPage3 && gameEndedBack)
//				{
//					page3+=1;
//					repaint();
//				}
//				else if(e.getX()>= 385 && e.getX()<= 440 && e.getY()>=850 && e.getY()<=937 && page4>1 && gameEndedBack)
//				{
//					page4-=1;
//					repaint();
//				}
//				else if(e.getX()>= 1360 && e.getX()<=1415 && e.getY()>=850 && e.getY()<=937 && page4<maxPage4 && gameEndedBack)
//				{
//					page4+=1;
//					repaint();
//				}
				
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("HOVERING X: "+e.getX()+" Y: "+e.getY());
		if(enterNames && !confirm && e.getX()>= 725 && e.getX()<=1200 && e.getY()>=475 && e.getY()<=700)
		{
			if(names.size()>0 && e.getX()>= 800 && e.getX()<=800+(Math.min(13*names.get(0).length(), 12*10))&& e.getY()>=530 && e.getY()<=550)//first name
			{
				
				this.remove(nameText);
				hovering1 = true;
				repaint();
			}
			else if(names.size()>1 && e.getX()>= 1010 && e.getX()<=1010+(Math.min(12*names.get(1).length(), 12*10)) && e.getY()>=530 && e.getY()<=550)//second name
			{
				
				this.remove(nameText);
				hovering2 = true;
				repaint();
			}
			else if(names.size()>2 && e.getX()>= 800 && e.getX()<=800+(Math.min(12*names.get(2).length(), 12*10)) && e.getY()>=630 && e.getY()<=650)//third name
			{
				
				this.remove(nameText);
				hovering3 = true;
				repaint();
			}
			else if(names.size()>3 && e.getX()>= 1010 && e.getX()<=1010+(Math.min(12*names.get(3).length(), 12*10)) && e.getY()>=630 && e.getY()<=650)//fourth name
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
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

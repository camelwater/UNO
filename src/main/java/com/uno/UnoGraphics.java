package com.uno;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.Timer;
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
	
	//private Color bgColor = new Color(0,138,138).darker();
	private Color bgColor = Color.DARK_GRAY;
	private ArrayList<Color> bgOptions = new ArrayList<Color>();
	private boolean bgOptionBox = false;
	
	private boolean colorPickerDraw = false;
	private boolean colorPickerPlay = false;
	private int colorPickerPlayCoord = 540;
	private int wildIndex = 0;
	private int hoveringCard = -1;
	private int drawClick = 640;
	
	private int hoveringCard0 = 0;
	private int hoveringCard1 = 0;
	private int hoveringCard2 = 0;
	private int hoveringCard3 = 0;
	private int hoveringCard4 = 0;
	private int hoveringCard5 = 0;
	private int hoveringCard6 = 0;
	
	
	private boolean start = true;
	private boolean enterNames = false;
	private boolean cpu = false;
	private boolean LAN = false;
	private boolean settings = false;
	
	private boolean arrows = true;
	private boolean gameEndedBack = false;
	private boolean menu = false;
	
	private boolean laxWildCard = false;
	private boolean infiniteDraw = false;
	
	private boolean repaintHover = false;
	private boolean repaintHoverNames = false;
	private String hoverStart = "none";
	private boolean hovering1 = false;
	private boolean hovering2 = false;
	private boolean hovering3 = false;
	private boolean hovering4 = false;
	private boolean confirm = false;
	private boolean dupName = false;
	private boolean notEnoughPlayers = false;
	
	private JTextField nameText = new JTextField(20);
	
	private ArrayList<String> names = new ArrayList<String>();
	private Queue<String> history = new LinkedList<String>();
	
	AbstractAction action = new AbstractAction()
	{
	    //@Override
	    public void actionPerformed(ActionEvent e)
	    {
	    	if(enterNames)
	    	{
	    		if(names.contains(nameText.getText()))
	    		{
	    			dupName = true;
	    			repaint();
	    		}
	    		else if(names.size()<4 && nameText.getText().replace(" ", "").length()>0)
	    		{
		    		names.add(nameText.getText());
		    		nameText.setText("");
		    		repaint();
	    		}
	    	}
	    }
	};
	ActionListener cpuTask = new ActionListener() {
	    public void actionPerformed(ActionEvent evt) 
	    {
	        cpuTurn();
	    }
	};
	private Timer timer = new Timer(750, cpuTask);
	
	
	public UnoGraphics(Board game, GameState gamestate) 
	{
		frame = new JFrame("UNO");
		this.game = game;
		this.gamestate = gamestate;
		bgOptions.add(new Color(0,138,138).darker());
		bgOptions.add(Color.LIGHT_GRAY.darker());
		bgOptions.add(new Color(222, 213, 242));
		bgOptions.add(new Color(240, 194, 199));
		bgOptions.add(new Color(207, 255, 245));
		bgOptions.add(new Color(22, 71, 53));
		
		timer.setRepeats(false);
		
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
	
	public void paint(Graphics g)
	{
		Rectangle r = g.getClipBounds();
		if(repaintHover||repaintHoverNames)
		{
			g.setColor(bgColor);
			g.fillRect(r.x, r.y, r.width, r.height);
			if(repaintHover)
			{
				hoveringCard0 = hoveringCard==0?-60:0;
				hoveringCard1 = hoveringCard==1?-60:0;
				hoveringCard2 = hoveringCard==2?-60:0;
				hoveringCard3 = hoveringCard==3?-60:0;
				hoveringCard4 = hoveringCard==4?-60:0;
				hoveringCard5 = hoveringCard==5?-60:0;
				hoveringCard6 = hoveringCard==6?-60:0;
				
				int x = 490;
				int y = 800;
				if(!cpu)
				{
					game.current_player.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());	
					int start = (page-1)*7;
					int end = Math.min(start+7, game.current_player.getHandSize());
					int hoveringIndex = hoveringCard +7*(page-1);
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
						if(hoveringCard>-1 && i==hoveringIndex)
						{
							try {
								g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y-60), xs(cardWidth), ys(cardHeight), null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							try {
								g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y), xs(cardWidth), ys(cardHeight), null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					//paintArrows(g, game.current_player.getHandSize());
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
					if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
						g.setColor(Color.DARK_GRAY.darker());
					if(hoveringCard == 2)
						g.drawString(game.current_player.getPaintName(), x(775), y(775-60));
					else
						g.drawString(game.current_player.getPaintName(), x(775), y(775));
					if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
						g.setColor(Color.LIGHT_GRAY);
					else
						g.setColor(Color.DARK_GRAY);
					if(hoveringCard == 3 || hoveringCard==4)
						g.drawString("Cards: "+Integer.toString((game.current_player.getHandSize())), x(925), y(775-60));
					else
						g.drawString("Cards: "+Integer.toString((game.current_player.getHandSize())), x(925), y(775));
					if(gamestate.isUno(game.current_player))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
				    	g.drawString("UNO",x(850),y(750));
					}
				}
				else
				{
					if(game.getTurn()==0)
						game.playerList.get(0).sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());	
					int start = (page-1)*7;
					int end = Math.min(start+7, game.playerList.get(0).getHandSize());
					int hoveringIndex = hoveringCard +7*(page-1);
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
						if(hoveringCard>-1 && i==hoveringIndex)
						{
							try {
								g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y-60), xs(cardWidth), ys(cardHeight), null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							try {
								g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y), xs(cardWidth), ys(cardHeight), null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
					//paintArrows(g, game.playerList.get(0).getHandSize());
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
					if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
						g.setColor(Color.DARK_GRAY.darker());
					if(hoveringCard==2)
						g.drawString(game.playerList.get(0).getPaintName(), x(775), y(775-60));
					else
						g.drawString(game.playerList.get(0).getPaintName(), x(775), y(775));
					if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
						g.setColor(Color.LIGHT_GRAY);
					else
						g.setColor(Color.DARK_GRAY);
					if(hoveringCard==3||hoveringCard==4)
						g.drawString("Cards: "+Integer.toString((game.playerList.get(0).getHandSize())), x(925), y(775-60));
					else
						g.drawString("Cards: "+Integer.toString((game.playerList.get(0).getHandSize())), x(925), y(775));
					if(gamestate.isUno(game.playerList.get(0)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
				    	g.drawString("UNO",x(850),y(750));
					}
				}
			}
			else
			{
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
			}
			
			repaintHover = false;
			repaintHoverNames = false;
		}
		else
		{
			if(start)
			{
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, xs(1920), ys(1080));
				g.setColor(Color.red);
				g.setFont(new Font("Trebuchet", Font.BOLD, font(100)));
				g.drawString("UNO", x(823), y(175));
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
				g.fillRect(x(857), y(610), xs(150), ys(62)); //1007
				g.fillRect(x(857), y(690), xs(150), ys(62));
				g.fillRect(x(872),y(799), xs(120), ys(52));
				
				Color outlineColor = hoverStart.equals("local")?Color.red:Color.black;
				g.setColor(outlineColor);
				g.drawRect(x(857),y(450), xs(150), ys(62));
				g.drawRect(x(857)+1,y(450)+1, xs(150-2), ys(62-2));
				
				outlineColor = hoverStart.equals("cpu")?Color.red:Color.black;
				g.setColor(outlineColor);
				g.drawRect(x(857),y(530), xs(150), ys(62));
				g.drawRect(x(857)+1,y(530)+1, xs(150-2), ys(62-2));
				
				outlineColor = hoverStart.equals("lan")?Color.red:Color.black;
				g.setColor(outlineColor);
				g.drawRect(x(857), y(610), xs(150), ys(62));
				g.drawRect(x(857)+1,y(610)+1, xs(150-2), ys(62-2));
				
				outlineColor = hoverStart.equals("options")?Color.red:Color.black;
				g.setColor(outlineColor);
				g.drawRect(x(857), y(690), xs(150), ys(62));
				g.drawRect(x(857)+1,y(690)+1, xs(150-2), ys(62-2));
				
				outlineColor = hoverStart.equals("exit")?Color.red:Color.black;
				g.setColor(outlineColor);
				g.drawRect(x(872),y(799), xs(120), ys(52));
				g.drawRect(x(872)+1,y(799)+1, xs(120-2), ys(52-2));
				
				
				
				
				
				Color tColor = hoverStart.equals("local")?Color.red:Color.black;
				g.setColor(tColor);
				g.drawString("LOCAL", x(865), y(495));
				g.setFont(new Font("Trebuchet", Font.BOLD, font(45)));
				tColor = hoverStart.equals("exit")?Color.red:Color.black;
				g.setColor(tColor);
				g.drawString("EXIT", x(882), y(842));
				tColor = hoverStart.equals("cpu")?Color.red:Color.black;
				g.setColor(tColor);
				g.drawString("CPU", x(883), y(578));
				tColor = hoverStart.equals("lan")?Color.red:Color.black;
				g.setColor(tColor);
				g.drawString("LAN", x(886), y(658));
				
				g.setFont(new Font("Tecbuchet", Font.BOLD, font(32)));
				tColor = hoverStart.equals("options")?Color.red:Color.black;
				g.setColor(tColor);
				g.drawString("OPTIONS", x(864), y(734));
				
			}
			else if(enterNames) //NEED TO ADD CLEAR BUTTON TO CLEAR NAMES
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
				g.setFont(new Font("Trebuchet", Font.BOLD, font(32)));
				g.drawString("START", x(1079), y(438));
				
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
				
				if(dupName)
				{
					nameText.setEnabled(false);
					g.setColor(Color.white);
					g.fillRect(x(770), y(375), xs(360), ys(365));
					g.setColor(Color.black);
					g.drawRect(x(770), y(375), xs(360), ys(365));
					
					g.drawRect(x(915), y(660), xs(70), ys(40));
					g.setFont(new Font("Trebuchet", Font.PLAIN, font(25)));
					g.drawString("That name is already taken.", x(800), y(435));
					g.drawString("Please choose another name.", x(785), y(465));
					
					g.setFont(new Font("Trebuchet", Font.BOLD, font(35)));
					g.drawString("OK", x(925), y(693));
					
				}
				if(notEnoughPlayers)
				{
					nameText.setEnabled(false);
					g.setColor(Color.white);
					g.fillRect(x(770), y(375), xs(360), ys(365));
					g.setColor(Color.black);
					g.drawRect(x(770), y(375), xs(360), ys(365));
					
					g.drawRect(x(915), y(660), xs(70), ys(40));
					g.setFont(new Font("Trebuchet", Font.PLAIN, font(25)));
					g.drawString("There are not enough players.", x(785), y(435));
					g.drawString("2-4 players are required.", x(825), y(465));
					
					g.setFont(new Font("Trebuchet", Font.BOLD, font(35)));
					g.drawString("OK", x(925), y(693));
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
					g.drawString("This game will only have", x(810), y(435));
					g.drawString("2 players.", x(899), y(465));
					g.drawString("Continue?", x(895), y(495));
					
					g.setFont(new Font("Trebuchet", Font.BOLD, font(22)));
					g.drawString("YES", x(1008), y(695));
					g.drawString("NO", x(849), y(695));
				}
				
				
			}
			else if (LAN)
			{
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, xs(1920), ys(1080));
				
				g.setColor(Color.white);
				g.fillRect(x(960-75-35),y(500),xs(150),ys(75));
				g.fillRect(x(960-112-35),y(400),xs(225),ys(75));
				g.fillRect(x(960-75-35),y(700),xs(150),ys(65));
				
				g.setColor(Color.black);
				g.drawRect(x(960-75-35),y(500),xs(150),ys(75));
				g.drawRect(x(960-112-35),y(400),xs(225),ys(75));
				g.drawRect(x(960-75-35),y(700),xs(150),ys(65));
				
				g.setFont(new Font("Trebuchet", Font.BOLD, font(47)));
				g.drawString("JOIN", x(960-75-35+22), y(555));
				g.drawString("CREATE", x(960-112-35+17), y(455));
				g.setFont(new Font("Trebuchet", Font.BOLD |Font.ROMAN_BASELINE, font(47)));
				g.drawString("BACK", x(960-75-35+10), y(750));
				
				g.setFont(new Font("Trebuchet", Font.BOLD, font(85)));
				g.drawString("LAN GAME", x(690), y(225));
				
			}
			else if(settings)
			{
				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, xs(1920), ys(1080));
				
				g.setColor(Color.black);
				g.setFont(new Font("Trebuchet", Font.BOLD, font(85)));
				g.drawString("OPTIONS", x(745), y(175));
				
				g.setFont(new Font("Trebuchet", Font.BOLD, font(45)));
				g.drawString("Infinite Draw", x(730), y(300));
				g.drawString("Lax Wild Four", x(730), y(400));
				g.setFont(new Font("Trebuchet", Font.BOLD, font(37)));
				g.drawString("Background Color", x(730), y(500));
				
				if(infiniteDraw)
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource("/toggle icon.png")), x(1100), y(250), xs(75), ys(75), null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					try {
						g.drawImage((ImageIO.read(getClass().getResource("/toggle icon off.png"))), x(1100), y(250), xs(75), ys(75), null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(laxWildCard)
				{
					try {
						g.drawImage(ImageIO.read(getClass().getResource("/toggle icon.png")), x(1100), y(350), xs(75), ys(75), null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else 
				{
					try {
						g.drawImage((ImageIO.read(getClass().getResource("/toggle icon off.png"))), x(1100), y(350), xs(75), ys(75), null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				g.setColor(bgColor);
				g.fillRect(x(1100), y(472), xs(75), ys(35));
				g.setColor(Color.black);
				g.drawRect(x(1100), y(472), xs(75), ys(35));
				g.fillPolygon(new int[] {x(1185), x(1207), x(1196)}, new int[] {y(483),y(483), y(498)}, 3);
				
				if(bgOptionBox)
				{
					g.setColor(Color.white);
					
					g.fillPolygon(new int[] {x(1145), x(1120), x(1170)}, new int[] {y(512),y(527), y(527)}, 3);
					g.fillRect(x(1000), y(525), xs(300), ys(65));
					
					for(int i = 0;i<bgOptions.size();i++)
					{
						g.setColor(bgOptions.get(i));
						g.fillRect(x(1009+50*i), y(540), xs(35), ys(35));
					}
				}
				
				g.setColor(Color.white);
				g.fillRect(x(960-75-35), y(650), xs(150), ys(60));
				g.setColor(Color.black);
				g.drawRect(x(960-75-35), y(650), xs(150), ys(60));
				g.setFont(new Font("Trebuchet", Font.BOLD, font(47)));
				g.drawString("BACK", x(960-75-35+9), y(697));
				
				
				
				
			}
			else if(gamestate.isOver() && !gameEndedBack) //game over
			{
				paintEndText(g);
			}
			else //game playing in progress
			{
				this.remove(nameText);
				
				if(colorPickerPlay || colorPickerDraw)
					hoveringCard = -1;
				if(cpu)
					arrows = (gameEndedBack||game.getTurn()==0)?true:false;
	
				g.setColor(bgColor);
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
				
				if(game.deck.deck.size()>0)
				{
					int max = Math.min(35, game.deck.deck.size());
					for (int i = 0;i<max;i++)
					{
						try {
							g.drawImage(ImageIO.read(getClass().getResource("/card_back.png")),x(640+i*1),y(400), xs(cardWidth), ys(cardHeight), null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(i==max-1)
							drawClick = 640+i*1;
					}
				}
				
				paintDiscard(g,950, 400);
				
//				hoveringCard0 = hoveringCard==0?-60:0;
//				hoveringCard1 = hoveringCard==1?-60:0;
//				hoveringCard2 = hoveringCard==2?-60:0;
//				hoveringCard3 = hoveringCard==3?-60:0;
//				hoveringCard4 = hoveringCard==4?-60:0;
//				hoveringCard5 = hoveringCard==5?-60:0;
//				hoveringCard6 = hoveringCard==6?-60:0;
				
				if(cpu)
				{
					paintCPUHands(g, 490, 800);
					paintTurnArrow(g, game.getTurn());
				}
				else
			    	paintHands(g,490, 800);
				
			    paintExtra(g,game.getCurrentDirection(), game.showTopCard().getColor().toString());
			    paintHistory(g);
			    
			    
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
			    	colorPickerPlayCoord = 540+120*(wildIndex%7);
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
					g.drawString("QUIT", x(1715), y(149));
				}
	
				if(cpu && !(colorPickerPlay || colorPickerDraw) && !gamestate.isOver() && !gameEndedBack && !game.cpuActive && !timer.isRunning())
			    {
					
					//int next = game.getCurrentDirection().equals("Clockwise")?3:1;
							
			    	if(game.current_player!=game.playerList.get(0))
			    	{
			    		System.out.println("PLAYER: "+game.current_player.name);
			    		timer.start();
			    		//repaint();
			    	}
			    }
				
			}
		}
	}
	public void cpuTurn()
	{
		if(game.current_player==game.playerList.get(0))
			return;
		game.cpuTurn();
		if(game.lastMove.contains("draw"))
			addHistory(game.getLastPlayerTurn("draw"));
		else
			addHistory(game.getLastPlayerTurn("play"));
		game.lastDraw=0;
		repaint();
	}
	public void skipStart()
	{
		start = false;
	}
	public void setCPU(boolean x)
	{
		cpu = x;
	}
	public void setBG(Color x)
	{
		bgColor = x;
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
	public void paintHistory(Graphics g)
	{
		if(history.size()>0)
		{
			if(bgColor.equals(Color.DARK_GRAY)|| bgColor.equals(new Color(22, 71, 53)))
			{
				g.setColor(Color.LIGHT_GRAY);
			}
			else
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
			g.drawString(">",x(1388), y(937));
			
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
				int numPlayers = game.maxTurn+1;
				
				if(game.playerList.size()>2)
					maxPage2 = Math.max(1,(int)(Math.ceil((double)game.playerList.get((game.getTurn()+1)%numPlayers).getHandSize()/5)));
				else
					maxPage2 = 1;
				
				if(game.playerList.size()==2 || game.playerList.size()==4)
					if(game.playerList.size()==2)	
						maxPage3 = Math.max(1,(int)(Math.ceil((double)game.playerList.get((game.getTurn()+1)%numPlayers).getHandSize()/5)));
					else
						maxPage3 = Math.max(1,(int)(Math.ceil((double)game.playerList.get((game.getTurn()+2)%numPlayers).getHandSize()/5)));
				else
					maxPage3 = 1;
				
				if(game.playerList.size()>2)
					if(game.playerList.size()==3)
						maxPage4 = Math.max(1,(int)(Math.ceil((double)game.playerList.get((game.getTurn()+2)%numPlayers).getHandSize()/5)));
					else
						maxPage4 = Math.max(1,(int)(Math.ceil((double)game.playerList.get((game.getTurn()+3)%numPlayers).getHandSize()/5)));
				else
					maxPage4= 1;
		
			}
			
			
			if (page3!=1)
			{
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.BOLD, font(55)));
				g.drawString(">",x(1190), y(210));
				
			}
			if (page3!=maxPage3)
			{
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.BOLD, font(55)));
				g.drawString("<",x(550), y(210));
				
			}
			
			if (page2!=1)
			{
			
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(new Font("Roboto", Font.PLAIN, font(70)));
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
				g.setFont(new Font("Roboto", Font.PLAIN, font(70)));
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
		int hoveringIndex = hoveringCard +7*(page-1);
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
			if(hoveringCard>-1 && i==hoveringIndex)
			{
				try {
					g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y-60), xs(cardWidth), ys(cardHeight), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y), xs(cardWidth), ys(cardHeight), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		paintArrows(g, game.playerList.get(0).getHandSize());
		g.setColor(Color.white);
		g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
		if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
			g.setColor(Color.DARK_GRAY.darker());
		if(hoveringCard==2)
			g.drawString(game.playerList.get(0).getPaintName(), x(775), y(775-60));
		else
			g.drawString(game.playerList.get(0).getPaintName(), x(775), y(775));
		if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
			g.setColor(Color.LIGHT_GRAY);
		else
			g.setColor(Color.DARK_GRAY);
		if(hoveringCard==3||hoveringCard==4)
			g.drawString("Cards: "+Integer.toString((game.playerList.get(0).getHandSize())), x(925), y(775-60));
		else
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
							g.drawImage(rotate180(ImageIO.read(getClass().getResource(a))),x(1150-cardWidth-100*(h%5)),y(100), xs(cardWidth), ys(cardHeight), null);
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
							g.drawImage(rotate180(ImageIO.read(getClass().getResource("/card_back_alt.png"))),x(1150-cardWidth-50*h),y(100), xs(cardWidth), ys(cardHeight), null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				g.setColor(Color.white);
				g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
				if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
					g.setColor(Color.DARK_GRAY.darker());
				g.drawString(game.playerList.get(2).getPaintName(), x(800), y(310));
				if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
					g.setColor(Color.LIGHT_GRAY);
				else
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
				if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
					g.setColor(Color.DARK_GRAY.darker());
				g.drawString(game.playerList.get(1).getPaintName(), x(400), y(500));
				if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
					g.setColor(Color.LIGHT_GRAY);
				else
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
					
						g.drawImage(im,x(1480),y(780-cardWidth-50*h), xs(cardHeight), ys(cardWidth), null);
					}
				g.setColor(Color.white);
				g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
				if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
					g.setColor(Color.DARK_GRAY.darker());
				g.drawString(game.playerList.get(3).getPaintName(),x(1370) ,y(500));
				if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
					g.setColor(Color.LIGHT_GRAY);
				else
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
		int hoveringIndex = hoveringCard +7*(page-1);
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
			if(hoveringCard>-1 && i==hoveringIndex)
			{
				try {
					g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y-60), xs(cardWidth), ys(cardHeight), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					g.drawImage(ImageIO.read(getClass().getResource(a)),x(x+120*(i%7)),y(y), xs(cardWidth), ys(cardHeight), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		paintArrows(g, game.current_player.getHandSize());
		g.setColor(Color.white);
		g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
		if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
			g.setColor(Color.DARK_GRAY.darker());
		if(hoveringCard == 2)
			g.drawString(game.current_player.getPaintName(), x(775), y(775-60));
		else
			g.drawString(game.current_player.getPaintName(), x(775), y(775));
		if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
			g.setColor(Color.LIGHT_GRAY);
		else
			g.setColor(Color.DARK_GRAY);
		if(hoveringCard == 3 || hoveringCard==4)
			g.drawString("Cards: "+Integer.toString((game.current_player.getHandSize())), x(925), y(775-60));
		else
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
				if((game.playerList.size()==2 && i==(game.getTurn()+1)%2)||(game.playerList.size()==4 && i== (game.getTurn()+2)%4))
				{
					int playerIndex = game.playerList.size()==4?(game.getTurn()+2)%4:(game.getTurn()+1)%2;
					
					if(gameEndedBack)
					{
						int s = (page3-1)*5;
						int en = Math.min(s+5, game.playerList.get(playerIndex).getHandSize());
						for(int h = s;h<en;h++)
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
								g.drawImage(rotate180(ImageIO.read(getClass().getResource(a))),x(1200-cardHeight-100*(h%5)),y(100), xs(cardWidth), ys(cardHeight), null);
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
								g.drawImage(rotate180(ImageIO.read(getClass().getResource("/card_back_alt.png"))),x(1150-cardWidth-50*h),y(100), xs(cardWidth), ys(cardHeight), null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
					if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
						g.setColor(Color.DARK_GRAY.darker());
					g.drawString(game.playerList.get(playerIndex).getPaintName(), x(800), y(310));
					if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
						g.setColor(Color.LIGHT_GRAY);
					else
						g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get(playerIndex).getHandSize()), x(900), y(310));
					if(gamestate.isUno(game.playerList.get(playerIndex)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
				    	g.drawString("UNO",x(850),y(350));
					}
				}
				else if ((game.playerList.size()==3 && i==(game.getTurn()+1)%3)||(game.playerList.size()==4 && i == (game.getTurn()+1)%4))
				{
					BufferedImage im = null;
					int playerIndex = game.playerList.size()==4?(game.getTurn()+1)%4:(game.getTurn()+1)%3;
					if(gameEndedBack)
					{
						int s = (page2-1)*5;
						int en = Math.min(s+5, game.playerList.get(playerIndex).getHandSize());
						for(int h = s;h<en;h++)
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
					if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
						g.setColor(Color.DARK_GRAY.darker());
					g.drawString(game.playerList.get(playerIndex).getPaintName(), x(400), y(500));
					if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
						g.setColor(Color.LIGHT_GRAY);
					else
						g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get(playerIndex).getHandSize()), x(400), y(600));
					if(gamestate.isUno(game.playerList.get(playerIndex)))
					{
						g.setColor(Color.red);
						g.setFont(new Font("Trebuchet", Font.BOLD, font(15)));
				    	g.drawString("UNO",x(450),y(550));
					}
				}
				else if((game.playerList.size()==3 && i==(game.getTurn()+2)%3)||(game.playerList.size()==4 && i==(game.getTurn()+3)%4))
				{
					BufferedImage im = null;
					int playerIndex = game.playerList.size()==4?(game.getTurn()+3)%4:(game.getTurn()+2)%3;
					if(gameEndedBack)
					{
						int s = (page4-1)*5;
						int en = Math.min(s+5, game.playerList.get(playerIndex).getHandSize());
						for(int h = s;h<en;h++)
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
							
							g.drawImage(im,x(1480),y(780-cardWidth-100*(h%5)), xs(cardHeight), ys(cardWidth), null);
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
						
							g.drawImage(im,x(1480),y(780-cardWidth-50*h), xs(cardHeight), ys(cardWidth), null);
						}
					}
					g.setColor(Color.white);
					g.setFont(new Font("Trebuchet", Font.BOLD, font(13)));
					if(!(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53))|| bgColor.equals(Color.LIGHT_GRAY) || bgColor.equals(new Color(0,138,138).darker())))
						g.setColor(Color.DARK_GRAY.darker());
					g.drawString(game.playerList.get(playerIndex).getPaintName(),x(1370) ,y(500));
					if(bgColor.equals(Color.DARK_GRAY) || bgColor.equals(new Color(22, 71, 53)))
						g.setColor(Color.LIGHT_GRAY);
					else
						g.setColor(Color.DARK_GRAY);
					g.drawString("Cards: "+Integer.toString(game.playerList.get(playerIndex).getHandSize()), x(1370), y(600));
					if(gamestate.isUno(game.playerList.get(playerIndex)))
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
		else if (color.equals("Green"))
			g.setColor(new Color(0,229,145));
		else
			g.setColor(Color.black);
		
		g.fillRect(x(x), y(y),xs(cardWidth), ys(7));
	}
	
	public void paintEndText(Graphics g)
	{
		int x = 538+((Math.max(5-game.current_player.getPaintName().length(), 0))*40);
		int y = 350;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,xs(1920),ys(1080));
		
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,font(75)));
		g.setColor(Color.RED);
		g.drawString(game.current_player.getPaintName()+" is the winner!!!", x(x), y(y));
		
		g.setColor(Color.white);

		//g.drawRect(x(785), y(491), xs(310), ys(80));
		//g.drawRect(x(804), y(641), xs(275), ys(80));
		g.fillRect(x(887), y(398), xs(110), ys(50));
		g.setColor(Color.black);
		g.drawRect(x(887), y(398), xs(110), ys(50));
		g.drawRect(x(887+1), y(398+1), xs(110-2), ys(50-2));
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,font(50)));
		g.setColor(Color.green);
		g.drawString("NEW GAME", x(800), y(570));
		g.drawString("REMATCH", x(814), y(700));
		
		g.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,font(35)));
		g.setColor(Color.black);
		g.drawString("BACK", x(893), y(435));
		
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
		
		else if(e.getX()>= x(1700) && e.getX()<=x(1800) && e.getY()>=y(115) && e.getY()<= y(160) && game.started && menu) //go to home
		{
			game = new Board();
			gamestate = new GameState(game);
			start = true;
			menu= false;
			gameEndedBack = false;
			cpu = false;
			history.clear();
			game.laxWildCard = laxWildCard;
			game.infiniteDraw = infiniteDraw;
			repaint();
		}
		else if(e.getX()>= x(0) && e.getX()<=x(1650) && game.started && menu){
			menu = false;
			repaint();
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
	    	    //addHistory(game.getLastTurn().getName()+" drew a card.");
	    		addHistory(game.getLastPlayerTurn("draw"));
	    	  
	    	    colorPickerDraw = false;
	    	    game.lastDraw = 0;
//	    	    if(cpu)
//	    	    {
//	    	    	Timer timer = new Timer(1000, cpuTask);
//		    		timer.setRepeats(false);
//		    		timer.start();
//	    	    }
	    	    repaint();
			}
			else if(e.getX()>= x(682) && e.getX()<=x(732) && e.getY()>=y(665) && e.getY()<=y(715) && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("blue");
				game.nextTurn();
				
	    		page = 1;
	    	    //addHistory(game.getLastTurn().getName()+" drew a card.");
	    	    addHistory(game.getLastPlayerTurn("draw"));
	    	  
	    	    colorPickerDraw = false;
	    	    game.lastDraw = 0;

	    	    repaint();
			}
			else if(e.getX()>= x(742) && e.getX()<=x(792) && e.getY()>=y(665) && e.getY()<=y(715) && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("green");
				game.nextTurn();
				
	    		page = 1;
	    	    //addHistory(game.getLastTurn().getName()+" drew a card.");
	    		addHistory(game.getLastPlayerTurn("draw"));
	    	   
	    	    colorPickerDraw = false;
	    	    game.lastDraw = 0;

	    	    repaint();
			}
			else if(e.getX()>= x(802) && e.getX()<=x(852) && e.getY()>=y(665) && e.getY()<=y(715) && colorPickerDraw)
			{
				game.play(-1);
				game.changeColor("yellow");
				game.nextTurn();
				
	    		page = 1;
	    	    //addHistory(game.getLastTurn().getName()+" drew a card.");
	    		addHistory(game.getLastPlayerTurn("draw"));
	    	    
	    	    colorPickerDraw = false;
	    	    game.lastDraw = 0;

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
//	    	    if(cpu)

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
			if (dupName||notEnoughPlayers)
			{
				if(e.getX()>= x(915) && e.getX()<=x(985) && e.getY()>=y(660) && e.getY()<=y(700) && enterNames)
				{
					this.remove(nameText);
					nameText.setEnabled(true);
					if(dupName)
					{
						nameText.setText(null);
						dupName = false;
					}
					else
						notEnoughPlayers=false;
					repaint();
				}
			}
			else if(confirm) //confirmation popup
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
//					int size = names.size();
//					for(int i = 1;i<=4-size;i++)
//						names.add("Player "+i);
					game.start(names);
					game.laxWildCard = laxWildCard;
					game.infiniteDraw = infiniteDraw;
					addHistory("The initial card is a "+game.showTopCard()+".");
					repaint();
				}
			}
			else
			{
				//beginning screen
				if(e.getX()>= x(857) && e.getX()<=x(1007) && e.getY()>=y(450) && e.getY()<=y(512) && start)
				{
					start = false;
					enterNames = true;
					//game.start(names);
					repaint();
				}
				else if(e.getX()>= x(857) && e.getX()<=x(1007) && e.getY()>=y(530) && e.getY()<=y(592) && start)
				{
					start = false;
					cpu = true;
					game.start(cpu);
					game.laxWildCard = laxWildCard;
					game.infiniteDraw = infiniteDraw;
					addHistory("The initial card is a "+game.showTopCard()+".");

					repaint();
				}
				else if(e.getX()>= x(857) && e.getX()<=x(1007) && e.getY()>=y(610) && e.getY()<=y(672) && start)
				{
					start = false;
					LAN = true;
					repaint();
				}
				else if(e.getX()>= x(857) && e.getX()<=x(1007) && e.getY()>=y(690) && e.getY()<=y(752) && start)
				{
					start = false;
					settings = true;
					repaint();
				}
				else if(e.getX()>= x(872) && e.getX()<=x(992) && e.getY()>=y(779) && e.getY()<=y(851) && start)
				{
					frame.setVisible(false); 
					frame.dispose();
				}
				
				
				//entering names screen
				if(e.getX()>= x(1110) && e.getX()<=x(1160) && e.getY()>=y(260) && e.getY()<=y(313) && enterNames && names.size()<4) //add name (plus png)
				{
					this.remove(nameText);
					if(names.contains(nameText.getText()))
						dupName = true;
					else if(nameText.getText().replace(" ", "").length()>0)
					{
							names.add(nameText.getText());
							nameText.setText("");
					}
					repaint();
				}
				else if(e.getX()>= x(725) && e.getX()<=x(845) && e.getY()>=y(400) && e.getY()<=y(450) && enterNames)// back button
				{
					this.remove(nameText);
					enterNames = false;
					start = true;
					//names.clear();
					repaint();
				}
				else if(e.getX()>= x(1070) && e.getX()<=x(1190) && e.getY()>=y(400) && e.getY()<=y(450) && enterNames)// start button
				{
					if(names.size()>=2 && names.size()<=4)
					{
						if(names.size()==2)
						{
							confirm = true;
							repaint();
						}
						else
						{
							this.remove(nameText);
							enterNames = false;
							game.start(names);
							game.laxWildCard = laxWildCard;
							game.infiniteDraw = infiniteDraw;
							addHistory("The initial card is a "+game.showTopCard()+".");
	
							repaint();
						}
					}
					else
					{
						notEnoughPlayers = true;
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
		else if (LAN)
		{
			if(e.getX()>= x(850) && e.getX()<=x(1000) && e.getY()>=y(700) && e.getY()<=y(765))
			{
				LAN = false;
				start = true;
				repaint();
			}
		}
		else if(settings)
		{
			//choose background color x(1009+50*i), y(538), xs(35), ys(35)
			if (e.getX()>= x(1009) && e.getX()<=x(1044) && e.getY()>=y(540) && e.getY()<=y(575) && bgOptionBox)
			{
				bgOptionBox = false;
				bgOptions.add(0,bgColor);
				bgColor = bgOptions.remove(1);
				repaint();
			}
			else if (e.getX()>= x(1059) && e.getX()<=x(1094) && e.getY()>=y(540) && e.getY()<=y(575) && bgOptionBox)
			{
				bgOptionBox = false;
				bgOptions.add(0,bgColor);
				bgColor = bgOptions.remove(2);
				repaint();
			}
			else if (e.getX()>= x(1109) && e.getX()<=x(1144) && e.getY()>=y(540) && e.getY()<=y(575) && bgOptionBox)
			{
				bgOptionBox = false;
				bgOptions.add(0,bgColor);
				bgColor = bgOptions.remove(3);
				repaint();
			}
			else if (e.getX()>= x(1159) && e.getX()<=x(1194) && e.getY()>=y(540) && e.getY()<=y(575) && bgOptionBox)
			{
				bgOptionBox = false;
				bgOptions.add(0,bgColor);
				bgColor = bgOptions.remove(4);
				repaint();
			}
			else if (e.getX()>= x(1209) && e.getX()<=x(1244) && e.getY()>=y(540) && e.getY()<=y(575) && bgOptionBox)
			{
				bgOptionBox = false;
				bgOptions.add(0,bgColor);
				bgColor = bgOptions.remove(5);
				repaint();
			}
			else if (e.getX()>= x(1259) && e.getX()<=x(1294) && e.getY()>=y(540) && e.getY()<=y(575) && bgOptionBox)
			{
				bgOptionBox = false;
				bgOptions.add(0,bgColor);
				bgColor = bgOptions.remove(6);
				repaint();
			}
			else if(!(e.getX()>= x(1000) && e.getX()<=x(1300) && e.getY()>=y(525) && e.getY()<=y(590)) && bgOptionBox)
			{
				bgOptionBox = false;
				repaint();
			}
			else if(e.getX()>= x(1100) && e.getX()<=x(1175) && e.getY()>=y(265) && e.getY()<=y(300))
			{
				infiniteDraw = !infiniteDraw;
				repaint();
			}
			else if(e.getX()>= x(1100) && e.getX()<=x(1175) && e.getY()>=y(365) && e.getY()<=y(400))
			{
				laxWildCard = !laxWildCard;
				repaint();
			}
			else if(e.getX()>= x(850) && e.getX()<=x(1000) && e.getY()>=y(650) && e.getY()<=y(710))
			{
				settings = false;
				start = true;
				repaint();
			}
			else if(e.getX()>= x(1100) && e.getX()<=x(1215) && e.getY()>=y(472) && e.getY()<=y(505))
			{
				bgOptionBox = !bgOptionBox;
				repaint();
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
				else if(e.getX()>= x(275) && e.getX()<= x(320) && e.getY()>=y(190) && e.getY()<=y(230) && page2>1)
				{
					
					page2-=1;
					repaint();
				}
				else if(e.getX()>= x(275) && e.getX()<=x(320) && e.getY()>=y(788) && e.getY()<=y(830) && page2<maxPage2)
				{
					
					page2+=1;
					repaint();
				}
				else if(e.getX()>= x(1185) && e.getX()<= x(1225) && e.getY()>=y(165) && e.getY()<=y(215) && page3>1)
				{
					
					page3-=1;
					repaint();
				}
				else if(e.getX()>= x(542) && e.getX()<=x(587) && e.getY()>=y(165) && e.getY()<=y(215) && page3<maxPage3)
				{
					
					page3+=1;
					repaint();
				}
				else if(e.getX()>=x(1550)  && e.getX()<= x(1595) && e.getY()>= y(788) && e.getY()<=y(830) && page4>1)
				{
					
					page4-=1;
					repaint();
				}
				else if(e.getX()>= x(1550) && e.getX()<=x(1595) && e.getY()>=y(190) && e.getY()<=y(230) && page4<maxPage4)
				{
					
					page4+=1;
					repaint();
				}
				else if(e.getX()>= x(385) && e.getX()<= x(440) && e.getY()>=y(850) && e.getY()<=y(937) && page>1)
				{
					page-=1;
					repaint();
				}
				else if(e.getX()>= x(1388) && e.getX()<=x(1443) && e.getY()>=y(850) && e.getY()<=y(937) && page<maxPage)
				{
					page+=1;
					repaint();
				}
			}
			else
			{
				if(e.getX()>= x(785) && e.getX()<=x(1095) && e.getY()>=y(510) && e.getY()<=y(590))//new game
				{
					game = new Board();
					gamestate = new GameState(game);
					frame.setVisible(false);
					frame.dispose();
					UnoGraphics g = new UnoGraphics(game, gamestate);
					g.setBG(bgColor);
					game.laxWildCard = laxWildCard;
					game.infiniteDraw = infiniteDraw;
				}
				else if(e.getX()>= x(804) && e.getX()<=x(1080) && e.getY()>=y(641) && e.getY()<=y(721)) //rematch
				{
					game = new Board();
					gamestate = new GameState(game);
					history.clear();
					gameEndedBack = false;
					menu = false;
//					frame.setVisible(false);
//					frame.dispose();
//					UnoGraphics g = new UnoGraphics(game, gamestate);
					if(cpu)
					{
						game.start(cpu);
						game.laxWildCard = laxWildCard;
						game.infiniteDraw = infiniteDraw;
						addHistory("The initial card is a "+game.showTopCard()+".");

//						g.setCPU(true);
//						g.setBG(bgColor);
					}
					else
					{
						game.start(names);
						addHistory("The initial card is a "+game.showTopCard()+".");

						game.laxWildCard = laxWildCard;
						game.infiniteDraw = infiniteDraw;
//						g.setBG(bgColor);
					}
					//g.skipStart();
					repaint();
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
					if(e.getX()>= x(drawClick) && e.getX()<=x(drawClick+cardWidth) && e.getY()>=y(400) && e.getY()<=y(580) && game.deck.deck.size()>0)
					{
						
						game.drawCard();
						if(game.current_player.getTemp().size()>0)
						{
							colorPickerDraw = true;
							
							repaint();
						}
						else
						{
							//System.out.println("drew a "+game.current_player.getHand().get(game.current_player.getHandSize()-1));
				    		
				    		System.out.println(game.current_player.getName());
				    		System.out.println(game.current_player.getHand());
				    		System.out.println("top card is now "+game.deck.discard.get(game.deck.discard.size()-1));
				    		System.out.println(game.deck.deck.size()+" cards left");
				    		game.nextTurn();
				    		page = 1;
				    	    addHistory(game.getLastPlayerTurn("draw"));
				    	    game.lastDraw = 0;
				    	    repaint();
						}
						//}
					}
					
					if(e.getX()>= x(490) && e.getX()<x(610) && e.getY()>=y(800+hoveringCard0) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>7*(page-1))
						{
							if(game.current_player.getHand().get(7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								if(game.laxWildCard || game.current_player.getHand().get(7*(page-1)).getValue().equals(UnoCard.Value.Wild)
										||(game.findFirstNonWild()>7*(page-1)&&!game.showTopCard().getColor().equals(UnoCard.Color.Wild)))
								{
									colorPickerPlay = true;
									wildIndex = 7*(page-1);
									colorPickerPlayCoord = 540+120*(wildIndex%7);
									repaint();
								}
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
					else if(e.getX()>= x(610) && e.getX()<x(730) && e.getY()>=y(800+hoveringCard1) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>1+7*(page-1))
						{
							if(game.current_player.getHand().get(1+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								if(game.laxWildCard || game.current_player.getHand().get(1+7*(page-1)).getValue().equals(UnoCard.Value.Wild)
										||(game.findFirstNonWild()>1+7*(page-1)&&!game.showTopCard().getColor().equals(UnoCard.Color.Wild)))
								{
									colorPickerPlay = true;
									wildIndex = 1+7*(page-1);
									colorPickerPlayCoord = 540+120*(wildIndex%7);
									repaint();
								}
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
					else if(e.getX()>= x(740) && e.getX()<x(850) && e.getY()>=y(800+hoveringCard2) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>2+7*(page-1))
						{
							if(game.current_player.getHand().get(2+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								if(game.laxWildCard || game.current_player.getHand().get(2+7*(page-1)).getValue().equals(UnoCard.Value.Wild)
										||(game.findFirstNonWild()>2+7*(page-1)&&!game.showTopCard().getColor().equals(UnoCard.Color.Wild)))
								{
									colorPickerPlay = true;
									wildIndex = 2+7*(page-1);
									colorPickerPlayCoord = 540+120*(wildIndex%7);
									repaint();
									
								}
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
					else if(e.getX()>= x(850) && e.getX()<x(970) && e.getY()>=y(800+hoveringCard3) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>3+7*(page-1))
						{
							if(game.current_player.getHand().get(3+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								if(game.laxWildCard || game.current_player.getHand().get(3+7*(page-1)).getValue().equals(UnoCard.Value.Wild)
										||(game.findFirstNonWild()>3+7*(page-1)&&!game.showTopCard().getColor().equals(UnoCard.Color.Wild)))
								{
									colorPickerPlay = true;
									wildIndex = 3+7*(page-1);
									colorPickerPlayCoord = 540+120*(wildIndex%7);
									repaint();
								}
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
					else if(e.getX()>= x(970) && e.getX()<x(1090) && e.getY()>=y(800+hoveringCard4) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>4+7*(page-1))
						{
							if(game.current_player.getHand().get(4+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								if(game.laxWildCard || game.current_player.getHand().get(4+7*(page-1)).getValue().equals(UnoCard.Value.Wild)
										||(game.findFirstNonWild()>4+7*(page-1)&&!game.showTopCard().getColor().equals(UnoCard.Color.Wild)))
								{
									colorPickerPlay = true;
									wildIndex = 4+7*(page-1);
									colorPickerPlayCoord = 540+120*(wildIndex%7);
									repaint();
								}
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
					else if(e.getX()>= x(1090) && e.getX()<x(1210) && e.getY()>=y(800+hoveringCard5) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>5+7*(page-1))
						{
							if(game.current_player.getHand().get(5+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								if(game.laxWildCard || game.current_player.getHand().get(5+7*(page-1)).getValue().equals(UnoCard.Value.Wild)
										||(game.findFirstNonWild()>5+7*(page-1)&&!game.showTopCard().getColor().equals(UnoCard.Color.Wild)))
								{
									colorPickerPlay = true;
									wildIndex = 5+7*(page-1);
									colorPickerPlayCoord = 540+120*(wildIndex%7);
									repaint();
								}
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
					else if(e.getX()>= x(1210) && e.getX()<=x(1350) && e.getY()>=y(800+hoveringCard6) && e.getY()<=y(980))
					{
						if(game.current_player.getHand().size()>6+7*(page-1))
						{
							if(game.current_player.getHand().get(6+7*(page-1)).getColor().equals(UnoCard.Color.Wild))
							{
								if(game.laxWildCard || game.current_player.getHand().get(6+7*(page-1)).getValue().equals(UnoCard.Value.Wild)
										||(game.findFirstNonWild()>6+7*(page-1)&&!game.showTopCard().getColor().equals(UnoCard.Color.Wild)))
								{
									colorPickerPlay = true;
									wildIndex = 6+7*(page-1);
									colorPickerPlayCoord = 540+120*(wildIndex%7);
									repaint();
								}
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
				else if(e.getX()>= x(1388) && e.getX()<=x(1443) && e.getY()>=y(850) && e.getY()<=y(937) && page<maxPage)
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
		
		//start screen hovering buttons
		if(start && e.getX()>= x(500) && e.getX()<=x(1200) && e.getY()>=y(200) && e.getY()<=y(1080))
		{
			if(e.getX()>= x(857) && e.getX()<=x(1007) && e.getY()>=y(450) && e.getY()<=y(512))
			{
				hoverStart = "local";
				repaint();
			}
			else if(e.getX()>= x(857) && e.getX()<=x(1007) && e.getY()>=y(530) && e.getY()<=y(592))
			{
				hoverStart ="cpu";
				repaint();
			}
			else if(e.getX()>= x(857) && e.getX()<=x(1007) && e.getY()>=y(610) && e.getY()<=y(672))
			{
				hoverStart = "lan";
				repaint();
			}
			else if(e.getX()>= x(857) && e.getX()<=x(1007) && e.getY()>=y(690) && e.getY()<=y(752))
			{
				hoverStart = "options";
				repaint();
			}
			else if(e.getX()>= x(872) && e.getX()<=x(992) && e.getY()>=y(779) && e.getY()<=y(851))
			{
				hoverStart = "exit";
				repaint();
			}
			else
			{
				hoverStart = "none";
				repaint();
			}
		}
		else
		{
			if(!hoverStart.equals("none"))
			{
				hoverStart = "none";
				repaint();
			}
				
		}
		//names hovering
		if(enterNames && !confirm && !dupName && e.getX()>= x(725) && e.getX()<=x(1200) && e.getY()>=y(475) && e.getY()<=y(700))
		{
			if(names.size()>0 && e.getX()>= x(800) && e.getX()<=x(800+(Math.min(13*names.get(0).length(), 12*10)))&& e.getY()>=y(530) && e.getY()<=y(550))//first name
			{
				
				//this.remove(nameText);
				hovering1 = true;
				repaintHoverNames = true;
				paintImmediately(x(725), y(475), x(1200)-x(725), y(700)-y(475));
			}
			else if(names.size()>1 && e.getX()>= x(1010) && e.getX()<=x(1010+(Math.min(12*names.get(1).length(), 12*10))) && e.getY()>=y(530) && e.getY()<=y(550))//second name
			{
				
				//this.remove(nameText);
				hovering2 = true;
				repaintHoverNames = true;
				paintImmediately(x(725), y(475), x(1200)-x(725), y(700)-y(475));
			}
			else if(names.size()>2 && e.getX()>= x(800) && e.getX()<=x(800+(Math.min(12*names.get(2).length(), 12*10))) && e.getY()>=y(630) && e.getY()<=y(650))//third name
			{
				
				//this.remove(nameText);
				hovering3 = true;
				repaintHoverNames = true;
				paintImmediately(x(725), y(475), x(1200)-x(725), y(700)-y(475));
			}
			else if(names.size()>3 && e.getX()>= x(1010) && e.getX()<=x(1010+(Math.min(12*names.get(3).length(), 12*10))) && e.getY()>=y(630) && e.getY()<=y(650))//fourth name
			{
				
				//this.remove(nameText);
				hovering4 = true;
				repaintHoverNames = true;
				paintImmediately(x(725), y(475), x(1200)-x(725), y(700)-y(475));
			}
			else
			{
				//this.remove(nameText);
				if(hovering1||hovering2||hovering3||hovering4)
				{
					hovering1 = false;
					hovering2 = false;
					hovering3 = false;
					hovering4 = false;
					repaintHoverNames = true;
					paintImmediately(x(725), y(475), x(1200)-x(725), y(700)-y(475));
				}
			}
		}
		
		//cards hovering
		if(!game.cpuActive && !menu && !game.isOver() && !colorPickerPlay && !colorPickerDraw && e.getX()>= x(490) && e.getX()<=x(1350) && e.getY()>=y(650) && e.getY()<=y(980))
		{
			Player p = cpu?game.playerList.get(0):game.current_player;
			if(e.getX()>= x(490) && e.getX()<x(610) && e.getY()>=y(800+hoveringCard0) && e.getY()<=y(980) && p.getHand().size()>0+7*(page-1))
			{
				hoveringCard = 0;
				repaintHover = true;
				paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
			}
			else if(e.getX()>= x(610) && e.getX()<x(730) && e.getY()>=y(800+hoveringCard1) && e.getY()<=y(980)&&p.getHand().size()>1+7*(page-1))
			{
				hoveringCard = 1;
				repaintHover = true;
				paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
			}
			else if(e.getX()>= x(730) && e.getX()<x(850) && e.getY()>=y(800+hoveringCard2) && e.getY()<=y(980)&&p.getHand().size()>2+7*(page-1))
			{
				hoveringCard = 2;
				repaintHover = true;
				paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
			}
			else if(e.getX()>= x(850) && e.getX()<x(970) && e.getY()>=y(800+hoveringCard3) && e.getY()<=y(980)&&p.getHand().size()>3+7*(page-1))
			{
				hoveringCard = 3;
				repaintHover = true;
				paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
			}
			else if(e.getX()>= x(970) && e.getX()<x(1090) && e.getY()>=y(800+hoveringCard4) && e.getY()<=y(980)&&p.getHand().size()>4+7*(page-1))
			{
				hoveringCard = 4;
				repaintHover = true;
				paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
			}
			else if(e.getX()>= x(1090) && e.getX()<x(1210) && e.getY()>=y(800+hoveringCard5) && e.getY()<=y(980)&&p.getHand().size()>5+7*(page-1))
			{
				hoveringCard = 5;
				repaintHover = true;
				paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
			}
			else if(e.getX()>= x(1210) && e.getX()<=x(1350) && e.getY()>=y(800+hoveringCard6) && e.getY()<=y(980)&&p.getHand().size()>6+7*(page-1))
			{
				hoveringCard = 6;
				repaintHover = true;
				paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
			}
			else
			{
				if(hoveringCard>-1)
				{
					hoveringCard = -1;
					if(game.isOver())
						repaint();
					else
					{
						repaintHover = true;
						paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
					}
					
				}
			}

		}
		else 
		{
			if(hoveringCard>-1)
			{
				hoveringCard = -1;
				if(!game.isOver())
				{
					repaintHover = true;
					paintImmediately(x(490), y(650), x(1350)-x(490), y(982)-y(650));
				}
				else
					repaint();
			}
		}
		
	}
	//@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GraphicsRunner extends JPanel implements MouseListener
{
	private int cardHeight = 180;
	private int cardWidth = 130;
	private Board game;
	private GameState gamestate;
	private JPanel newPanel;
	private JFrame frame;
	private Queue<String> t = new LinkedList<String>();
	int page = 1;
	public GraphicsRunner()
	{
		frame = new JFrame("Uno gAme");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1980, 1080);
        frame.setResizable(true);
        
        
        frame.add(this);
        frame.setVisible(true);
      
	}
	public GraphicsRunner(Board game, GameState gamestate)throws Exception
	{
		this.game = game;
		this.gamestate = gamestate;
		frame = new JFrame("Uno gAme.exe");
		newPanel = new JPanel();
	    newPanel.setLayout(null);
	    newPanel.setBackground(new Color(0,138,138));
	    newPanel.addMouseListener(this);
	    frame.add(newPanel);
	    
	   // buildBackground();
	    buildDiscard(950, 400);
	    buildDeck(670, 400);
	    buildHand(475, 800);
	    buildDirection(game.getCurrentDirection());
	    buildColorTracker(game.showTopCard().getColor().toString());
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1980, 1080);
        
        frame.setResizable(true);
        
        frame.setVisible(true);
        
	}
	public String getMouseLocation()
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		int x = p.x;
		int y = p.y;
		return ""+x+y;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
	}
	public void buildBackground()
	{
		ImageIcon h = null;
		try {
			h = new ImageIcon(ImageIO.read(getClass().getResource("background.jpg")));
		} catch (IOException e) {
			
		}
		JLabel l = new JLabel();
		l.setIcon(h);
		l.setBounds(500, -500, 1920, 1080);
		newPanel.add(l);
	}
	public void buildDiscard(int x, int y)
	{
		String a = "";
		for(int i =0;i<game.deck.discard.size();i++)
		{
			UnoCard card = game.deck.discard.get(i);
			if(card.getValue().toString().equals("Reverse"))
			{
				a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
			}
			else if(card.getValue().toString().equals("DrawTwo"))
			{
				a = card.getColor().toString().toLowerCase()+"_picker"+".png";
			}
			else if(card.getValue().toString().equals("Wild"))
			{
				a = "wild_color_changer.png";
			}
			else if(card.getValue().toString().equals("Skip"))
			{
				a = card.getColor().toString().toLowerCase()+"_skip.png";
			}
			else if(card.getValue().toString().equals("Wild_Four"))
			{
				a = "wild_pick_four.png";
			}
			else 
				a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
			
				toDiscard(a, x, y);
		}
	}
	public void buildDeck(int x, int y)
	{
		for(int i = 0;i<game.deck.deck.size();i++)
		{
			
			String a = "card_back.png";
			toDeck(a,x,y);
			
		}
	}
	public void buildHand(int x, int y)
	{
		game.current_player.sortHand(game.showTopCard().getColor(), game.showTopCard().getValue());	
		int start = (page-1)*7;
		int end = Math.min(start+7, game.current_player.getHandSize());
		for(int i = start;i<end;i++)
		{
			
			String a = "";
			UnoCard card = game.current_player.getHand().get(i);
			
			if(card.getValue().toString().equals("Reverse"))
			{
				a = card.getColor().toString().toLowerCase()+"_reverse"+".png";
			}
			else if(card.getValue().toString().equals("DrawTwo"))
			{
				a = card.getColor().toString().toLowerCase()+"_picker"+".png";
			}
			else if(card.getValue().toString().equals("Wild"))
			{
				a = "wild_color_changer.png";
			}
			else if(card.getValue().toString().equals("Skip"))
			{
				a = card.getColor().toString().toLowerCase()+"_skip"+".png";
			}
			else if(card.getValue().toString().equals("Wild_Four"))
			{
				a = "wild_pick_four.png";
			}
			else
				a = card.getColor().toString().toLowerCase()+"_"+card.toInt()+".png";
			
			toBufferedImage(a,i,x+120*(i%7),y);
			
		}
		buildArrows(game.current_player.getHandSize());
		toLabel(game.current_player.getName(), x+250, y-150);
		buildHandSize(Integer.toString((game.current_player.getHandSize())), x+350, y-200);
		if(gamestate.isUno(game.current_player))
	    	buildUno(x+500,y-150);
		for(int i =0;i<game.playerList.size();i++)
		{
			if(i !=game.getTurn())
			{
				Player p = game.playerList.get(i);
				if(i == (game.getTurn()+2)%4)
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
					{
						String a = "card_back_alt.png";
						toHands(a,669+50*h,100);
						
					}
					toLabel(game.playerList.get((game.getTurn()+2)%4).getName(), 800, 200);
					buildHandSize(Integer.toString(game.playerList.get((game.getTurn()+2)%4).getHandSize()), 900, 150);
					if(gamestate.isUno(game.playerList.get((game.getTurn()+2)%4)))
				    	buildUno(950,200);
				}
				else if ( i == (game.getTurn()+1)%4)
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
					{
						
						String a = "card_back_alt.png";
						BufferedImage image = buf(a);
						image = rotateClockwise90(image);
						
						toHands(a,200,290+50*h);
						
					}
					toLabel(game.playerList.get((game.getTurn()+1)%4).getName(),50 , 400);
					buildHandSize(Integer.toString(game.playerList.get((game.getTurn()+1)%4).getHandSize()), 50, 400);
					if(gamestate.isUno(game.playerList.get((game.getTurn()+1)%4)))
				    	buildUno(50,280);
				}
				else if(i==(game.getTurn()+3)%4)
				{
					for(int h = 0;h<Math.min(p.getHandSize(), 10);h++)
					{
						
						String a = "card_back_alt.png";
						BufferedImage image = buf(a);
						image = rotateCounter90(image);
						
						toHands(a,1480,290+50*h);
					}
					toLabel(game.playerList.get((game.getTurn()+3)%4).getName(),1700 ,400 );
					buildHandSize(Integer.toString(game.playerList.get((game.getTurn()+3)%4).getHandSize()), 1700, 400);
					if(gamestate.isUno(game.playerList.get((game.getTurn()+3)%4)))
				    	buildUno(1700,200);
				}
			}
		}	
	}
	public void buildArrows(int handSize)
	{
		
		int maxPage = (int)(Math.ceil((double)game.current_player.getHandSize()/7));
		if (page!=1)
		{
			JLabel leftArrow = new JLabel("<");
			leftArrow.setForeground(Color.LIGHT_GRAY);
			leftArrow.setFont(new Font("Roboto", Font.BOLD, 75));
			leftArrow.setBounds(385, 845, 100, 100);
			newPanel.add(leftArrow);
			leftArrow.addMouseListener(new MouseListener() {
		    	public void mouseClicked(MouseEvent e)
		    	{
		    		
		    	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
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
					page-=1;
					newPanel.removeAll();
					newPanel.revalidate();
		    		newPanel.repaint();
		    		buildHand(475, 800);
		    		buildDiscard(950, 400);
		    	    buildDeck(670, 400);
		    	    buildHand(475, 800);
		    	    buildDirection(game.getCurrentDirection());
		    	    rebuildHistory();
		    	    buildColorTracker(game.showTopCard().getColor().toString());
				}
			});
		}
		if (page!=maxPage)
		{
			JLabel rightArrow = new JLabel(">");
			rightArrow.setForeground(Color.LIGHT_GRAY);
			rightArrow.setFont(new Font("Roboto", Font.BOLD, 75));
			rightArrow.setBounds(1375, 845, 100, 100);
			newPanel.add(rightArrow);
			rightArrow.addMouseListener(new MouseListener() {
		    	public void mouseClicked(MouseEvent e)
		    	{
		    		
		    	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
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
					page+=1;
					newPanel.removeAll();
					newPanel.revalidate();
		    		newPanel.repaint();
		    		buildHand(475, 800);
		    		buildDiscard(950, 400);
		    	    buildDeck(670, 400);
		    	    buildHand(475, 800);
		    	    buildDirection(game.getCurrentDirection());
		    	    rebuildHistory();
		    	    buildColorTracker(game.showTopCard().getColor().toString());
				}
			});
		}
	}
	public void buildHandSize(String z, int x, int y)
	{
		JLabel l = new JLabel("Cards: "+z);
		l.setForeground(Color.DARK_GRAY);
		newPanel.add(l);
		l.setBounds(x,y,300,300);
	}
	public void buildUno(int x, int y)
	{
		JLabel l = new JLabel("UNO");
		l.setForeground(Color.RED);
		newPanel.add(l);
		l.setBounds(x,y,200,200);
	}
	public void toLabel(String name,int x, int y)
	{
		JLabel l = new JLabel(name);
		l.setForeground(Color.WHITE);
		newPanel.add(l);
		l.setBounds(x,y,500,200);
	}
	public void buildDeck(Graphics g, int x, int y)
	{
		for(int i = 0;i<game.deck.deck.size();i++)
		{
			String a = "card_back_alt.png";
			toBufferedImage(a,g,x+1*i,y);
			
		}
	}

	public void toBufferedImage(String z, Graphics g, int x, int y)
	{
		try {
			BufferedImage img = ImageIO.read(getClass().getResource(z));
			g.drawImage(img, x, y, cardWidth, cardHeight, null);
			
		} catch(Exception e)
		{}
	}
	public void toHands(String z, int x, int y)
	{
		JLabel btn = new JLabel();
		try {
			newPanel.add(btn);
//			newPanel.removeAll();
//    		revalidate();
//    		repaint();
			BufferedImage img = ImageIO.read(getClass().getResource(z));
			btn.setIcon(new ImageIcon(img));
			newPanel.add(btn);
		    btn.setBounds(x,y,cardWidth,cardHeight);
		    ImageIcon i = new ImageIcon(img);
		    btn.setIcon(i);
			
			
			
		} catch(Exception e)
		{}
	}
	public void toDeck(String z, int x, int y)
	{
		JLabel btn = new JLabel();
		try {
			BufferedImage img = ImageIO.read(getClass().getResource(z));
			btn.setIcon(new ImageIcon(img));
			newPanel.add(btn);
		    btn.setBounds(x,y,cardWidth,cardHeight);
		    ImageIcon i = new ImageIcon(img);
		    btn.setIcon(i);
		    btn.addMouseListener(new MouseListener() {
		    	public void mouseClicked(MouseEvent e)
		    	{
		    		
		    	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
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
				public void mouseReleased(MouseEvent arg0) {

		    		game.drawCard();
		    		game.checkPlayable();
		    		game.nextTurn();
		    		if(game.showTopCard().getColor().equals(UnoCard.Color.Wild))
		    		{
		    			int x = buildColorPicker();
		    			if(x==0)
		    				game.changeColor("red");
		    			else if(x==1)
		    				game.changeColor("blue");
		    			else if(x==2)
		    				game.changeColor("green");
		    			else
		    				game.changeColor("yellow");
		    		}
		    		System.out.println("drew a "+game.current_player.getHand().get(game.current_player.getHandSize()-1));
		    		System.out.println(game.current_player.getName());
		    		System.out.println(game.current_player.getHand());
		    		System.out.println("top card is now "+game.deck.discard.get(game.deck.discard.size()-1));
		    		//game.nextTurn();
		    		page = 1;
		    		newPanel.removeAll();
		    		newPanel.revalidate();
		    		newPanel.repaint();
		    		buildDiscard(950, 400);
		    	    buildDeck(670, 400);
		    	    buildHand(475, 800);
		    	    buildDirection(game.getCurrentDirection());
		    	    buildHistory(game.getLastTurn().getName()+" drew a card.");
		    	    buildColorTracker(game.showTopCard().getColor().toString());
		    		
		    		
					
				}
		    });
		  
			newPanel.add(btn);
			//g.drawImage(img, x, y, cardWidth, cardHeight, null);
			
		} catch(Exception e)
		{}
	}
	public void toDiscard(String z, int x, int y)
	{
		JLabel btn = new JLabel();
		try {
			BufferedImage img = ImageIO.read(getClass().getResource(z));
			newPanel.add(btn);
			newPanel.removeAll();
    		revalidate();
    		repaint();
			btn.setIcon(new ImageIcon(img));
			newPanel.add(btn);
		    btn.setBounds(x,y,cardWidth,cardHeight);
		    ImageIcon i = new ImageIcon(img);
		    btn.setIcon(i);
			
			//g.drawImage(img, x, y, cardWidth, cardHeight, null);
			
		} catch(Exception e)
		{}
	}
	public void buildDirection(String b)
	{
		int x = 695;
		int y = 500;
		JLabel l = new JLabel(b);
		l.setFont(new Font("Trebuchet", Font.BOLD, 17));
		l.setForeground(Color.WHITE);
		newPanel.add(l);
		l.setBounds(x, y, 250,250);
	}
	public void buildHistory(String z)
	{
		if(t.size()>=6)
			t.remove();
		t.add(z);
		String[]x = new String[6];
		System.arraycopy(t.toArray(), 0, x, 0, t.size());
		JList l = new JList<>(x);
		newPanel.add(l);
		l.setOpaque(false);
		l.setBackground(new Color(0,138,138));
		l.setBounds(395,465,215,110);
	}
	public void rebuildHistory()
	{
		String[]x = new String[6];
		System.arraycopy(t.toArray(), 0, x, 0, t.size());
		JList l = new JList<>(x);
		newPanel.add(l);
		l.setOpaque(false);
		l.setBackground(new Color(0,138,138));
		l.setBounds(395,465,215,110);
	}
	
	public void buildColorTracker(String z)
	{
		int x = 955;
		int y = 485;
		JLabel l = new JLabel(z);
		l.setFont(new Font("Roboto", Font.BOLD, 30));
		if(z.equals("Yellow"))
			l.setForeground(Color.YELLOW);
		else if(z.equals("Blue"))
			l.setForeground(new Color(0,191,255));
		else if(z.equals("Red"))
			l.setForeground(new Color(246,72,72));
		else
			l.setForeground(new Color(0,229,145));
		newPanel.add(l);
		l.setBounds(x, y, 250,250);
	}
	public int buildColorPicker()
	{
		String[] options = {"red", "blue", "green", "yellow"};
		int s = JOptionPane.showOptionDialog(null, "Choose a color:", null, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
		return s;
	}
	public void buildEndText()
	{
		int x = 715;
		int y = 150;
		newPanel.removeAll();
		newPanel.setBackground(Color.DARK_GRAY);
		JLabel l = new JLabel(game.getLastTurn().getName()+" is the winner!!!");
		l.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,50));
		l.setForeground(Color.RED);
		newPanel.add(l);
		l.setBounds(x,y,1000,500);
		
//		JLabel newGameOutline = new JLabel();
//		newGameOutline.setBounds(870, 700, 200, 50);
//		newGameOutline.setOpaque(true);
//		newGameOutline.setBackground(Color.white);
		//newPanel.add(newGameOutline);
		
		JLabel newGame = new JLabel("NEW GAME");
		newGame.setFont(new Font("Trebuchet", Font.CENTER_BASELINE,35));
		newGame.setBounds(875, 550, 200, 50);
		newGame.setForeground(Color.green);
		newPanel.add(newGame);
		newGame.addMouseListener(new MouseListener() {
		    	public void mouseClicked(MouseEvent e)
		    	{
		    		
		    	    
		    	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
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
					game = new Board();
					gamestate = new GameState(game);
					game.start();
					frame.setVisible(false); 
					frame.dispose();
					try {
						GraphicsRunner newG = new GraphicsRunner(game, gamestate);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
		    });
	}
	public void toBufferedImage(String z,int index, int x, int y)
	{
		JLabel btn = new JLabel();
		try {
			BufferedImage img =ImageIO.read(getClass().getResource(z));
			btn.setIcon(new ImageIcon(img));
			newPanel.add(btn);
		    btn.setBounds(x,y,cardWidth,cardHeight);
		    ImageIcon i = new ImageIcon(img);
		    btn.setIcon(i);
		    
		    btn.addMouseListener(new MouseListener() {
		    	public void mouseClicked(MouseEvent e)
		    	{
		    		
		    	    
		    	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
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
					rebuildHistory();
					game.play(index);
		    		if(game.showTopCard().getColor().equals(UnoCard.Color.Wild))
		    		{
		    			int x = buildColorPicker();
		    			if(x==0)
		    				game.changeColor("red");
		    			else if(x==1)
		    				game.changeColor("blue");
		    			else if(x==2)
		    				game.changeColor("green");
		    			else if(x==3)
		    				game.changeColor("yellow");
		    			else
		    			{
		    				int rand = (int)(Math.random()*4+1);
		    				if(rand==0)
			    				game.changeColor("red");
			    			else if(rand==1)
			    				game.changeColor("blue");
			    			else if(rand==2)
			    				game.changeColor("green");
			    			else 
			    				game.changeColor("yellow");
		    			}
		    			
		    		}
		    		newPanel.removeAll();
		    		newPanel.revalidate();
		    		newPanel.repaint();
		    		page = 1;
		    		buildDiscard(950, 400);
		    	    buildDeck(670, 400);
		    	    buildHand(475, 800);
		    	    buildDirection(game.getCurrentDirection());
		    	    if(game.canPlay())
		    	    {
		    	    	buildHistory(game.getLastPlayerTurn());
		    	    }
		    	    buildColorTracker(game.showTopCard().getColor().toString());
		    	    if(gamestate.isOver())
		    			buildEndText();
					
				}
		    });
			newPanel.add(btn);
			//g.drawImage(img, x, y, cardWidth, cardHeight, null);
			
		} catch(Exception e)
		{}
	}
	public BufferedImage buf(String z)
	{
		try 
		{
			BufferedImage img = ImageIO.read(getClass().getResource(z));
			return img;
		} 
		catch(Exception e){return null;}
			
	}
	public BufferedImage rotateClockwise90(BufferedImage src) 
	{
	    int w = src.getHeight();
	    int h = src.getWidth();

	    BufferedImage dest = new BufferedImage(h,w, src.getType());

	    Graphics2D graphics2D = dest.createGraphics();
	    graphics2D.rotate(Math.toRadians(90), h / 2, w / 2);
	    graphics2D.drawImage(src, null,0,0);

	    return dest;
	}
	public BufferedImage rotateCounter90(BufferedImage src) 
	{
	    int width = src.getHeight();
	    int height = src.getWidth();

	    BufferedImage dest = new BufferedImage(height, width, src.getType());

	    Graphics2D graphics2D = dest.createGraphics();
	    graphics2D.translate((width-height) / 2, (height-width) / 2);
	    graphics2D.rotate(3*Math.PI/2, height / 2, width / 2);
	    graphics2D.drawImage(src, null,0,0);

	    return dest;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("X: "+e.getX()+", Y: "+e.getY());
	}
}

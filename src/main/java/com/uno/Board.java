package com.uno;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Board
{
	public Deck deck;
	public ArrayList<Player> playerList;
	public Player current_player;
	private Player lastPlayer;
	public int turn = 0;
	Direction direction;
	String lastMove = "";
	private boolean canPlay = true;
	public boolean cpu = false;
	public boolean cpuActive = false;
	public boolean started = false;
	public boolean laxWildCard = false;
	public boolean infiniteDraw = false;
	public int lastDraw= 0;
	public boolean firstTurn = false;
	public int maxTurn = 3;
	
	public enum Direction
	{
		Clockwise, Counter_Clockwise;
	}
	
	public Board()
	{
		playerList = new ArrayList<Player>();
		direction = Direction.Clockwise;
		deck = new Deck();
		for(int i = 0;i<4;i++)
			playerList.add(new Player("Player "+(i+1)));
		
		//playerList.get(0).getHand().add(new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild_Four));
		
	}
	public void start()
	{
		started = true;
		deal();
		current_player = playerList.get(0);
		deck.discard.add(deck.getCard());
		while(showTopCard().getValue().equals(UnoCard.Value.Wild_Four))
		{
			deck.deck.push(deck.discard.remove(deck.discard.size()-1));
			deck.shuffle();
			deck.discard.add(deck.getCard());
		}

		if(showTopCard().getValue().equals(UnoCard.Value.Wild))
			firstTurn = true;
		if(showTopCard().getValue().equals(UnoCard.Value.DrawTwo))
		{
			drawCard(current_player);
			drawCard(current_player);
			nextTurn();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(showTopCard().getValue().equals(UnoCard.Value.Reverse))
		{
			direction = Direction.Counter_Clockwise;
		}
		if (showTopCard().getValue().equals(UnoCard.Value.Skip))
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nextTurn();
		}
		System.out.println("Initial card is "+deck.discard.get(deck.discard.size()-1));
		current_player.sortHand(showTopCard().getColor(), showTopCard().getValue());
	}
	public void start(boolean cpus)
	{
		started = true;
		cpu = true;
		playerList.get(0).name = "Player";
		for(int i = 1;i<playerList.size();i++)
			playerList.get(i).name = "CPU "+i;
		
		deal();
		current_player = playerList.get(0);
		deck.discard.add(deck.getCard());
		
		while(showTopCard().getValue().equals(UnoCard.Value.Wild_Four))
		{
			deck.deck.add(0,deck.discard.remove(deck.discard.size()-1));
			deck.shuffle();
			deck.discard.add(deck.getCard());
		}

		if(showTopCard().getValue().equals(UnoCard.Value.Wild))
			firstTurn = true;
		if(showTopCard().getValue().equals(UnoCard.Value.DrawTwo))
		{
//			try {
//			Thread.sleep(150);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			drawCard(current_player);
			drawCard(current_player);
			nextTurn();
		}
		if(showTopCard().getValue().equals(UnoCard.Value.Reverse))
		{
			direction = Direction.Counter_Clockwise;
		}
		if (showTopCard().getValue().equals(UnoCard.Value.Skip))
		{
//			try {
//			Thread.sleep(250);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			nextTurn();
		}
		System.out.println("Initial card is "+deck.discard.get(deck.discard.size()-1));
		
		current_player.sortHand(showTopCard().getColor(), showTopCard().getValue());
	}
	public void start(ArrayList<String> names)
	{
		started = true;
		
		current_player = playerList.get(0);
		
//		deck.discard.add(new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild));
//		current_player.getHand().add(new UnoCard(UnoCard.Color.Wild, UnoCard.Value.Wild_Four));
		
		if (names.size()<4)
		{
			if(names.size()==2)
			{
				playerList.remove(3);
				playerList.remove(2);
				maxTurn = 1;
			}
			else
			{
				playerList.remove(3);
				maxTurn = 2;
			}
		}
		
		for(int i =0;i<playerList.size();i++)
		{
			playerList.get(i).name = names.get(i);
		}
		deal();
		deck.discard.add(deck.getCard());
		while(showTopCard().getValue().equals(UnoCard.Value.Wild_Four))
		{
			deck.deck.add(0,deck.discard.remove(deck.discard.size()-1));
			deck.shuffle();
			deck.discard.add(deck.getCard());
		}

		if(showTopCard().getValue().equals(UnoCard.Value.Wild))
			firstTurn = true;
		if(showTopCard().getValue().equals(UnoCard.Value.DrawTwo))
		{
//			try {
//			Thread.sleep(150);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			drawCard(current_player);
			drawCard(current_player);
			nextTurn();

		}
		if(showTopCard().getValue().equals(UnoCard.Value.Reverse))
		{

			direction = Direction.Counter_Clockwise;
		}
		if (showTopCard().getValue().equals(UnoCard.Value.Skip))
		{
//			try {
//			Thread.sleep(250);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			nextTurn();
		}
		System.out.println("Initial card is "+deck.discard.get(deck.discard.size()-1));
		current_player.sortHand(showTopCard().getColor(), showTopCard().getValue());
	}
	
	public void changeColor(String c)
	{
		deck.discard.get(deck.discard.size()-1).color = UnoCard.Color.valueOf((c.charAt(0)+"").toUpperCase()+c.substring(1)); 
		if(showTopCard().getValue().equals(UnoCard.Value.Wild_Four))
		{

			if(direction.equals(Direction.Clockwise))
			{
				if(turn==maxTurn)
				{
					drawCard(playerList.get(0));
					drawCard(playerList.get(0));
					drawCard(playerList.get(0));
					drawCard(playerList.get(0));
					turn=0;
					current_player = playerList.get(turn);
				}
				else
				{
					drawCard(playerList.get(turn+1));
					drawCard(playerList.get(turn+1));
					drawCard(playerList.get(turn+1));
					drawCard(playerList.get(turn+1));
					turn++;
					current_player = playerList.get(turn);
				}
			}
			else
			{
				if(turn ==0)
				{
					drawCard(playerList.get(maxTurn));
					drawCard(playerList.get(maxTurn));
					drawCard(playerList.get(maxTurn));
					drawCard(playerList.get(maxTurn));
					turn=maxTurn;
					current_player = playerList.get(turn);
				}

				else
				{
					drawCard(playerList.get(turn-1));
					drawCard(playerList.get(turn-1));
					drawCard(playerList.get(turn-1));
					drawCard(playerList.get(turn-1));
					turn--;
					current_player = playerList.get(turn);
				}
			}
			
		}
	}
	public void deal()
	{
		for(Player p: playerList)
			for(int i = 0;i<7;i++)
				drawCard(p);
	}
	public void checkPlayable()
	{
		if(firstTurn||current_player.getHand().get(current_player.getHandSize()-1).match(showTopCard()))
		{
			if(current_player.getHand().get(current_player.getHandSize()-1).getColor().equals(UnoCard.Color.Wild))
				return;
			UnoCard card = current_player.playCard(current_player.getHandSize()-1);
			
			deck.discard.add(card);
			current_player.getHand().remove(current_player.getHandSize()-1);
			setLastMove(current_player.getName()+" draw then play");
		
			if(showTopCard().getValue().equals(UnoCard.Value.Reverse))
			{
				if(direction.equals(Direction.Clockwise))
					direction = Direction.Counter_Clockwise;
				else
					direction = Direction.Clockwise;
			}
			if(showTopCard().getValue().equals(UnoCard.Value.DrawTwo))
			{
				if(direction.equals(Direction.Clockwise))
				{
					if(turn==maxTurn)
					{
						drawCard(playerList.get(0));
						drawCard(playerList.get(0));
						turn=0;
					}
					else
					{
						drawCard(playerList.get(turn+1));
						drawCard(playerList.get(turn+1));
						turn++;
					}
				}
				else
				{
					if(turn==0)
					{
						drawCard(playerList.get(maxTurn));
						drawCard(playerList.get(maxTurn));
						turn=maxTurn;
					}
					else
					{
						drawCard(playerList.get(turn-1));
						drawCard(playerList.get(turn-1));
						turn--;
					}
				}
					
			}
			if(showTopCard().getValue().equals(UnoCard.Value.Skip))
			{
				if(direction.equals(Direction.Clockwise))
				{
					if(turn==maxTurn)
					{
						turn=0;
						current_player = playerList.get(turn);
						System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
					}

					else	
					{
						turn++;
						current_player = playerList.get(turn);
						System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
					}
				}
				else
				{
					if(turn==0)
					{
						turn = maxTurn;
						current_player = playerList.get(turn);
						System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
					}

					else
					{
						turn--;
						current_player = playerList.get(turn);
						System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
					}
				}
			}

		}
		else
		{
			if(infiniteDraw)
			{
				drawCard();
			}
		}
	}
	public void shuffleDis()
	{
		Collections.shuffle(deck.discard);
	}
	
	public UnoCard showTopCard()
	{
		return deck.discard.get(deck.discard.size()-1);
	}

	public String getCurrentDirection()
	{
		return direction.toString();
	}

	public void setLastMove(String x)
	{
		lastMove = x;
	}
	public int getTurn()
	{
		return turn;
	}
	public Player getLastTurn()
	{
		int numPlayers = playerList.size();
		int t = turn;
		boolean drawCheck = lastMove.contains("draw then play")&&(showTopCard().getValue().equals(UnoCard.Value.DrawTwo)||
				showTopCard().getValue().equals(UnoCard.Value.Skip)||showTopCard().getValue().equals(UnoCard.Value.Wild_Four))?true:false;
		if(direction.equals(Direction.Clockwise))
		{
			if(lastMove.contains("Skip")||lastMove.contains("DrawTwo")||lastMove.contains("Wild_Four")||drawCheck)
			{
				t = ((t-2)+numPlayers)%(maxTurn+1);
			}
			else
			{
				t--;
				if(t<0)
					t = maxTurn;
			}
			
			
		}
		else
		{
			if(lastMove.contains("Skip")||lastMove.contains("DrawTwo")||lastMove.contains("Wild_Four")||drawCheck)
			{
				t = (t+2)%(maxTurn+1);
			}
			else
			{
				t++;
				if(t>maxTurn)
					t=0;
			}
			
			
		}
		lastPlayer = playerList.get(t);
		return lastPlayer;
	}
	public void fixWinTurn()
	{
		int numPlayers = playerList.size();
		int t = turn;
		if(direction.equals(Direction.Clockwise))
		{
			if(lastMove.contains("Skip")||lastMove.contains("DrawTwo")||lastMove.contains("Wild_Four"))
			{
				t = ((t-2)+numPlayers)%(maxTurn+1);
				
			}
			else
			{
				t--;
				if(t<0)
					t = maxTurn;
			}
		}
		else
		{
			if(lastMove.contains("Skip")||lastMove.contains("DrawTwo")||lastMove.contains("Wild_Four"))
			{

				t = (t+2)%(maxTurn+1);
			}
			else
			{
				t++;
				if(t>maxTurn)
					t=0;
			}	
		}
		
		turn = t;
		current_player = playerList.get(turn);
	}
	public void nextTurn()
	{
		
		if(direction.equals(Direction.Clockwise))
		{
			turn++;
			if(turn>maxTurn)
				turn = 0;
		}
		else
		{
			turn--;
			if(turn<0)
				turn=maxTurn;
			
		}
		current_player = playerList.get(turn);
		if(firstTurn)
			firstTurn = false;
		
		if(deck.deck.size()<1)
			deck.takeFromDis();
	}
	public int seeNextTurn()
	{
		int t = turn;
		if(direction.equals(Direction.Clockwise))
		{
			t++;
			if(t>maxTurn)
				t = 0;
		}
		else
		{
			t--;
			if(t<0)
				t=maxTurn;
			
		}
		return t;
	}
	public void cpuTurn()
	{
		cpuActive = true;
		current_player.sortHand(showTopCard().getColor(), showTopCard().getValue());
		UnoCard card = current_player.playCard(0);
		
		if(firstTurn||card.match(deck.discard.get(deck.discard.size()-1)))
		{
			
			play(0);
			if(card.getColor().equals(UnoCard.Color.Wild))
			{
				String color = getMostColor(current_player); 
				changeColor(color);
			}
			
		}
		else
		{

			drawCard();
			
		}
		nextTurn();
		cpuActive = false;
		
	}
	public String getMostColor(Player p)
	{
		int blue=0;int red=0;int yellow=0;int green=0;
		for(UnoCard c: p.getHand())
		{
			if(c.getColor().equals(UnoCard.Color.Blue))
				blue++;
			if(c.getColor().equals(UnoCard.Color.Red))
				red++;
			if(c.getColor().equals(UnoCard.Color.Green))
				green++;
			if(c.getColor().equals(UnoCard.Color.Yellow))
				yellow++;
		}
		if(yellow == Math.max(yellow, Math.max(red, Math.max(blue, green))))
			return "yellow";
		else if(blue == Math.max(blue, Math.max(red, Math.max(yellow, green))))
			return "blue";
		else if(green == Math.max(green, Math.max(red, Math.max(blue, yellow))))
			return "green";
		else
			return "red";
	}
	public boolean canPlay()
	{
		return canPlay;
	}
	public void drawCard()
	{
		if(deck.deck.size()<1)
			return;
		lastDraw++;
		UnoCard draw = deck.getCard();
		if (draw==null)
			return;
		if(draw.getColor().equals(UnoCard.Color.Wild))
		{
			if(turn==0)
			{
				setLastMove(current_player.name+" rid draw");
			}
			else
				setLastMove(current_player.name+" draw");
			
			drawTempCard(draw);
		}
		else
		{
			current_player.getHand().add(draw);
			System.out.println("drew a "+current_player.getHand().get(current_player.getHandSize()-1));
			if(turn==0)
			{
				setLastMove(current_player.name+" rid draw");
			}
			else
				setLastMove(current_player.name+" draw");
			checkPlayable();
		}
	}
	public void drawCard(Player p)
	{
		UnoCard c = deck.getCard();
		if(c!=null)
			p.getHand().add(c);
	}
	public void drawTempCard(UnoCard card)
	{
//		if(deck.deck.size()<1)
//			return;
		current_player.getTemp().add(card);
		if(cpu && turn!=0)
		{
			play(-1);
			String color = getMostColor(current_player); 
			changeColor(color);
		}
				
	}
	public int findFirstNonWild()
	{
		for(int i = 0;i<current_player.getHand().size();i++)
		{
			if(!current_player.getHand().get(i).getColor().equals(UnoCard.Color.Wild))
				return i;
		}
		return Integer.MAX_VALUE;
	}
	public void play(int index)
	{
		UnoCard card = null;
		current_player = playerList.get(turn);
		if(index<0)
		{
			card = current_player.getTempCard();
			if(card==null)
			{
				return;
			}
			deck.discard.add(card);
			if(turn==0)
			{
				setLastMove(current_player.name+" rid "+card.toString());
			}
			else
				setLastMove(current_player.name+" "+card.toString());
		}
		else
		{
			card = current_player.playCard(index);
			if(!laxWildCard &&card.getValue().equals(UnoCard.Value.Wild_Four)&&findFirstNonWild()<index)
			{
				card = null;
			}
			else if(firstTurn || card.match(deck.discard.get(deck.discard.size()-1)))
			{
				deck.discard.add(card);
				current_player.getHand().remove(index);
				if(turn==0)
				{
					setLastMove(current_player.name+" rid "+card.toString());
				}
				else
					setLastMove(current_player.name+" "+card.toString());
			}
			else card = null;
		}
		if(card==null)
		{
			System.out.println("cannot play that card");
			canPlay = false;
			return;
		}
		canPlay = true;
		System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
		System.out.println(current_player.getName());
		System.out.println(current_player.getHand());
	
		if(showTopCard().getValue().equals(UnoCard.Value.Reverse))
		{
			if(direction.equals(Direction.Clockwise))
				direction = Direction.Counter_Clockwise;
			else
				direction = Direction.Clockwise;
		}
		else if(showTopCard().getValue().equals(UnoCard.Value.DrawTwo))
		{
			if(direction.equals(Direction.Clockwise))
			{
				if(turn==maxTurn)
				{
					drawCard(playerList.get(0));
					drawCard(playerList.get(0));
					turn=0;
				}
				else
				{
					drawCard(playerList.get(turn+1));
					drawCard(playerList.get(turn+1));
					turn++;
				}
			}
			else
			{
				if(turn==0)
				{
					drawCard(playerList.get(maxTurn));
					drawCard(playerList.get(maxTurn));
					turn=maxTurn;
				}
				else
				{
					drawCard(playerList.get(turn-1));
					drawCard(playerList.get(turn-1));
					turn--;
				}
			}
				
		}
		else if(showTopCard().getValue().equals(UnoCard.Value.Skip))
		{
			if(direction.equals(Direction.Clockwise))
			{

				if(turn==maxTurn)
				{
					turn=0;
					current_player = playerList.get(turn);
					System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
				}

				else	
				{
					turn++;
					current_player = playerList.get(turn);
					System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
				}
			}
			else
			{
				if(turn==0)
				{
					turn = maxTurn;
					current_player = playerList.get(turn);
					System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
				}
				else
				{
					turn--;
					current_player = playerList.get(turn);
					System.out.println("top card is now "+deck.discard.get(deck.discard.size()-1));
				}
			}
				
		}
		if(current_player.getHandSize()==1)
			System.out.println("UNO");
		
		//nextTurn();
		
	}
	public boolean isOver()
	{
		boolean x = false;
		for(int i =0;i<playerList.size();i++)
		{
			if(playerList.get(i).getHandSize()==0)
				return true;
		}
		return x;
	}
	
	public String getLastPlayerTurn(String type)
	{

		if(type.equals("play"))
		{
			if(getLastTurn().getName().length()>25)
				return getLastTurn().getName().substring(0,23)+"... played a "+showTopCard().toString()+".";
			return getLastTurn().getName()+" played a "+showTopCard().toString()+".";
		}
		else if(type.equals("draw"))
		{
			if(getLastTurn().getName().length()>25)
			{
				if(infiniteDraw && lastDraw>1)
					return getLastTurn().getName().substring(0,23)+"... drew "+lastDraw+ " cards.";
				else
					return getLastTurn().getName().substring(0,23)+"... drew a card.";
			}
			if(infiniteDraw && lastDraw>1)
				return getLastTurn().getName()+" drew "+lastDraw+ " cards.";
			return getLastTurn().getName()+" drew a card.";
		}
		return null;
	}
	
}

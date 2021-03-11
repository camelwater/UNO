package com.uno;
import java.util.ArrayList;
import java.util.Collections;

public class Player
{
	public ArrayList<UnoCard>hand;
	public ArrayList<Player>playerList;
	private ArrayList<UnoCard> temp = new ArrayList<UnoCard>();
	public String name;
	Board game;
	
	public Player()
	{
		hand = new ArrayList<UnoCard>();
		playerList = new ArrayList<Player>();
	}
	public Player(String name)
	{
		hand = new ArrayList<UnoCard>();
		playerList = new ArrayList<Player>();
		this.name = name;
	}
	public UnoCard playCard(int i, Player p)
	{
		
		UnoCard card=p.getHand().get(i);
		
		return card;
	}
	public void setTempCard(UnoCard card)
	{
		temp.clear();
		temp.add(card);
	}
	public ArrayList<UnoCard> getTemp()
	{
		return temp;
	}
	public UnoCard getTempCard()
	{
		if(temp.size()>0)
			return temp.remove(0);
		return null;
	}
	public String getName()
	{
		return this.name;
	}
	public ArrayList<UnoCard> getHand()
	{
		return this.hand;
	}
	public int getHandSize()
	{
		return this.hand.size();
	}
	public String toString()
	{
		return hand.toString();
	}
	public String getPaintName()
	{
		if(name.length()>10)
			return name.substring(0,8)+"...";
		return name;
	}
	public void convertDraw()
	{
		hand.add(temp.remove(0));
	}
	public void sortHand(UnoCard.Color color, UnoCard.Value value)
	{
		for(int i = 0;i<hand.size()-1;i++)
			for(int j = i+1;j<hand.size();j++)
			{
				if(hand.get(i).getColor().toString().compareTo(hand.get(j).getColor().toString())>0)
				{
					Collections.swap(hand, i, j);
				}
			}
		
		for(int i = 0;i<hand.size()-1;i++)
			for(int j = i+1;j<hand.size();j++)
				if(hand.get(i).getColor().equals(hand.get(j).getColor()))
				{
					if(hand.get(i).toInt()>(hand.get(j).toInt()))
					{
						Collections.swap(hand, i, j);
					}
				}
		for(int i = 0;i<hand.size();i++)
		{
			if(hand.get(i).getColor().equals(UnoCard.Color.Wild))
				hand.add(0, hand.remove(i));
		}
		for(int i = 0;i<hand.size();i++)
		{
			if(hand.get(i).getValue().equals(value) && !hand.get(i).getColor().equals(color))
				hand.add(0, hand.remove(i));
		}
		for(int i = 0;i<hand.size();i++)
		{
			if(hand.get(i).getColor().equals(color))
				hand.add(0, hand.remove(i));
		}
		
		if(value.equals(UnoCard.Value.Skip) || value.equals(UnoCard.Value.DrawTwo) ||value.equals(UnoCard.Value.Reverse))
		{
			for(int i = 0;i<hand.size();i++)
				if(hand.get(i).getValue().equals(value))
					hand.add(0, hand.remove(i));
		}
			
			
			
				
	}
//	public String getLastPlayerrTurn()
//	{
//		return game.getLastTurn()+"played a "+Board.deck.discard.get(Board.deck.discard.size()-1);
//	}
	public Player getNextHand()
	{
		return playerList.get(game.turn+1);
	}
}


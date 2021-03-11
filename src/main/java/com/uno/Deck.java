package com.uno;
import java.util.Collections;
import java.util.*;


public class Deck 
{
	//public ArrayList<UnoCard>deck;
	public Stack<UnoCard>deck;
	public ArrayList<UnoCard>discard;
	public Deck()
	{
		//deck = new ArrayList<UnoCard>();
		deck = new Stack<UnoCard>();
		discard = new ArrayList<UnoCard>();
		UnoCard.Color[] col = new UnoCard.Color[] 
				{UnoCard.Color.Blue,UnoCard.Color.Yellow,UnoCard.Color.Red,UnoCard.Color.Green};
		UnoCard.Value[] val = new UnoCard.Value[] 
				{UnoCard.Value.Zero, UnoCard.Value.One, UnoCard.Value.Two, UnoCard.Value.Three, UnoCard.Value.Four, UnoCard.Value.Five, UnoCard.Value.Six, UnoCard.Value.Seven, UnoCard.Value.Eight, UnoCard.Value.Nine};
		for(UnoCard.Color c : col)
		{
			for(int j = 0;j < 2;j++)
			{
				for(UnoCard.Value v: val)
				{
					if(j==1 && v.equals(UnoCard.Value.Zero))
						continue;
					else
						deck.push(new UnoCard(c,v));
				}
			}
		}
		UnoCard.Color[] colors = new UnoCard.Color[] 
				{UnoCard.Color.Blue,UnoCard.Color.Yellow,UnoCard.Color.Red,UnoCard.Color.Green};
		for(UnoCard.Color c: colors)
		{
			UnoCard.Value[] values = new UnoCard.Value[] 
					{UnoCard.Value.DrawTwo,UnoCard.Value.Reverse, UnoCard.Value.Skip};
			for(int j = 0;j<2;j++)
			{
					deck.push(new UnoCard(c, values[0]));
					deck.push(new UnoCard(c, values[1]));
					deck.push(new UnoCard(c, values[2]));
			}
		}
		
			UnoCard.Value[] value = new UnoCard.Value[] {UnoCard.Value.Wild,UnoCard.Value.Wild_Four};
			for(int j = 0;j<4;j++)
			{
				deck.push(new UnoCard(UnoCard.Color.Wild, value[0]));
				deck.push(new UnoCard(UnoCard.Color.Wild, value[1]));
			}
		
		Collections.shuffle(deck);
	}
	public void shuffle()
	{
		Collections.shuffle(deck);
	}
	public UnoCard get(int i)
	{
		return this.deck.get(i);
	}
	public UnoCard getCard()
	{
		if(deck.size()<2)
			takeFromDis();
		try
		{
			//return deck.remove(deck.size()-1);
			return deck.pop();
		}catch(Exception e)
		{
			return null;
		}
	}
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for(UnoCard c:deck)
			sb.append(c.toString()+" ");
		
		return sb.toString();
	}
	public boolean addCard(UnoCard card)
	{
		return this.deck.add(card);
	}
	public void addToDis(UnoCard card)
	{
		discard.add(card);
	}
	public int size()
	{
		return deck.size();
	}
	public boolean isEmpty()
	{
		return deck.size()==0;
	}
	public void takeFromDis()
	{
		
		for(int i =discard.size()-2;i>-1;i--)
		{
			deck.push(discard.remove(i));
			//System.out.println("CARDS IN DISCARD: "+discard.size());
		}
		shuffle();
		for(UnoCard c:deck)
		{
			if(c.getValue().equals(UnoCard.Value.Wild)||c.getValue().equals(UnoCard.Value.Wild_Four))
				c.color = UnoCard.Color.Wild;

		}
		//System.out.println("DECK ADDED NEW CARDS");
	}
	
}


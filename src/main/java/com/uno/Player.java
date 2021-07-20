package com.uno;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Player
{
	public ArrayList<UnoCard>hand;
	private ArrayList<UnoCard> temp = new ArrayList<UnoCard>();
	public String name;
	
	public Player()
	{
		hand = new ArrayList<UnoCard>();
	}
	public Player(String name)
	{
		hand = new ArrayList<UnoCard>();
		this.name = name;
	}
	public UnoCard playCard(int i)
	{
		
		UnoCard card= this.hand.get(i);
		
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
	@SuppressWarnings("unchecked")
	public void sortHand(UnoCard.Color color, UnoCard.Value value)
	{
//		long start = System.nanoTime();
		ArrayList<UnoCard> tempHand = new ArrayList<UnoCard>();
		HashMap<String,ArrayList<UnoCard>> map = new HashMap<String,ArrayList<UnoCard>>();
		
		map.put("Color", new ArrayList<UnoCard>());
		map.put("Value", new ArrayList<UnoCard>());
		map.put("Wild", new ArrayList<UnoCard>());

		Collections.sort(hand);
		
		int j = 0;
		while (j<hand.size())
		{
			UnoCard c = hand.get(j);
			
			if (c.getColor().equals(UnoCard.Color.Wild))
				map.get("Wild").add(hand.remove(j));
			else if (c.getValue().equals(value) && !c.getColor().equals(color))
				map.get("Value").add(hand.remove(j));
			else if (c.getColor().equals(color))
				map.get("Color").add(hand.remove(j));
			else
				j++;
		}
		
		boolean wildFirst = map.get("Color").size() + map.get("Value").size() > 0 ? true: false;
		if (wildFirst)
		{
			ArrayList<UnoCard> wildIter = map.get("Wild");
			Collections.sort(wildIter);
			Collections.reverse(wildIter);
		}
		
		tempHand.addAll(map.get("Color"));
		tempHand.addAll(map.get("Value"));
		tempHand.addAll(map.get("Wild"));

		if(value.equals(UnoCard.Value.Skip) || value.equals(UnoCard.Value.DrawTwo) ||value.equals(UnoCard.Value.Reverse))
		{
			int lastIndex = 0;
			for(int i = 0;i<tempHand.size();i++)
				if(tempHand.get(i).getValue().equals(value))
				{
					tempHand.add(lastIndex, tempHand.remove(i));
					lastIndex++;
				}
		}
		
		tempHand.addAll(hand);
		hand = tempHand;
		
//		System.out.println("SORTING TIME: " + (System.nanoTime()-start));
					
	}
	
	@SuppressWarnings("unchecked")
	public void sortHand2(UnoCard.Color color, UnoCard.Value value)
	{
		long start = System.nanoTime();
		Collections.sort(hand);
		
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
		System.out.println("SORTING TIME: " + (System.nanoTime() - start));
	}
}


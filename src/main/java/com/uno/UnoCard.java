package com.uno;

public class UnoCard 
{
	enum Color
	{
		 Red,Blue,Green,Yellow,Wild;
	}	 
	enum Value
	{
		Zero, One, Two, Three, Four, Five, Six, Seven, Eight, Nine, DrawTwo, Skip, Reverse, Wild, Wild_Four;
	}
	
	public Color color;
	public Value value;
	
	public UnoCard(Color c,  Value v)
	{
		color = c;
		value = v;
	}
	public Color getColor()
	{
		return this.color;
	}
	public Value getValue()
	{
		return this.value;
	}
	public boolean match(UnoCard a, UnoCard b)
	{
		if(a.color.equals(Color.Wild))
			return true;
		if (a.value.equals(b.value)||a.color.equals(b.color))
			return true;
		return false;
	}
	public boolean match(UnoCard card) //this = card being played, card = card on the discard pile
	{
		if(this.color.equals(Color.Wild)) //only this card can be wild, not "card" 
			return true;
		if (this.value.equals(card.value)|| this.color.equals(card.color))
			return true;
		return false;
	}
	public String toString()
	{
		return color+" "+value;
	}
	public int toInt()
	{
		if(value.toString().equals("Zero"))
			return 0;
		else if(value.toString().equals("One"))
			return 1;
		else if(value.toString().equals("Two"))
			return 2;
		else if(value.toString().equals("Three"))
			return 3;
		else if(value.toString().equals("Four"))
			return 4;
		else if(value.toString().equals("Five"))
			return 5;
		else if(value.toString().equals("Six"))
			return 6;
		else if(value.toString().equals("Seven"))
			return 7;
		else if(value.toString().equals("Eight"))
			return 8;
		else if(value.toString().equals("Nine"))
			return 9;
		else if (value.toString().equals("Reverse"))
			return 10;
		else if(value.toString().equals("Skip"))
			return 11;
		else if(value.toString().equals("DrawTwo"))
			return 12;
		else if (value.toString().equals("Wild"))
			return 13;
		else if(value.toString().equals("Wild_Four"))
			return 14;
		else
			return 14;
	}
	
	
}

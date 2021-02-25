package com.uno;
import java.util.ArrayList;

public class GameState
{
	boolean over = false;
	Board game;
	public GameState(Board game)
	{
		this.game = game;
	}
	public boolean isForward()
	{
		return game.direction == Board.Direction.Clockwise;
	}
	public boolean isOver()
	{
		if(game.started && !over)
			for(int i =0;i<game.playerList.size();i++)
			{
				if(game.playerList.get(i).getHandSize()==0)
				{
					game.fixWinTurn();
					over = true;
					return over;
				}
			}
		return over;
	}
	public boolean isUno(Player p)
	{
		boolean x = false;
		if(p.getHandSize()==1)
			return true;
		return x;
	}
	public Player getCurrentPlayer()
	{
		return game.current_player;
	}
	public String getCurrentDirection()
	{
		return game.direction.toString();
	}
	public int getNumOfDeckCards()
	{
		return game.deck.deck.size();
	}
	public boolean isUp(UnoCard card)
	{
		if(game.current_player.getHand().contains(card))
			return true;
		if(game.deck.discard.contains(card))
			return true;
		return false;
	}
	public int getNumPlayers()
	{
		return game.playerList.size();
	}
	public ArrayList<Player> getPlayers()
	{
		return game.playerList;
	}
	public boolean isUno()
	{
		return game.current_player.getHandSize()==1;
	}
	public int getNumOfCardsLeft()
	{
		int x = 0;
		for(Player p:game.playerList)
		{
			x+=p.getHandSize();
		}
		return x;
	}
	public UnoCard getLastPlay()
	{
		return game.deck.discard.get(game.deck.discard.size()-1);
	}
	public UnoCard showCurrentCard()
	{
		return game.showTopCard();
	}
	public ArrayList<UnoCard> getCurrentHand()
	{
		return game.current_player.getHand();
	}
	public int getCurrentTurn()
	{
		return game.getTurn();
	}
	public boolean isDeckGone()
	{
		return game.deck.deck.isEmpty();
		
	}
	public boolean isDiscardGone()
	{
		return game.deck.discard.isEmpty();
	}
	public boolean areBothEmpty()
	{
		return game.deck.discard.isEmpty()&& game.deck.deck.isEmpty();
	}
	public UnoCard peekDeckCard()
	{
		return game.deck.deck.get(game.deck.deck.size()-1);
	}
}

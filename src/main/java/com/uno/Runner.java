package com.uno;
//import java.util.Scanner;
public class Runner 
{
	public static void main(String args[]) throws Exception
	{
		Board game = new Board();
		GameState gamestate = new GameState(game);
		
		//game.start();
		UnoGraphics g = new UnoGraphics(game, gamestate);
		
	}
}

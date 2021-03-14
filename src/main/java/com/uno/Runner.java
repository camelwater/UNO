package com.uno;
import javax.swing.SwingUtilities;

public class Runner 
{
	public static void main(String args[])
	{
		//game.start();
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	Board game = new Board();
				GameState gamestate = new GameState(game);
		        new UnoGraphics(game, gamestate);
		    }
		});
	}
}

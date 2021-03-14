package com.uno;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class Runner 
{
	public static void main(String args[]) throws InvocationTargetException, InterruptedException
	{
		//game.start();
		EventQueue.invokeAndWait(new Runnable() {
		    public void run() {
		    	Board game = new Board();
				GameState gamestate = new GameState(game);
		        new UnoGraphics(game, gamestate);
		    }
		});
	}
}

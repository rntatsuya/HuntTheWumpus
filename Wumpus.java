/*	Tatsuya Yokota
	Wumpus.java
	11/28/16	*/

import java.awt.Graphics;
import java.awt.Color;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.Random;

public class Wumpus extends Agent {
	private Vertex homeRoom;
	private boolean gameEnded;
	private int size;
	private BufferedImage image;
	private BufferedImage imageRight;
	private Random gen;
	private boolean lose;
	private boolean angry;
	private boolean befriended;
	private int moveTextPos;
	private boolean moveTextRight;
	private int count;
	private boolean faceRight;
	
	public Wumpus(int x, int y, Vertex home) {
		super(x,y);
		homeRoom = home;
		gameEnded = false;
		lose = false;
		angry = false;
		befriended = false;
		moveTextPos = 150;
		moveTextRight = true;
		size = 5;
		gen = new Random();
		count = 0;
		faceRight = false;
		try {               
    		image = ImageIO.read(new File("wumpus.png"));
    		imageRight = ImageIO.read(new File("wumpusRight.png"));
    	}
    	catch (IOException e) {
	    	//Not handled.
	    }
	}
	
	public Vertex getRoom() {
		return homeRoom;
	}
	
	public void changeRoom(Vertex nextRoom) {
		homeRoom = nextRoom;
	}
	// called when the gameEnded 
	public void gameEnded() {
		gameEnded = true;
	}
	// called when the player loses
	public void lose() {
		lose = true;
	}
	// called when player insulted the Wumpus too much//
	public void madeAngry() {
		angry = true;
	}
	// called when player complimented the Wumpus enough//
	public void befriend() {
		befriended = true;
	}
	
	public void draw(Graphics g, int gridScale) {
		if (gameEnded) {
			int xpos = homeRoom.getCol()*gridScale;
			int ypos = homeRoom.getRow()*gridScale;
			int xposHalf = homeRoom.getCol()/2*gridScale;
			int yposHalf = homeRoom.getRow()/2*gridScale;
			int radius = 2*gridScale;
			int half = 5*gridScale / 2;
    	    int eighth = 5*gridScale / 8;
	        int sixteenth = 5*gridScale / 16;
			
			// draw the Wumpus' room first
			homeRoom.setVisible();
			homeRoom.draw(g, gridScale);
			
			///////// DANCING LOGIC /////////
			if (count == 20) {
				faceRight = true;
			}
			else if (count == 0) {
				faceRight = false;
			}
			if (faceRight) {
				count--;
				g.drawImage(imageRight ,xpos+half-eighth-sixteenth, ypos+half-eighth-sixteenth, radius, radius, null);
			}
			else {
				count++;
				g.drawImage(image ,xpos+half-eighth-sixteenth, ypos+half-eighth-sixteenth, radius, radius, null);
			}
			
			//////  DIFFERENT TEXT FOR DIFFERENT SITUATIONS ///////// 
			if (lose) {
				g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
				g.drawString("YOU GOT EATEN",250, half);
				g.setFont(new Font("ComicSans", Font.PLAIN, 30)); 
				g.drawString("HAHAHAHAHAHAHAHA",gen.nextInt(500), gen.nextInt(500));
				g.drawString("HAHAHAHAHAHAHAHA",gen.nextInt(500), gen.nextInt(500));
			}
			else if (angry) {
				if (moveTextPos == 100) {
					moveTextRight = true;
				}
				else if (moveTextPos == 200) {
					moveTextRight = false;
				}
				if (moveTextRight) {
					moveTextPos++;
				}
				else {
					moveTextPos--;
				}
				g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
				g.drawString("YOU INSULTED THE WUMPUS AND HE JUST CAME AND ATE YOU",moveTextPos, half);
				g.setFont(new Font("ComicSans", Font.PLAIN, 30)); 
				g.drawString("HAHAHAHAHAHAHAHA",gen.nextInt(500), gen.nextInt(500));
				g.drawString("HAHAHAHAHAHAHAHA",gen.nextInt(500), gen.nextInt(500));
			}
			else if (befriended) {
				if (moveTextPos == 150) {
					moveTextRight = true;
				}
				else if (moveTextPos == 230) {
					moveTextRight = false;
				}
				if (moveTextRight) {
					moveTextPos++;
				}
				else {
					moveTextPos--;
				}
				g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
				g.drawString("CONGRATS, YOU BEFRIENDED THE WUMPUS!",moveTextPos, half);
			}
		}
	}

}
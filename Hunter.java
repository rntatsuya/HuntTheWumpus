/*	Tatsuya Yokota
	Hunter.java
	11/28/16	*/

import java.awt.Graphics;
import java.awt.Color;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class Hunter extends Agent {
	private Vertex curRoom;
	private int size;
	private BufferedImage imageFacingRight;
	private BufferedImage imageFacingLeft;
	private boolean faceRight;
	private boolean win;

	public Hunter(int x, int y, Vertex initRoom) {
		super(x, y);
		curRoom = initRoom;
		size = 3;
		faceRight = true;
		win = false;
		try {
			imageFacingRight = ImageIO.read(new File("./images/hunterSamusRight.png"));
	    	imageFacingLeft = ImageIO.read(new File("./images/hunterSamusLeft.png"));

    	}
		catch (IOException e) {
	      //Not handled.
    	}
	}

	public Vertex currentRoom() {
		return curRoom;
	}

	public void changeRoom(Vertex nextRoom) {
		curRoom = nextRoom;
	}

	public void faceRight() {
		faceRight = true;
	}

	public void faceLeft() {
		faceRight = false;
	}

	public void player2() {
		try {
			imageFacingRight = ImageIO.read(new File("./images/hunterBlackRight.png"));
		    imageFacingLeft = ImageIO.read(new File("./images/hunterBlackLeft.png"));
    	}
		catch (IOException e) {
	      //Not handled.
    	}
	}

	public void win() {
		win = true;
	}

	public void draw(Graphics g, int gridScale) {
		int xpos = curRoom.getCol()*gridScale;
        int ypos = curRoom.getRow()*gridScale;
        int radius = 2*gridScale;
        int half = 5*gridScale / 2;
        int eighth = 5*gridScale / 8;
        int sixteenth = 5*gridScale / 16;

     //   g.setColor(Color.green);
        // be in the middle of current room
    	if (faceRight) {
    		g.drawImage(imageFacingRight ,xpos+half-eighth-sixteenth, ypos+half-eighth-sixteenth, radius, radius, null);
    	}
    	else {
    		g.drawImage(imageFacingLeft ,xpos+half-eighth-sixteenth, ypos+half-eighth-sixteenth, radius, radius, null);
    	}

    	if (win) {
    		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.drawString("YOU KILLED THE WUMPUS!",250, half);
    	}
	}


}

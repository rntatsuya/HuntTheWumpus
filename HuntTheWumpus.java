/* 	Tatsuya Yokota
	HuntTheWumpus.java
	10/18/16 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Point;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

import java.util.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import javax.sound.sampled.*;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import java.io.FileNotFoundException;


public class HuntTheWumpus extends JFrame {
	protected Landscape scape;
    private LandscapePanel canvas;
    private int gridScale; // width (and height) of each square in the grid
    private Control control;
    private Hunter hunter1;
    private Hunter hunter2;    
    private Wumpus wumpus;
    private boolean mounted1;
    private boolean mounted2;
    private boolean gameEnded;
    private JTextField convo;
    private WordCounter goodWordCounter;
    private WordCounter badWordCounter;
    private boolean tripping;
    private JLabel wumpWords;
    private JLabel aimWisely;
    private int happinessCount;
    private boolean multiplayer;
    
	// controls whether the simulation is playing or exiting
	private enum PlayState { PLAY, STOP }
	private PlayState state= PlayState.PLAY;
	
	public HuntTheWumpus(Landscape scape, int scale) throws FileNotFoundException, IOException {
		super("Hunt The Wumpus");
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		
		this.scape = scape;
        this.gridScale = scale;
		
		this.canvas = new LandscapePanel( this.scape.getCols() * this.gridScale,
                                         this.scape.getRows() * this.gridScale );
		this.add( this.canvas, BorderLayout.CENTER );
		this.pack();
		this.setVisible( true );
		
		// add the all the components 
		JButton quit = new JButton("Quit");
		JButton restart = new JButton("Restart");
		JButton trippy = new JButton("Trippy");
		JButton tell = new JButton("Talk to Wumpus");
		convo = new JTextField(20);
		wumpWords = new JLabel("Wumpus Words");
		wumpWords.setText("WUMPUS: \"So, you came into my cave little hunter...\"");
		aimWisely = new JLabel("Aim");
		aimWisely.setText("Find the Wumpus!");
		JPanel panelInputs = new JPanel( new FlowLayout(FlowLayout.CENTER));
		JPanel panelDialogue = new JPanel( new FlowLayout(FlowLayout.CENTER));
		JPanel panelDown = new JPanel( new FlowLayout(FlowLayout.CENTER));
		JPanel panelUp = new JPanel( new FlowLayout(FlowLayout.LEFT));
		
		// add components to each panel
		panelUp.add( quit );
		panelUp.add( restart );
		panelUp.add( trippy );
		panelUp.add( aimWisely );
		panelInputs.add(convo);
		panelInputs.add(tell);
		panelDialogue.add(wumpWords);
		panelDown.setLayout(new BoxLayout(panelDown,BoxLayout.Y_AXIS)); // stack on top
		panelDown.add(panelInputs);
		panelDown.add(panelDialogue);
		
		// add panels to the BorderLayout
		this.add( panelUp, BorderLayout.NORTH);
		this.add( panelDown, BorderLayout.SOUTH);
		this.pack();
		
		// add the control
		control = new Control();
		this.addKeyListener(control);
		this.setFocusable(true);
		this.requestFocus();
		
		quit.addActionListener( control );
		restart.addActionListener( control );
		trippy.addActionListener(control);
		tell.addActionListener(control);
		
		// check if the game is multiplayer
		if (!scape.multiplayer()) {
			hunter1 = scape.getHunter1();
		}
		else {
			hunter1 = scape.getHunter1();
			hunter2 = scape.getHunter2();
		}
		
		// initialize necessary booleans and references
		wumpus = scape.getWumpus();
		mounted1 = false;
		mounted2 = false;
		gameEnded = false;
		tripping = false;
		happinessCount = 0;
		
		// load the wordCounter with good and bad words
		goodWordCounter = new WordCounter();
		goodWordCounter.loadFromOriginalWordsFile("positive-words.txt");
		badWordCounter = new WordCounter();
		badWordCounter.loadFromOriginalWordsFile("bad-words.txt");
	}

	// used to regain focus to the canvas after pressing buttons 
	protected void focus() {
		this.requestFocus();
	}
	
	public LandscapePanel getCanvas() {
		return canvas;
	}
	
	public boolean getTrippy() {
		return tripping;
	}
	
	/**
     * This inner class provides the panel on which Landscape elements
     * are drawn.
     */
    private class LandscapePanel extends JPanel
    {
        /**
         * Creates the panel.
         * @param width     the width of the panel in pixels
         * @param height        the height of the panel in pixels
         */
        public LandscapePanel(int width, int height)
        {
                super();
                this.setPreferredSize(new Dimension(width, height));
                this.setBackground(Color.white);
        }

        /**
         * Method overridden from JComponent that is responsible for
         * drawing components on the screen.  The supplied Graphics
         * object is used to draw.
         * 
         * @param g     the Graphics object used for drawing
         */
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            scape.draw( g, gridScale );    
        } // end paintComponent
        
    } // end LandscapePanel
	
// 	public static synchronized void playSound(final String url) {
// 		new Thread(new Runnable() {
// 		// The wrapper thread is unnecessary, unless it blocks on the
//   		// Clip finishing; see comments.
//     	public void run() {
//       		try {
//         		Clip clip = AudioSystem.getClip();
//         		AudioInputStream inputStream = AudioSystem.getAudioInputStream(
//           			Main.class.getResourceAsStream(url));
//         		clip.open(inputStream);
//         		clip.start(); 
//       		} catch (Exception e) {
//         		System.err.println(e.getMessage());
//       		}
//     	}
//   		}).start();
// 	}
// 	
	private class Control extends KeyAdapter implements ActionListener {
		
		
        public void keyTyped(KeyEvent e) {
           // System.out.println( "Key Pressed: " + e.getKeyChar() );
            if (gameEnded) {
            	return;
            }
            if( ("" + e.getKeyChar()).equalsIgnoreCase("q") ) {
                state = PlayState.STOP;
            }
            ///// Player 1 Moving and arrow shooting logic
			if (e.getKeyChar() == 'w') {
				Vertex nextRoom = hunter1.currentRoom().getNeighbor(Vertex.Direction.NORTH);
				if (mounted1) {
					if (nextRoom == wumpus.getRoom()) {
						System.out.println("you win");
						wumpus.gameEnded();
						gameEnded = true;
						tripping = true;
						hunter1.win();
					}
					else {
						System.out.println("you lose");
						wumpus.gameEnded();
						wumpus.lose();
						gameEnded = true;
					}
				}
				else {
					if (nextRoom == wumpus.getRoom()) {
						System.out.println("you lose");
						wumpus.gameEnded();
						wumpus.lose();
						gameEnded = true;
					}
					if (nextRoom != null) {
						hunter1.changeRoom(nextRoom);
						nextRoom.setVisible();
					}
				}
			}
			if (e.getKeyChar() == 's') {
				Vertex nextRoom = hunter1.currentRoom().getNeighbor(Vertex.Direction.SOUTH);
				if (mounted1) {
					if (nextRoom == wumpus.getRoom()) {
						System.out.println("you win");
						wumpus.gameEnded();
						gameEnded = true;
						tripping = true;
						hunter1.win();
					}
					else {
						System.out.println("you lose");
						wumpus.gameEnded();
						wumpus.lose();
						gameEnded = true;
					}
				}
				else {
					if (nextRoom == wumpus.getRoom()) {
						System.out.println("you lose");
						wumpus.gameEnded();
						wumpus.lose();
						gameEnded = true;
					}
					if (nextRoom != null) {
						hunter1.changeRoom(nextRoom);
						nextRoom.setVisible();
					}
				}
			}
			if (e.getKeyChar() == 'a') {
				Vertex nextRoom = hunter1.currentRoom().getNeighbor(Vertex.Direction.WEST);
				if (mounted1) {
					if (nextRoom == wumpus.getRoom()) {
						System.out.println("you win");
						wumpus.gameEnded();
						gameEnded = true;
						tripping = true;
						hunter1.win();
					}
					else {
						System.out.println("you lose");
						wumpus.gameEnded();
						wumpus.lose();
						gameEnded = true;
					}
				}
				else {
					if (nextRoom == wumpus.getRoom()) {
						System.out.println("you lose");
						wumpus.gameEnded();
						wumpus.lose();
						gameEnded = true;
					}
					if (nextRoom != null) {
						hunter1.changeRoom(nextRoom);
						hunter1.faceLeft();
						nextRoom.setVisible();
					}
				}
			}
			if (e.getKeyChar() == 'd') {
				Vertex nextRoom = hunter1.currentRoom().getNeighbor(Vertex.Direction.EAST);
				if (mounted1) {
					if (nextRoom == wumpus.getRoom()) {
						System.out.println("you win");
						wumpus.gameEnded();
						gameEnded = true;
						tripping = true;
						hunter1.win();
					}
					else {
						System.out.println("you lose");
						wumpus.gameEnded();
						wumpus.lose();
						gameEnded = true;
					}
				}
				else {
					if (nextRoom == wumpus.getRoom()) {
						System.out.println("you lose");
						wumpus.gameEnded();
						wumpus.lose();
						gameEnded = true;
					}
					if (nextRoom != null) {
						hunter1.changeRoom(nextRoom);
						hunter1.faceRight();
						nextRoom.setVisible();
					}
				}
			}
			if (e.getKeyChar() == ' ') {
				if (!mounted1) {
					System.out.println("Arrow is mounted!"); // maybe I can say aim well later on...
//					playSound("mountArrow.wav").run();
					aimWisely.setText("Aim wisely, young one...");
					mounted1 = true;
				}
				else {
					System.out.println("Unmounted arrow!");
					aimWisely.setText("Find the Wumpus!");
					mounted1 = false;
				}
			}
			// if it is multiplayer, player 2 moving and arrow logic
			if (scape.multiplayer()) {
				if (e.getKeyChar() == 'i') {
					Vertex nextRoom = hunter2.currentRoom().getNeighbor(Vertex.Direction.NORTH);
					if (mounted2) {
						if (nextRoom == wumpus.getRoom()) {
							System.out.println("you win");
							wumpus.gameEnded();
							gameEnded = true;
							tripping = true;
							hunter2.win();
						}
						else {
							System.out.println("you lose");
							wumpus.gameEnded();
							wumpus.lose();
							gameEnded = true;
						}
					}
					else {
						if (nextRoom == wumpus.getRoom()) {
							System.out.println("you lose");
							wumpus.gameEnded();
							wumpus.lose();
							gameEnded = true;
						}
						if (nextRoom != null) {
							hunter2.changeRoom(nextRoom);
							nextRoom.setVisible();
						}
					}
				}
				if (e.getKeyChar() == 'k') {
					Vertex nextRoom = hunter2.currentRoom().getNeighbor(Vertex.Direction.SOUTH);
					if (mounted2) {
						if (nextRoom == wumpus.getRoom()) {
							System.out.println("you win");
							wumpus.gameEnded();
							gameEnded = true;
							tripping = true;
							hunter2.win();
						}
						else {
							System.out.println("you lose");
							wumpus.gameEnded();
							wumpus.lose();
							gameEnded = true;
						}
					}
					else {
						if (nextRoom == wumpus.getRoom()) {
							System.out.println("you lose");
							wumpus.gameEnded();
							wumpus.lose();
							gameEnded = true;
						}
						if (nextRoom != null) {
							hunter2.changeRoom(nextRoom);
							nextRoom.setVisible();
						}
					}
				}
				if (e.getKeyChar() == 'j') {
					Vertex nextRoom = hunter2.currentRoom().getNeighbor(Vertex.Direction.WEST);
					if (mounted2) {
						if (nextRoom == wumpus.getRoom()) {
							System.out.println("you win");
							wumpus.gameEnded();
							gameEnded = true;
							tripping = true;
							hunter2.win();
						}
						else {
							System.out.println("you lose");
							wumpus.gameEnded();
							wumpus.lose();
							gameEnded = true;
						}
					}
					else {
						if (nextRoom == wumpus.getRoom()) {
							System.out.println("you lose");
							wumpus.gameEnded();
							wumpus.lose();
							gameEnded = true;
						}
						if (nextRoom != null) {
							hunter2.changeRoom(nextRoom);
							hunter2.faceLeft();
							nextRoom.setVisible();
						}
					}
				}
				if (e.getKeyChar() == 'l') {
					Vertex nextRoom = hunter2.currentRoom().getNeighbor(Vertex.Direction.EAST);
					if (mounted2) {
						if (nextRoom == wumpus.getRoom()) {
							System.out.println("you win");
							wumpus.gameEnded();
							gameEnded = true;
							tripping = true;
							hunter2.win();
						}
						else {
							System.out.println("you lose");
							wumpus.gameEnded();
							wumpus.lose();
							gameEnded = true;
						}
					}
					else {
						if (nextRoom == wumpus.getRoom()) {
							System.out.println("you lose");
							wumpus.gameEnded();
							wumpus.lose();
							gameEnded = true;
						}
						if (nextRoom != null) {
							hunter2.changeRoom(nextRoom);
							hunter2.faceRight();
							nextRoom.setVisible();
						}
					}
				}
				if (e.getKeyChar() == '\n') {
					if (!mounted2) {
						System.out.println("Arrow is mounted!"); 
	//					playSound("mountArrow.wav").run();
						aimWisely.setText("Aim wisely, young one...");
						mounted2 = true;
					}
					else {
						System.out.println("Unmounted arrow!");
						aimWisely.setText("Find the Wumpus!");
						mounted2 = false;
					}
				}			
			}
        }
		
		// all the button and text input logic ///////////////////////////////
        public void actionPerformed(ActionEvent event) {
            if( event.getActionCommand().equalsIgnoreCase("Trippy") ) {
                // change the color of the background
                if (tripping) {
                	tripping = false;
                }
                else {
                	tripping = true;
                }
                focus();
            }
            else if( event.getActionCommand().equalsIgnoreCase("Quit") ) {
                state = PlayState.STOP;
            }
            else if( event.getActionCommand().equalsIgnoreCase("Restart") ) {
                scape.restart();
                hunter1 = scape.getHunter1();
                if (scape.multiplayer()) {
                	hunter2 = scape.getHunter2();
                }
				wumpus = scape.getWumpus();
				mounted1 = false;
				mounted2 = false;
				gameEnded = false;
				tripping = false;
				wumpWords.setText("WUMPUS: \"So, you came into my cave little hunter...\"");
				aimWisely.setText("Find the Wumpus!");
				canvas.setBackground(Color.white);
                focus();
                System.out.println("pressed");
            }
            else if ( event.getActionCommand().equalsIgnoreCase("Talk to Wumpus")) {
            	System.out.println(convo.getText());
            	String line = convo.getText();
				String[] parse = line.split("[^a-zA-Z'0-9]");
				for (int i = 0; i < parse.length; i++) {
					String word = parse[i].trim().toLowerCase();
					if (!word.isEmpty()) {
						if (goodWordCounter.getInitMapKeyCount(word) != 0) {
							System.out.println("good word: "+word);
							System.out.println(goodWordCounter.getInitMapKeyCount(word));
							if (happinessCount < 0) {
								happinessCount = 0;
							}
							else {
								happinessCount++;
							}
						}
						else if (badWordCounter.getInitMapKeyCount(word) != 0) {
							System.out.println("bad word: "+word);
							System.out.println(badWordCounter.getInitMapKeyCount(word));
							if (happinessCount > 0) {
								happinessCount = 0;
							}
							else {
								happinessCount--;
							}
						}
					}
            	}
            	System.out.println("The happiness count is: "+happinessCount);
            	if (happinessCount == 1) {
            		wumpWords.setText("WUMPUS: \"Hmmm, you're better than you seem...\"");
            	}
            	else if (happinessCount == 2) {
            		wumpWords.setText("WUMPUS: \"You seem nice...\"");
            	}
            	else if (happinessCount == 3) {
            		wumpWords.setText("WUMPUS: \"Wow, you're so nice... I've always been lonely...\"");
            	}
            	else if (happinessCount >= 4) {
            		wumpWords.setText("WUMPUS: \"Let's be friends!\"");
            		wumpus.gameEnded();
            		tripping = true;
            		wumpus.befriend();
            		gameEnded = true;
            	}
            	else if (happinessCount == -1) {
            		wumpWords.setText("WUMPUS: \"You better stop with those bad words...\"");
            	}
            	else if (happinessCount == -2) {
            		wumpWords.setText("WUMPUS: \"I'm warning you...\"");
            	}
            	else if (happinessCount == -3) {
            		wumpWords.setText("WUMPUS: \"You're not listening to me!!!\"");
            	}
            	else if (happinessCount <= -4) {
            		wumpWords.setText("WUMPUS: \"I am ANGRY! AHHHHHHH!!\"");
            		wumpus.gameEnded();
            		wumpus.madeAngry();
            		gameEnded = true;
            	}
            	else {
            		wumpWords.setText("Wumpus: \"I don't know how to feel about that\"");
            	}
            	convo.setText("");
				focus();
        	}
        }
    } // end class Control

	public static void main( String[] argv ) throws InterruptedException, FileNotFoundException, IOException {
		Landscape scape = new Landscape(100,100);
		Random gen = new Random();
		int gridScale = 8;
		int r = 0;
		int g = 0;
		int b = 0;
		boolean increase = true;
		HuntTheWumpus game = new HuntTheWumpus( scape, gridScale );
		while(game.state == PlayState.PLAY) {
			// controling the trippy effect
			if (game.getTrippy()) {
				if (increase) {
					r += gen.nextInt(30);
					g += gen.nextInt(10);
					b += gen.nextInt(10);
					if (r >= 255 || g >= 255 || b >= 255) {
						r = gen.nextInt(255);
						g = gen.nextInt(255);
						b = gen.nextInt(255);
						increase = false;
					}
				}
				else {
					r -= 10;
					g -= 5;
					b -= 7;
					if (r <= 0 || g <= 0 || b <= 0) {
						r = gen.nextInt(255);
						g = gen.nextInt(255);
						b = gen.nextInt(255);
						increase = true;
					}
				}
				game.getCanvas().setBackground(new Color(r,g,b));
			}
			game.repaint();
			Thread.sleep(33);
		}
		game.dispose();
	}
}
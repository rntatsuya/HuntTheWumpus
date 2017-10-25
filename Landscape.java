/* 	Tatsuya Yokota
	Landscape.java
	10/19/16 */
	
import java.util.ArrayList;
import java.awt.Graphics;
import java.util.Random;
import java.lang.Math;
import java.util.Iterator;

public class Landscape {
	private int width;
	private int height;
	private Graph g;
	private Hunter hunter1;
	private Hunter hunter2;
	private Wumpus wump;
	private LinkedList<Agent> foreAgents; 
	private LinkedList<Agent> backAgents; 
	private Vertex[][] vertices;
	private ArrayList<Vertex> usedVertices;
	private int numRowVertex = 20;
	private int numColVertex = 20; 
	private Random gen;
	private boolean gameEnded;
	private int numPlayers;
    
	
	public Landscape(int width, int height) {
		this.width = width; 
		this.height = height;
		numPlayers = Integer.parseInt(System.console().readLine("1 or 2 players? [Enter either 1 or 2]:  "));
		restart();
	}
	
	// initializes everything
	public void restart() {
//		System.out.println("Trying");
		gen = new Random();
		g = new Graph();
		backAgents = new LinkedList<Agent>();
		foreAgents = new LinkedList<Agent>();
		usedVertices = new ArrayList();
		Vertex wumpRoom = null;
		gameEnded = false;
		// initiate 2-d array with references to each vertex
		// and add Vertices to background
		vertices = new Vertex[numColVertex][numRowVertex];
		for (int i = 0; i < numColVertex; i++) {
			for (int j = 0; j < numRowVertex; j++) {
				if (i == numColVertex/2 && j == numRowVertex/2) {
					vertices[i][j] = new Vertex(i*5,j*5);
					this.addAgentToBack(vertices[i][j]);
				}
				if (i == numColVertex/4 && j == numRowVertex/4) {
					if (numPlayers == 2) {
						vertices[i][j] = new Vertex(i*5,j*5);
						this.addAgentToBack(vertices[i][j]);
					}
				}
				else if (gen.nextInt() < 0.95) {
					vertices[i][j] = new Vertex(i*5,j*5);
					this.addAgentToBack(vertices[i][j]);
				}
			}
		}
		// make the middle vertex visible
		if (numPlayers == 1) {
			vertices[numColVertex/2][numRowVertex/2].setVisible();
		}
		else if (numPlayers == 2) {
			vertices[numColVertex/2][numRowVertex/2].setVisible();
			vertices[numColVertex/4][numRowVertex/4].setVisible();
		}
		else {
			System.err.println("Error: Choose 1 or 2");
		}
		
		// add the vertices to the Graph and add edges
		for (int i = 0; i < numRowVertex; i++) {
			for (int j = 0; j < numColVertex; j++) {
				if (i % numRowVertex != numRowVertex-1) {
					if (vertices[i][j] != null && vertices[i+1][j] != null) {
			//			System.out.println("This is not supposed to be null:");
			//			System.out.println(vertices[i][j]);
			//			System.out.println(vertices[i+1][j]);
						g.addEdge(vertices[i][j], Vertex.Direction.EAST, vertices[i+1][j]);
						usedVertices.add(vertices[i][j]);
						usedVertices.add(vertices[i+1][j]);
					}
				}
				if (j % numRowVertex != numColVertex-1) {
					if (vertices[i][j] != null && vertices[i][j+1] != null) {
						g.addEdge(vertices[i][j], Vertex.Direction.SOUTH, vertices[i][j+1]);
						usedVertices.add(vertices[i][j]);
						usedVertices.add(vertices[i][j+1]);
					}
				}
			}
		}
		
		// create the Hunter and Wumpus object
		if (numPlayers == 1) {
			hunter1 = new Hunter(0,0,vertices[numRowVertex/2][numColVertex/2]);
		}
		else if (numPlayers ==2) {
			hunter1 = new Hunter(0,0,vertices[numRowVertex/2][numColVertex/2]);
			hunter2 = new Hunter(0,0,vertices[numRowVertex/4][numColVertex/4]);
			hunter2.player2();
		}
		// choose the wumpus room
		wumpRoom = usedVertices.get(gen.nextInt(usedVertices.size()));
		wump = new Wumpus(0,0,wumpRoom);
		// set the costs of each room based on the Wumpus room
		g.shortestPath(wumpRoom);
		
		// have to make sure that the chosen wumpus room is connected to the hunter's room
		if (numPlayers == 1) {
			while (hunter1.currentRoom().getCost() > 2000) {
				wumpRoom = usedVertices.get(gen.nextInt(usedVertices.size()));
				wump.changeRoom(wumpRoom);
				if (wumpRoom != null) {
					g.shortestPath(wumpRoom);
				}
				else {
					hunter1.currentRoom().setCost(20001);
				}
			}
			if (hunter1.currentRoom().getCost()==0) {
				restart();
			}
			this.addAgentToFore(hunter1);
		}
		else if (numPlayers == 2) {
			int count = 0;
			while (hunter1.currentRoom().getCost() > 2000 || hunter2.currentRoom().getCost() > 2000) {
				count++;
				wumpRoom = usedVertices.get(gen.nextInt(usedVertices.size()));
				wump.changeRoom(wumpRoom);
				if (wumpRoom != null) {
					g.shortestPath(wumpRoom);
				}
				else {
					hunter1.currentRoom().setCost(20001);
				}
				if (count > 200) {
					break;
				}
			}
			if (hunter1.currentRoom().getCost()==0 || hunter2.currentRoom().getCost()==0 || hunter1.currentRoom().getCost() > 20000 || hunter2.currentRoom().getCost() > 20000) {
				restart();
			}
		this.addAgentToFore(hunter1);
		this.addAgentToFore(hunter2);					
		}
//		System.out.println("The cost of the current room is: " + hunter1.currentRoom().getCost());
		
		// add Hunter and Wumpus to the foreground
		this.addAgentToFore(wump);
	}
	
	// returns the rows field
	public int getRows() {
		return height;
	}
	
	// returns the cols field
	public int getCols() {
		return width;
	}
	
	// returns LinkedList of foreground Agents
	
	
	// returns LinkedList of background Agents
	public LinkedList<Agent> getBackAgents() {
		return backAgents;
	}
	
	public Hunter getHunter1() {
		return hunter1;
	}
	
	public Hunter getHunter2() {
		return hunter2;
	}
	
	public Wumpus getWumpus() {
		return wump;
	}
	
	public boolean multiplayer() {
		if (numPlayers == 1) {
			return false;
		}
		else if (numPlayers == 2) {
			return true;
		}
		return false;
	}
	
	public void gameEnded() {
		gameEnded = true;
	}
	
	// returns String that gives info about the dimensions of the landscape
	public String toString() {
		return "Landscape has " + width + " columns and " + height + " rows.";
	}
	
	public void addAgentToFore(Agent agent) {
		foreAgents.add(agent);
	}
	
	public void addAgentToBack(Agent agent) {
		backAgents.add(agent);
	}
	
	public void draw( Graphics g, int gridScale ) {
		// loop through backgroundAgents
		Iterator<Agent> backIterator = backAgents.iterator();		
		for(Agent backAgent: backAgents) {
			backAgent.draw(g, gridScale);
		}
		// loop through foregroundAgents
		Iterator<Agent> foreIterator = foreAgents.iterator();
		for(Agent foreAgent: foreAgents) {
			foreAgent.draw(g, gridScale);
		}
	}
	
    public static void main( String[] args ) {
    	Landscape scape = new Landscape(100, 100);
    	LandscapeDisplay display = new LandscapeDisplay(scape, 10);
        display.repaint();
		
    }
}
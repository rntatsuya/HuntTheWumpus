/*	Tatsuya Yokota
	Vertex.java
	11/28/16	*/

// stuff to do
// make a remove method for PQ that can take out a particular node
// 


import java.util.HashMap;
import java.util.Comparator;
import java.util.Collection;
import java.awt.Graphics;
import java.awt.Color;


public class Vertex extends Agent {
	public enum Direction {NORTH, EAST, SOUTH, WEST, NONE};
	//public interface Collection<Vertex>{};
	private HashMap<Direction, Vertex> edges;
	private int cost;
	private boolean marked;
	private String label;
	private boolean visible;
	private int size;

	public static Direction opposite(Direction d) {
		if (d == Direction.NORTH) {
			return Direction.SOUTH;
		}
		else if (d == Direction.SOUTH) {
			return Direction.NORTH;
		}
		else if (d == Direction.EAST) {
			return Direction.WEST;
		}
		else if (d == Direction.WEST) {
			return Direction.EAST;
		}
		else {
			return Direction.NONE;
		}
	}
	
	
	public Vertex(int row, int col) {
		super(row, col); 
		
		this.edges = new HashMap<Direction, Vertex>();
		this.cost = 0;
		this.marked = false;
		this.label = "";
		this.visible = false;
		this.size = 5;
	}
	// a constructor used when I wanted to check the label of the Vertex
	public Vertex(int row, int col, String label) {
		super(row, col);
		
		this.edges = new HashMap<Direction, Vertex>();
		this.cost = 0;
		this.marked = false;
		this.label = label + "\n";
	}
	
	public String getLabel() {
		return label;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void setCost(int newCost) {
		cost = newCost;
	}
	
	public void setVisible() {
		visible = true;
	}
	
	public boolean checkMarked() {
		return marked;
	}
	
	public void setMarked(boolean mark) {
		marked = mark;
	}
	
	public void connect(Vertex other, Direction dir) {
		edges.put(dir, other);
	}
	
	public Vertex getNeighbor(Direction dir) {
//		System.out.println(edges.get(dir));
		return edges.get(dir);
	}
	
	public Collection<Vertex> getNeighbors() {
		return edges.values();
	}
	
	public String toString() {
		int numNeighbors = 0;
		String neighbors = "";
		String cost = "";
		String marked = "";
		for (Vertex neighbor : this.getNeighbors()) {
			if (neighbor != null) {
				numNeighbors++;
			}
		}
		neighbors += 	"Number of Neighbors: "+numNeighbors+"\n";
		cost += 		"Cost: "+this.cost+"\n";
		marked += 		"Marked: "+this.marked;
		return label + neighbors + cost + marked;
	}
	
	public void draw(Graphics g, int gridScale) {
		if (!this.visible) {
			return;
		}
		int xpos = this.getCol()*gridScale; 
        int ypos = this.getRow()*gridScale; 
        int border = 2;
        int half = 5*gridScale / 2;
        int eighth = 5*gridScale / 8;
        int sixteenth = 5*gridScale / 16;
        
        // draw rectangle for the walls of the cave
        if (this.cost <= 2) {
            // wumpus is nearby
            g.setColor(Color.red);
        }
        else {
            // wumpus is not nearby
            g.setColor(Color.blue);
        }
        
        g.fillRect(xpos, ypos, 5*gridScale, 5*gridScale);
        // draw doorways as boxes
        g.setColor(Color.black);
        if (this.edges.containsKey(Direction.NORTH)) {
            g.fillRect(xpos + half - sixteenth, ypos, eighth, eighth + sixteenth);
        }
        if (this.edges.containsKey(Direction.SOUTH)) {
            g.fillRect(xpos + half - sixteenth, ypos + 5*gridScale - (eighth + sixteenth), eighth, eighth + sixteenth);
        }
        if (this.edges.containsKey(Direction.WEST)) {
            g.fillRect(xpos, ypos + half - sixteenth, eighth + sixteenth, eighth);
        }
        if (this.edges.containsKey(Direction.EAST)) {
            g.fillRect(xpos + 5*gridScale - (eighth + sixteenth), ypos + half - sixteenth, eighth + sixteenth, eighth);
        }
	}
	
	public static void main( String[] argv ) {
// 		Vertex v1 = new Vertex("v1");
// 		Vertex v2 = new Vertex("v2");
// 		Vertex v3 = new Vertex("v3");
// 		VertexComparator comp = new VertexComparator();
// //		System.out.println(v1.toString());
// 		
// 		v1.connect(v2, Direction.NORTH);
// 		v2.connect(v1, Direction.SOUTH);
// 		v1.connect(v3, Direction.SOUTH);
// 		v3.connect(v3, Direction.NORTH);
// 		
// 		for (Vertex w : v1.getNeighbors()) {
// 		//	System.out.println();
// 			System.out.println(w.getLabel());
// 			System.out.println(w.toString());
// 		}
// 		
// // 		System.out.println(v1.toString());
// // 		
// // 		System.out.println(opposite(Direction.NORTH));
// // 		System.out.println(opposite(Direction.SOUTH));
// // 		System.out.println(opposite(Direction.WEST));
// // 		System.out.println(opposite(Direction.EAST));
// // 		
//  		v1.setCost(5);
//  		v2.setCost(10);
// // 		
//  		System.out.println(comp.compare(v1,v2));
	}
}

class VertexComparator implements Comparator<Vertex> {
    public VertexComparator() {;}
    
    public int compare( Vertex i1, Vertex i2 ) {
        // returns negative number if i2 comes after i1 lexicographically
        return i1.getCost() - i2.getCost();
    }
}



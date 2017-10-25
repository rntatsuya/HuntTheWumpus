/* 	Tatsuya Yokota
	Agent.java
	10/18/16 */

import java.awt.Graphics;

public class Agent {
	protected int row; // y-value
	protected int col; // x-value
	
	public Agent(int c, int r) {
		row = r;
		col = c;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setRow( int newRow ) {
		row = newRow;
	}
	
	public void setCol( int newCol ) {
		col = newCol;	
	}
	
	public String toString() {
		return "(" + col + ", " + row + ")";
	}
	
// 	public void updateState( Landscape scape ) {
// 	
// 	}
// 	
	public void draw(Graphics g, int scale) {
	
	}
	
	public static void main( String[] args ) {
		Agent a = new Agent(15, 10);
		
		System.out.println(a.getRow());
		System.out.println(a.getCol());
		System.out.println(a);
		
		a.setRow(200);
		a.setCol(300);
		
		System.out.println(a.getRow());
		System.out.println(a.getCol());
		System.out.println(a);
		
	}
}
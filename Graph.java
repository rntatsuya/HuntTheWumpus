/*	Tatsuya Yokota
	Graph.java
	11/28/16	*/

import java.util.ArrayList;
import java.util.Collections;

public class Graph {
	private ArrayList<Vertex> vList;
	
	public Graph() {
		vList = new ArrayList<Vertex>();
	}
	
	public int vertexCount() {
		return vList.size();
	}

	
	public void addEdge(Vertex v1, Vertex.Direction dir, Vertex v2) {
		// check if the vertices are already in list
		if (!vList.contains(v1)) {
			vList.add(v1);
		}
		if (!vList.contains(v2)) {
			vList.add(v2);
		}
		// connect the vertices from both vertices' perspective
		v1.connect(v2, dir);
		v2.connect(v1, Vertex.opposite(dir));
	}
	
	public void shortestPath(Vertex v0) {
		// initialized minimum distance to all infinity
		//ArrayList<Double> dist = new ArrayList<Double>(Collections.nCopies(vList.size(), Double.POSITIVE_INFINITY));
		//dist.set(vList.indexOf(v0), 0.0); // initialize distance of v0 to 0
		
		for (int i = 0; i < vList.size(); i++) {
			vList.get(i).setCost((int)Double.POSITIVE_INFINITY);
			vList.get(i).setMarked(false);
		}
		
		
		//boolean[] visited = new boolean[vList.size()]; // initialized to all false
		//visited[vList.indexOf(v0), 0] = true; // initialize v0 visited to true
		PQHeap<Vertex> pQueue = new PQHeap<Vertex>( new VertexComparator() ); // min heap
		
		v0.setCost(0);
		pQueue.add(v0);
		// put all vertices in priority queue (min heap)
		// for (int i = 0; i < vList.size(); i++) {
// 			pQueue.add(vList.get(i));
// 		}
		// for (int i = 0; i < pQueue.size(); i++) {
// 			System.out.println(pQueue.remove().getCost());
// 		}
		while (!pQueue.empty()) {
			Vertex minDist = pQueue.remove();
//			System.out.println("the min node:"+minDist.getLabel());
			minDist.setMarked(true);
			for (Vertex neighbor: minDist.getNeighbors()) {
				//System.out.println(neighbor.getLabel());
				if (neighbor.checkMarked() == false && (minDist.getCost()+1 < neighbor.getCost())) {
					neighbor.setCost(minDist.getCost() + 1);
					//System.out.println(neighbor.getLabel());
					//System.out.println(neighbor.checkMarked());
					pQueue.add(neighbor);
//					System.out.println("added: "+neighbor.getLabel());
				}
			}
		}
		
		for (int i = 0; i < vList.size(); i++) {
//			System.out.println("Distance to vertex "+(i+1)+": "+vList.get(i).getCost());
		}
	}
	
	public static void main( String[] argv ) {
		Graph g = new Graph();
		Vertex v1 = new Vertex(1,2,"v1");
		Vertex v2 = new Vertex(1,2,"v2");
		Vertex v3 = new Vertex(1,2,"v3");
		Vertex v4 = new Vertex(1,2,"v4");
		
		g.addEdge(v1, Vertex.Direction.NORTH, v2);
		g.addEdge(v2, Vertex.Direction.EAST, v3);
		g.addEdge(v2, Vertex.Direction.WEST, v4);
		
		// for (Vertex v : v3.getNeighbors()) {
// 			System.out.println(v.getLabel());
// 		}
		
// 		System.out.println(v2.toString());
// 		System.out.println(g.vertexCount());
		
// 		System.out.println(v1.toString());
// 		System.out.println(v2.toString());
		
		g.shortestPath(v1);
	}
}
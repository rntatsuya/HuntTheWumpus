/*
	Tatsuya Yokota
	PQHeap.java
	11/06/2016 */

import java.util.ArrayList;
import java.util.Comparator;

// a minimum heap data structure

public class PQHeap<T> {
	private ArrayList<T> heap;
	private Comparator<T> comp;
	private int size;
	private int depth;
	
	public PQHeap( Comparator<T> comparator ) {
		heap = new ArrayList<T>();
		comp = comparator;
		size = 0;
		depth = 0;
	}
	
	public int size() {
		return size;
	}
	
	public int arraySize() {
		return heap.size();
	}
	
	public int depth() {
    	return this.depth;
    }
    
    public boolean empty() {
    	if (this.size == 0) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
	
	public ArrayList<T> getHeapArray() {
		return heap;
	}
	
	public void add(T obj) {
			heap.add(obj);
			size++;
			depth = (int)(Math.floor(Math.log10(size)/Math.log10(2.0) + 1));
			//System.out.println("size: "+size);
			//System.out.println("depth: "+depth);
			this.moveUp();
	}
	
	private void moveUp() {
		int curIndex = size - 1;
		int parIndex = (curIndex-1)/2;
		while (curIndex != 0) {
			T parent = heap.get(parIndex);
			T cur = heap.get(curIndex);
			if (comp.compare(cur, parent) < 0) {
				heap.set(parIndex,cur);
				heap.set(curIndex,parent);
				curIndex = parIndex;
				parIndex = (curIndex - 1)/2;
			}
			else {
				curIndex = 0;
			}
		}
	}
	
	public T remove() {
		
		if (size != 0) {
			T ret = heap.get(0);
			heap.set(0, heap.get(size-1));
			this.moveDown();
			heap.remove(size-1);
			size--;
			depth = (int)(Math.floor(Math.log10(size)/Math.log10(2.0) + 1));
			return ret;
		}
		else {
			return null;
		}
		
	}
	
	private void moveDown() {
		int curIndex = 0;
		int leftChildIndex = 2*curIndex+1;
		int rightChildIndex = 2*curIndex+2;
		T curValueRef;
		int curDepth = 1;
		
		if (size == 1) {
//			System.out.println("first one");
			return;
		}
		
		if (size == 2) {
//			System.out.println("this one");
			if (comp.compare(heap.get(curIndex), heap.get(leftChildIndex)) > 0) {
				curValueRef = heap.get(curIndex);
				heap.set(curIndex, heap.get(leftChildIndex));
				heap.set(leftChildIndex, curValueRef);
				return;
			}
		}
		
		while (curDepth != this.depth) {
			curDepth++;
			if (leftChildIndex >= size || rightChildIndex >= size) {
				break;
			}
			if (comp.compare(heap.get(leftChildIndex), heap.get(rightChildIndex)) < 0) {
				if (comp.compare(heap.get(curIndex), heap.get(leftChildIndex)) > 0) {			
					curValueRef = heap.get(curIndex);
					heap.set(curIndex, heap.get(leftChildIndex));
					heap.set(leftChildIndex, curValueRef);
					if (curDepth != this.depth) {
						curIndex = leftChildIndex;
						leftChildIndex = 2*curIndex+1;
						rightChildIndex = 2*curIndex+2;
					}
				}
				else {
					break;
				}
			}
			else {
				if (comp.compare(heap.get(curIndex), heap.get(rightChildIndex)) > 0) {
					curValueRef = heap.get(curIndex);
					heap.set(curIndex, heap.get(rightChildIndex));
					heap.set(rightChildIndex, curValueRef);
					if (curDepth != this.depth) {
						curIndex = rightChildIndex;
						leftChildIndex = 2*curIndex+1;
						rightChildIndex = 2*curIndex+2;
					}
				}
				else {
					break;
				}
			}
		}
	}
	
	
	public static void main( String[] args ) {
		
// 		PQHeap<Integer> heap = new PQHeap<Integer>(new TestIntComparator());
// 		
// 		heap.add(9);
// 		heap.add(10);
// 		heap.add(8);
// 		heap.add(2);
// 		heap.add(100);
// 		heap.add(4);
// 		heap.add(7);
// 		
// 		
// 		for (Integer num : heap.getHeapArray()) {	
// 			System.out.println(num);
// 		
// 		}
// 		
// 		System.out.println("-----------------");
// 		
// 		System.out.println(heap.remove()); //  2
// 		System.out.println(heap.remove()); //  4
// 		System.out.println(heap.remove()); //  7
// 		System.out.println(heap.remove()); //  8
// 		System.out.println(heap.remove()); //  9
// 		System.out.println(heap.remove()); //  10
// 		System.out.println(heap.remove()); //  100

		PQHeap<Vertex> heap = new PQHeap<Vertex>(new VertexComparator());
		
		Vertex v1 = new Vertex(1,1,"v1");
		Vertex v2 = new Vertex(1,1,"v2");
		Vertex v3 = new Vertex(1,1,"v3");
		Vertex v4 = new Vertex(1,1,"v4");
		Vertex v5 = new Vertex(1,1,"v5");
		v1.setCost(0);
		v2.setCost(1);
		v3.setCost(2);
		v4.setCost(6);
		v5.setCost(5);
		
		System.out.println("ArrayList size: "+ heap.arraySize()); // 0
		heap.add(v1);
		System.out.println("ArrayList size: "+ heap.arraySize()); // 1
		System.out.println(heap.remove().getLabel()); //  v1
		heap.add(v2);
		heap.add(v3);
		System.out.println("ArrayList size: "+ heap.arraySize()); // 2
		System.out.println(heap.remove().getLabel()); // v2
		heap.add(v4);
		heap.add(v5);
		System.out.println("ArrayList size: "+ heap.arraySize()); // 3
		System.out.println(heap.remove().getLabel()); // v3
// 		System.out.println(heap.remove().getLabel()); //  v2
// 		System.out.println(heap.remove().getLabel()); //  v3
// 		heap.add(v4);
// 		heap.add(v5);
		
// 		for (Vertex v : heap.getHeapArray()) {	
// 			System.out.println(v.getLabel());
// 		}
		
		System.out.println("-----------------");
		
		// for (int i = 0; i < heap.size(); i++) {
// 			System.out.println(heap.remove().getLabel());
// 		}
		
// 		System.out.println(heap.remove().getLabel()); //  v1
// 		System.out.println(heap.remove().getLabel()); //  v2
// 		System.out.println(heap.remove().getLabel()); //  v3
// 		System.out.println(heap.remove().getLabel()); //  v5
// 		System.out.println(heap.remove().getLabel()); //  v4
	}
}

// for testing purposes!!!
class TestIntComparator implements Comparator<Integer> {
    public TestIntComparator() {;}
    
    public int compare(Integer o1, Integer o2) {
        return o1-o2;
    }   
}
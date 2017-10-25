/*
	Tatsuya Yokota
	PQHeap.java
	11/06/2016 */

import java.util.ArrayList;
import java.util.Comparator;
// a max heap data structure
public class PQHeapMax<T> {
	private ArrayList<T> heap;
	private Comparator<T> comp;
	private int size;
	private int depth;
	
	public PQHeapMax( Comparator<T> comparator ) {
		heap = new ArrayList<T>();
		comp = comparator;
		size = 0;
		depth = 0;
	}
	
	public int size() {
		return size;
	}
	
	public int depth() {
    	return this.depth;
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
			if (comp.compare(cur, parent) > 0) {
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
			size--;
			depth = (int)(Math.floor(Math.log10(size)/Math.log10(2.0) + 1));
			this.moveDown();
		//	System.out.println(size);
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
			return;
		}
		
		if (size == 2) {
			if (comp.compare(heap.get(curIndex), heap.get(leftChildIndex)) < 0) {
				curValueRef = heap.get(curIndex);
				heap.set(curIndex, heap.get(leftChildIndex));
				heap.set(leftChildIndex, curValueRef);
				return;
			}
		}
		
		while (curDepth != this.depth - 1) {
			curDepth++;
			if (leftChildIndex > size || rightChildIndex > size) {
				break;
			}
			if (comp.compare(heap.get(leftChildIndex), heap.get(rightChildIndex)) > 0) {
				if (comp.compare(heap.get(curIndex), heap.get(leftChildIndex)) < 0) {			
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
				if (comp.compare(heap.get(curIndex), heap.get(rightChildIndex)) < 0) {
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
		
		// PQHeap<Integer> heap = new PQHeap<Integer>(new TestIntComparator());
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
// 		System.out.println(heap.remove()); // 100
// 		System.out.println(heap.remove()); // 10
// 		System.out.println(heap.remove()); // 9
// 		System.out.println(heap.remove()); // 8
// 		System.out.println(heap.remove()); // 7
// 		System.out.println(heap.remove()); // 4
// 		System.out.println(heap.remove()); // 2
	}
}
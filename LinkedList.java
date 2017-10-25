/* 	Tatsuya Yokota
	LinkedList.java
	11/01/16 */

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

public class LinkedList<T> implements Iterable<T> {
	private Node<T> head;
	private Node<T> tail;
	private int size;
	
	public LinkedList() {
		head = null;
		tail = null;
		size = 0;
	}
	
	public void clear() {
		head = null;
		tail = null;
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public T getHeadContent() {
		return head.getThing();
	}
	
	public void add(T item) { // adds item to head
		Node<T> n = new Node<T>(item);
		if(head == null) {
			tail = n;
			n.setPrev(null);
			n.setNext(head);
			head = n;
			size++;
		}
		else {
			n.setPrev(null);
			n.setNext(head);
			head.setPrev(n);
			head = n;
			size++;
		}
	}
	
	public boolean addToTail(T item) { // adds item to tail
		Node<T> n = new Node<T>(item);
		if(head == null) {
			tail = n;
			n.setNext(null);
			n.setPrev(head);
			head = n;
			size++;
			return true;
		}
		else if (size == 1) {
			tail = n;
			n.setPrev(head);
			n.setNext(null);
			n.getPrev().setNext(n);
			size++;
			return true;
		}
		else {
			tail.setNext(n);
			n.setPrev(tail);
			n.setNext(null);
			tail = n;
			size++;
			return true;
		}
	}
	
	public T removeFromHead() { // remove item from head
		Node<T> n = head;

		// empty case
		if (head == null) {
			return null;
		}
		// one node case
		else if (size == 1) {
			head = null;
			tail = null;
			size--;
			return n.getThing();
		}
		// rest
		else {
			head.getNext().setPrev(null);
			head = head.getNext();
			size--;
			return n.getThing();
		}
	}
	
	public Node<T> getHead() {
		return head;
	}

// 	Don't need this remove method for this project
	public T remove(T thing) { // remove item from tail
		Node<T> ptr = head;
		Node<T> n = null;
		
		// if list size is one
		if (size == 1) {
			head = null;
			tail = null;
		//	ptr.getPrev().setNext(ptr.getNext());
			size--;
			return ptr.getThing();
		}
		// if the node we need to remove is at the head
		else if (head.getThing() == thing) {
			head = ptr.getNext();
			ptr.getNext().setPrev(ptr.getPrev());
			size--;
			return ptr.getThing();
		}
		// if the node we need to remove is at the tail
		else if (tail.getThing() == thing) {
			ptr = tail;
			tail = ptr.getPrev();
			ptr.getPrev().setNext(ptr.getNext());
			size--;
			return ptr.getThing();
		}
		else {
			for (int i = 0; i < size; i++) {
				ptr = ptr.getNext();
				if (ptr.getThing() == thing) {
					ptr.getNext().setPrev(ptr.getPrev());
					ptr.getPrev().setNext(ptr.getNext());
					size--;
					return ptr.getThing();
				}
			}
		}
		return null;
	}
	
	// Return a new LLIterator pointing to the head of the list
    public Iterator<T> iterator() {
        return new LLIterator( this.head );
    }
    
    // Return a new LLBackwardIterator pointing to the head of the list
    public Iterator<T> backward_iterator() {
        return new LLBackwardIterator( tail );
    }
    
    
    public ArrayList<T> toArrayList() {
    	ArrayList<T> storage = new ArrayList<T>();
    	for (T item: this) {
    		storage.add(item);
    	}
    	return storage;
    }
    
    public ArrayList<T> toShuffledList() {
    	ArrayList<T> storage = this.toArrayList();
    	Collections.shuffle(storage);
    	
    	return storage;
    }
	
	private class Node<T> {
		private Node<T> prev;
		private Node<T> next;
		private T bucket;
		
		public Node(T item) {
			next = null;
			prev = null;
			bucket = item;
		}
		
		public T getThing() {
			return bucket;
		}
		
		public void setNext(Node<T> n) {
			next = n;
		}
		
		public void setPrev(Node<T> n) {
			prev = n;
		}
		
		public Node<T> getNext() {
			return next;
		}
		
		public Node<T> getPrev() {
			return prev;
		}
	}
	
	private class LLIterator implements Iterator<T> {
		private Node<T> iteratedNode;
		
		public LLIterator(Node<T> head) {
			iteratedNode = head;
		}
		
		public boolean hasNext() {
			return iteratedNode != null;
		}
		
		public T next() {
			if(iteratedNode == null)
				return null;
			T ret = iteratedNode.getThing();
			iteratedNode = iteratedNode.getNext();
			return ret;
		}
		
		public void remove() {
		
		}
	}
	
	private class LLBackwardIterator implements Iterator<T> {
		private Node<T> iteratedNode;
		
		public LLBackwardIterator(Node<T> tail) {
			iteratedNode = tail;
		}
		
		public boolean hasNext() {
			return iteratedNode != null;
		}
		
		public T next() {
			if(iteratedNode == null)
				return null;
			T ret = iteratedNode.getThing();
			iteratedNode = iteratedNode.getPrev();
			return ret;
		}
	}
	
	public static void main( String[] args ) {
		// LinkedList<String> list = new LinkedList<String>();
// 		
// 		System.out.println(list.size());
// 		list.add( "Hi" );
// 		System.out.println(list.size());
		
		LinkedList<Integer> llist = new LinkedList<Integer>();
		
		llist.addToTail(5);
		llist.addToTail(2);
		llist.addToTail(4);
		llist.addToTail(1);
		for (Integer item: llist) {
			System.out.printf("thing %d\n", item);
		}
		System.out.println(llist.getHeadContent());
		
		llist.removeFromHead();
		System.out.println(llist.getHeadContent());
		for (Integer item: llist) {
			System.out.printf("thing %d\n", item);
		}
		// llist.remove(5);
// 		llist.remove(4);
// 		llist.remove(2);
// 		llist.remove(1);
// 		for (Integer item: llist) {
// 			System.out.printf("thing %d\n", item);
// 		}
		
// 		llist.add(5);
// 		llist.add(2);
// 		llist.add(4);
// 		llist.add(1);
// 		System.out.println( "iterating backward" );
//         Iterator bi = llist.backward_iterator();
//         while (bi.hasNext()) {
//             System.out.println( bi.next() );
//         }
        
        
        
// 		llist.add(10);
// 		//System.out.println(llist.head.getThing());
// 		llist.add(20);
// 		//System.out.println(llist.head.getThing());
// 		
// 		System.out.printf("\nAfter setup %d\n", llist.size());
// 		for(Integer item: llist) {
// 			System.out.printf("thing %d\n", item);
// 		}
// 		
// 		llist.clear();
// 		
// 		System.out.printf("\nAfter clearing %d\n", llist.size());
// 		for (Integer item: llist) {
// 			System.out.printf("thing %d\n", item);
// 		}
// 		
// 		for (int i=0; i<20; i+=2) {
// 			llist.add(i);
// 		}
// 		
// 		System.out.printf("\nAfter setting %d\n", llist.size());
// 		for (Integer item: llist) {
// 			System.out.printf("thing %d\n", item);
// 		}
// 		
// 		ArrayList<Integer> alist = llist.toArrayList();
// 		System.out.printf("\nAfter copying %d\n", alist.size());
// 		for(Integer item: alist) {
// 			System.out.printf("thing %d\n", item);
// 		}						
// 	
// 		alist = llist.toShuffledList();	
// 		System.out.printf("\nAfter copying %d\n", alist.size());
// 		for(Integer item: alist) {
// 			System.out.printf("thing %d\n", item);
// 		}
	}
}
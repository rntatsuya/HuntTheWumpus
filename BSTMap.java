/*
	Tatsuya Yokota
	BSTMap.java
	10/31/2016 */

import java.util.Comparator;
import java.util.ArrayList;

public class BSTMap<K,V> implements MyMap<K,V> {
	private TreeNode<K,V> root;
	private Comparator<K> comp;
	private ArrayList<KeyValuePair<K,V>> list;
	private int size;
	private int depth;
	
	public BSTMap( Comparator<K> comp ) {
		this.comp = comp;
		this.root = null;
		this.size = 0;
		this.depth = 1;
		this.list = new ArrayList<KeyValuePair<K,V>>();
	}
	
	// adds or updates a key-value pair
	public void put( K key, V value ) {
        // if (!this.containsKey(key)) {
//         	this.size++;
//         }
        if (root == null) {
        	this.root = new TreeNode<K,V>(key, value, null, null);
        	size++;
        }
        else {
        	if (this.containsKey(key)) {
        		this.root.put(key, value, this.comp);
        	}
        	else {
        		this.root.put(key, value, this.comp);
        		size++;
        	}
        }
        this.depth = (int)(Math.floor(Math.log10(size)/Math.log10(2.0) + 1));
    }
    
    // Returns true if the map contains a key-value pair with the given key
    public boolean containsKey( K key ) {
    	if (root == null) {
    		return false;
    	}
    	return root.contains( key, this.comp );
    }
    
    // Returns the value associated with the given key.
    // If that key is not in the map, then it returns null.
	public V get( K key ) {
		if (root == null) {
			return null;
		}
    	return root.get( key, this.comp );
    }
     
    // Returns an array list of all the keys in the map.
    public ArrayList<K> getKeys() {
    	return new ArrayList<K>();
    }
    
    // Returns the number of key-value pairs in the map.
    public int size() {
    	return this.size;
    }
    
    public int depth() {
    	return this.depth;
    }
    
    // return list of pairs with keys in an order that will be useful
    //       to reconstruct the map. by level
    public ArrayList<KeyValuePair<K,V>> getPairs() {
    	return root.getPairs(new ArrayList<KeyValuePair<K,V>>());
    }
    
    // helper method for the printInOrder public method
    private void inOrder(TreeNode<K,V> cur) {
		if( cur == null ) {
			return;
		}

		inOrder( cur.getLeft() );
		System.out.println( cur.getPair().toString() );
		inOrder( cur.getRight() );
    }
    
    
    // prints the tree in-order, left, root, right node
    public void printInOrder() {
    	this.inOrder(this.root);
    }
    
    // returns a String with the key and content of each pair separated 
    // by a new line
    public String contentInOrder() {
    	String ret = "";
    	long start = System.currentTimeMillis();
    	for (KeyValuePair<K,V> pair: this.getPairs()) {
	    	System.out.println(start - System.currentTimeMillis());
	    	ret += pair.toString() + "\n";
    	}
    	System.out.println("------------------------------------");
    	return ret;
    }
    
    public static void main( String[] args ) {
        System.out.println( "test code" );
        BSTMap<String,Integer> map = new BSTMap<String,Integer>(new AscendingStringComparator<String>());
        String yo = "yo";
        String haha = "haha";
        map.put( yo, 1 );
        map.put( haha, 1 );
        map.put( yo, map.get(yo)+1 );
        System.out.println(map.size());
//        System.out.println();
        System.out.println(map.get("yo"));
        System.out.println(map.containsKey("yo"));
        System.out.println(map.getPairs());
//         map.put( "A", 1);
//         map.put( "D", 2 );
//         map.put( "C", 2 );
//         map.printInOrder();
//         System.out.println();
//         System.out.println( "Has A: " + map.containsKey( "A" ) );
//         System.out.println( "Has G: " + map.containsKey( "G" ) );
//         map.put( "D", 3 );
//         System.out.println( "D now has value " + map.get( "D" ) );
//         System.out.println( "The tree has " + map.size() + " elements" );
//         System.out.println( "The tree has " + map.depth() + " levels" );
//         ArrayList<KeyValuePair<String,Integer>> pairs  = map.getPairs();
//         System.out.println( "In useful order: " );
//         System.out.println( pairs );
    }        
}

//---------------------------------------

class TreeNode<Key,Value> {
    private KeyValuePair<Key,Value> pair;
    private TreeNode<Key,Value> left;
    private TreeNode<Key,Value> right;
    

    public TreeNode( Key this_key, Value this_val, TreeNode<Key,Value> l, TreeNode<Key,Value> r ) {
    	this.pair = new KeyValuePair<Key,Value>( this_key, this_val );
    	this.left = l;
    	this.right = r;
    }
    
  // Methods to support insert, contains, printing, getPairs, etc.
	
	public TreeNode<Key, Value> getLeft() {
		return this.left;
	}

	public TreeNode<Key, Value> getRight() {
		return this.right;
	}
	
	public KeyValuePair<Key, Value> getPair() {
		return this.pair;
	}

	
	public void put( Key this_key, Value this_value, Comparator<Key> comp ) {
		if (comp.compare( this.pair.getKey(), this_key ) == 0) {
			this.pair.setValue(this_value);
			return;
		}
		if (comp.compare( this.pair.getKey(), this_key ) > 0) { // when this.pair.key >= this_key
			if (this.left == null) {
				this.left = new TreeNode<Key,Value>(this_key, this_value, null, null);
				return;
			}
			else {
				this.left.put( this_key, this_value, comp );
			}
		}
		else {
			if (this.right == null) {
				this.right = new TreeNode<Key,Value>(this_key, this_value, null, null);
				return;
			}
			else {
				this.getRight().put( this_key, this_value, comp );
			}
		}
	}
	
	
	public Value get( Key k, Comparator<Key> comp ) {
    	if (comp.compare( this.pair.getKey(), k ) == 0) {
    		//System.out.println(this.pair.getValue());
    		return this.pair.getValue();
    	}
    	if (comp.compare(this.pair.getKey(), k) > 0) {
			if (this.left == null) {
				return null;
			}
			else {
				return this.left.get(k, comp);
			}
		}
		else {
			if (this.right == null) {
				return null;
			}
			else {
				return this.right.get(k, comp);
			}
		}
    }
	
	public ArrayList<KeyValuePair<Key,Value>> getPairs( ArrayList<KeyValuePair<Key,Value>> list ) {
		//	System.out.println("getting pairs");
			if( this == null ) {
					return list;
			}
			list.add( this.pair );
			if (this.left != null) {
				this.left.getPairs( list );
			}
			if (this.right != null) {
				this.right.getPairs( list );
			}
			return list;
	}
	
	public boolean contains( Key k, Comparator<Key> comp ) {
		if (comp.compare( this.pair.getKey(), k ) == 0) {
    		return true;
    	}
		if (comp.compare(this.pair.getKey(), k) > 0) {
			if (this.left != null) {
				if (comp.compare( this.left.pair.getKey(), k ) == 0) {
					return true;
				}
				else {
					return this.left.contains(k, comp);
				}
			}
		}
		else {
			if (this.right != null) {
				if (comp.compare( this.right.pair.getKey(), k ) == 0) {
					return true;
				}
				else {
					return this.right.contains(k, comp);
				}
			}
		}
		return false;
	}
	
	
	
} // end class TreeNode

//---------------------------------------

// For sorting by Key. It requires the user to supply a comparator for just the 
// Key type
class AscendingStringComparator<K> implements Comparator<String> {
    public int compare( String i1, String i2 ) {
        // returns negative number if i2 comes after i1 lexicographically
        return i1.compareTo(i2);
    }
}
//---------------------------------------



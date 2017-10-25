/**
 * Interface for a data structure that maps a key to a value, like a Python dictionary would.
 * @author srtaylor
 */
import java.util.ArrayList;

public interface MyMap<K,V> {

    public void put( K key, V value ); // adds or updates a key-value pair
    
    // Returns true if the map contains a key-value pair with the given key
    public boolean containsKey( K key );
    
    // Returns the value associated with the given key.
    // If that key is not in the map, then it returns null.
    public V get( K key );
    
    // Returns an array list of all the keys in the map.
    public ArrayList<K> getKeys();
    
    // Returns the number of key-value pairs in the map.
    public int size();
        
    // return list of pairs with keys in an order that will be useful
    //       to reconstruct the map.
    public ArrayList<KeyValuePair<K,V>> getPairs(); 
    
}
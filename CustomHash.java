
import java.util.Hashtable;
import java.util.ArrayList;

// solves warning class "should have a serialVersionUID" and "unchecked cast"
@SuppressWarnings({"serial", "unchecked"}) 
public class CustomHash<K,V> extends Hashtable<K,V> {
	
	private ArrayList<KeyValuePair<K,V>> pairs;
	
	public CustomHash() {
		super();
		pairs = new ArrayList<KeyValuePair<K,V>>();
	}
	
	// adds all the KeyValuePairs into an ArrayList and returns it
	// not a fast way of getting all the KeyValuePair data though
	public ArrayList<KeyValuePair<K,V>> getPairs() {
	//	K[] keyList = this.keySet().toArray();
		for (int i = 0; i < this.keySet().toArray().length; i++) {
			//System.out.println(this.get(this.keySet().toArray()[i]));
			pairs.add(new KeyValuePair<K,V>((K)this.keySet().toArray()[i], (V)this.get(this.keySet().toArray()[i])));
		}
		
		return pairs;
	}
	
	public static void main( String[] argv ) {
		CustomHash<String, Integer> hash = new CustomHash<String, Integer>();
		
		hash.put("Hi", 9);
		hash.put("Yo", 6);
		hash.put("Mo", 3);
		hash.put("So", 2);
		
		System.out.println(hash.keySet().toArray().length);
		
		for (int i = 0; i < hash.keySet().toArray().length; i++) {
			System.out.println(hash.get(hash.keySet().toArray()[i]));
		}
		
		// KeyValuePair<String, Integer> pair = new KeyValuePair<String, Integer>((String)hash.keySet().toArray()[0], hash.get(hash.keySet().toArray()[0]));
// 		
// 		System.out.println(pair.toString());
		
		
		for (KeyValuePair<String,Integer> pair : hash.getPairs()) {
			System.out.println(pair.toString());
		}
	}
}
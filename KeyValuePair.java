/*
	Tatsuya Yokota
	KeyValuePair.java
	10/31/2016 */
	
public class KeyValuePair<Key, Value> {
	private Key key;
	private Value value;
	
	public KeyValuePair( Key k, Value v) {
		this.key = k;
		this.value = v;
	}
	
	public Key getKey() {
		return this.key;
	}
	
	public Value getValue() {
		return this.value;
	}
	
	public void setValue( Value v ) {
		this.value = v;
	}
	
	public String toString() {
		return this.key + " " + this.value;
	}
	
	public static void main( String[] args ) {
		KeyValuePair<String, Integer> pair = new KeyValuePair<String, Integer>( "Hi", 2);
		
		System.out.println(pair.getKey());
		System.out.println(pair.getValue());
		System.out.println(pair);			
		pair.setValue( 5 );
		System.out.println(pair);
	}
}
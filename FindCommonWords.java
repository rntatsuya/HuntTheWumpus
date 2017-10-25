/*
	Tatsuya Yokota
	FindCommonWords.java
	11/06/2016 */

import java.util.Comparator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FindCommonWords {
	private PQHeapMax<KeyValuePair<String, Integer>> heap;
	private WordCounterHashTable wordCounterHash;
	private WordCounter wordCounter;
	private Integer method = Integer.parseInt(System.console().readLine("Use BSTMap or HashTable? Enter 0 for BSTMap or 1 for HashTable: "));
	
	public FindCommonWords() {
		 heap = new PQHeapMax<KeyValuePair<String, Integer>>( new KVComparator() );
		 wordCounterHash = new WordCounterHashTable();
		 wordCounter = new WordCounter();
	}
	
	// reads the word count file and loads it into a map
	// can use which data structure to use (BSTMap or hashtable)
	public void readWordCountFile( String filename ) throws FileNotFoundException, IOException {
		// uses BSTMap
		if (method == 0) {
			wordCounter.readWordCountFile(filename);
		}
		// uses hashtable
		else if (method == 1) {
			wordCounterHash.readWordCountFile(filename);
		}	
	}
	
	// loads the mapped data into a PQHeap
	public void loadToHeap() {
		// uses BSTMap
		if (method == 0) {
			for (KeyValuePair<String, Integer> pair : wordCounter.getInitMap().getPairs()) {
				heap.add(pair);
			}
		}
		// uses hashtable
		else if (method == 1) {
			// Get a set of the entries
		 	Set set = wordCounterHash.getInitMap().entrySet();
  
			// Get an iterator
			Iterator i = set.iterator();
 			// enable access to the key and value
			// and add data to heap 
			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				heap.add(new KeyValuePair<String, Integer>((String)me.getKey(), (Integer)me.getValue()));
			}
			
			// failed method of using a for each loop through an ArrayList with all the KeyValuePairs
			// takes too long for large data set
			// for (KeyValuePair<String, Integer> pair : wordCounterHash.getInitMap().getPairs()) {
// 				System.out.println("hihohohoh");
// 				heap.add(pair);
// 			}
		}
	}
	
	public KeyValuePair<String, Integer> removePairFromHeap() {
		return heap.remove();
	}
	
	
// 	public WordCounterHashTable getWordCounter() {
// 		return wordCounter;
// 	}
	
// 	public BSTMap<String,Integer> getMap() {
// 		return wordCounter.getReconstructedMap();
// 	}
	
	
	public static void main( String[] argv ) throws FileNotFoundException, IOException {
		FindCommonWords pqFinder = new FindCommonWords();
		
		// test with test file		
// 		pqFinder.readWordCountFile("counts_short.txt");
// 		pqFinder.loadToHeap();
// 		System.out.println(pqFinder.removePairFromHeap().toString());
// 		System.out.println(pqFinder.removePairFromHeap().toString());
// 		System.out.println(pqFinder.removePairFromHeap().toString());
// 		System.out.println(pqFinder.removePairFromHeap().toString());
// 		System.out.println(pqFinder.removePairFromHeap().toString());
// 		System.out.println(pqFinder.removePairFromHeap().toString());
// 		System.out.println(pqFinder.removePairFromHeap().toString());
// 		System.out.println(pqFinder.removePairFromHeap().toString());
// 		System.out.println(pqFinder.removePairFromHeap());

		// find 10 most common words
		// make command line argument the filename you want to load
		long start = System.currentTimeMillis();
		pqFinder.readWordCountFile(argv[0]);
		long totalTime = System.currentTimeMillis() - start;
//		System.out.println("Reading time: " + totalTime);
		
		start = System.currentTimeMillis();
		pqFinder.loadToHeap();
		totalTime = System.currentTimeMillis() - start;
//		System.out.println("Heap loading time: " + totalTime);
		
		for (int i = 0; i < 10; i++) {
			System.out.println(pqFinder.removePairFromHeap().toString());
		}
	}

}

// a KeyValuePair comparator that compares a key's value to another's
class KVComparator implements Comparator<KeyValuePair<String,Integer>> {
    public int compare( KeyValuePair<String,Integer> i1, KeyValuePair<String,Integer> i2 ) {
        // returns negative number if i2 comes after i1 lexicographically
        int diff = i1.getValue() - i2.getValue();
        if (diff == 0)
            return 0;
        if (diff > 0)
            return 1;
        else
            return -1;
    }
}
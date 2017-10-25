/*
	Tatsuya Yokota
	WordCounter.java
	11/06/2016 
referenced for method of writing file in Java using BufferReader
http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/	*/

import java.util.Comparator;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Console;


public class WordCounter {
	private BSTMap<String,Integer> initMap;
	private BSTMap<String,Integer> reconstructedMap;
	private ArrayList<String> commonWords;
	private int totalWordCount;
	private int removeCommonWords = 0;
	private String trendWords;
	
	public WordCounter() {
		initMap = new BSTMap<String, Integer>(new AscendingStringComparator<String>());
		reconstructedMap = new BSTMap<String, Integer>(new AscendingStringComparator<String>());
		totalWordCount = 0;
		commonWords = new ArrayList<String>();
	//	trendWords = System.console().readLine("Enter trend words you would like to search the count for (divide each word with a space): ");
	}
	
	public void clear() {
		initMap = new BSTMap<String, Integer>(new AscendingStringComparator<String>());
		reconstructedMap = new BSTMap<String, Integer>(new AscendingStringComparator<String>());
		totalWordCount = 0;
		commonWords = new ArrayList<String>();
	}
	
	// loads in words from a text file and puts them into the intiMap BSTMap object
	public void loadFromOriginalWordsFile( String filename ) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parse = line.split("[^a-zA-Z'0-9]");
				for (int i = 0; i < parse.length; i++) {
            		String word = parse[i].trim().toLowerCase();
					if (!word.isEmpty()) {
						initMap.put(word, this.getInitMapKeyCount(word)+1);
						totalWordCount++;
					}
            	}
			}
		}
	}
	
	// used to load in common words stored in the commonWords.txt file and puts them in 
	// the commonWords arraylist
	public void loadCommonWordsFile() throws FileNotFoundException, IOException {
		// creating array with common words
		try (BufferedReader br = new BufferedReader(new FileReader("commonWords.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parse = line.split("[^a-zA-Z]");
				for (int i = 0; i < parse.length; i++) {
            		String word = parse[i].trim().toLowerCase();
					if (!word.isEmpty()) {
						commonWords.add(word);
					}
            	}
			}
		}
	}
	
	// allows the user to dynamically set the trend words and print its count 
	// through user input
	public void showTrendWords() {
		String[] parse = trendWords.split("[^a-zA-Z]");
			for (int i = 0; i < parse.length; i++) {
				String trendWord = parse[i].trim().toLowerCase();
				if (!trendWord.isEmpty()) {
					System.out.println(trendWord + ":  " + this.getInitMapKeyCount(trendWord));
				}
			}
	}
	
	public int getTotalWordCount() {
		return totalWordCount;
	}
	
	public int getUniqueWordCount() {
		return this.initMap.size();
	}
	
	// gets the frequency of the word from the initMap BSTMap object
	public int getInitMapKeyCount( String word ) {
		Integer count = initMap.get(word.toLowerCase());
		if (count == null) {
			return 0;
		} 
		return count;
	}
	
	// gets the frequency of the word from the reconstructedMap BSTMap object
	public int getReconstructedMapKeyCount( String word ) {
		Integer count = reconstructedMap.get(word);
		if (count == null) {
			return 0;
		} 
		return count;
	}
	
	// method to return arraylist storing the common words
	public ArrayList<String> getCommonWords() {
		return commonWords;
	}
	
	// returns the ratio of the number of times that word appeared 
	public double getFrequency( String word ) {
		if (initMap.get(word) == null) {
			return 0.0;
		}
		
		return (double)(initMap.get(word))/(double)(totalWordCount);
	}
	
	public BSTMap<String,Integer> getInitMap() {
		return this.initMap;
	}
	
	public BSTMap<String,Integer> getReconstructedMap() {
		return this.reconstructedMap;
	}
	
// 	public String toString() {
// 		String ret = "";
// 		for (KeyValuePair<K,V> pair: map.getPairs()) {
// 			String indent = "     ";
// 			for (int i = 0; i < map.depth() - i; i++) {
// 				indent += indent;
// 			}
// 			
// 			ret += 
// 		}
// 	}
	
	// that writes out the word and its count into a text file
	// removes common words depending on whether the user set the removeCommonWords field
	// to a 0 or 1 in the Terminal (user is prompted)
	public void writeWordCountFile( String filename ) {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
		
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//bw.write("total_word_count " + this.totalWordCount + "\n");
			//System.out.println(removeCommonWords);
			if (removeCommonWords == 0) {
				for (KeyValuePair<String,Integer> pair : this.getInitMap().getPairs()) {
					bw.write(pair.toString() + "\n");
				}
			}
			else if (removeCommonWords == 1) {
				this.loadCommonWordsFile();
				for (KeyValuePair<String,Integer> pair : this.getInitMap().getPairs()) {
					if (commonWords.contains(pair.getKey())) {
						continue;
					}
					else {
						bw.write(pair.toString() + "\n");
					}
					
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int convertStringInt(String count) {
		return Integer.valueOf( count );
	}
	
	// reads and reconstructs a map
	public void readWordCountFile( String filename ) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parse = line.split("[^a-zA-Z'0-9_]");
				// for (int j = 0; j < parse.length; j++) {
// 					System.out.println(parse[j]);
// 				}
				for (int i = 0; i < parse.length-1; i++) {
            		//System.out.println(i);
            		String word = parse[i].trim().toLowerCase();
					if (!word.isEmpty()) {
						//System.out.println(parse[i].trim().toLowerCase() + "    word");
						//System.out.println(parse[i+1].trim().toLowerCase());
						initMap.put(parse[i].trim().toLowerCase(), this.convertStringInt(parse[i+1]));
						totalWordCount++;
					}
            	}
			}
		}
	}
	
	public static void main( String[] args ) throws FileNotFoundException, IOException {
		WordCounter counter = new WordCounter();
		
		long start = System.currentTimeMillis();
		counter.loadFromOriginalWordsFile(args[0]);
		long totalTime = System.currentTimeMillis() - start;
		System.out.println("Loading time: " + totalTime);
		
// 		start = System.currentTimeMillis();
// 		counter.writeWordCountFile(args[1]);
// 		totalTime = System.currentTimeMillis() - start;
// 		System.out.println("Writing time: " + totalTime);
// 		System.out.println("Number of Unique Words: " + counter.getUniqueWordCount());
// 		System.out.println("Total number of words: " + counter.getTotalWordCount());
		
		if (counter.getFrequency("good") != 0) {
			System.out.println("hi");
		}
// 		
// 		// dynamically getting frequency count for trending words with user input
// 		counter.showTrendWords();
		
 		//manually getting frequency count for trending words
// 		System.out.println("Frequency of Trending Words");
// 		System.out.println("Gangnam:   " + counter.getInitMapKeyCount("Gangnam"));
// 		System.out.println("Nishikori:   " + counter.getInitMapKeyCount("Nishikori"));
// 		System.out.println("Earthquake:   " + counter.getInitMapKeyCount("Earthquake"));
// 		System.out.println("Nuclear:   " + counter.getInitMapKeyCount("Nuclear"));
// 		System.out.println("President:   " + counter.getInitMapKeyCount("President"));
// 		System.out.println("Obama:   " + counter.getInitMapKeyCount("Obama"));

		// checking output of most common words txt file		
// 		counter.loadCommonWordsFile();
// 		for (String word : counter.getCommonWords()) {
// 			System.out.println(word);
// 		}
		
//		int test = counter.convertStringInt("19");
		
		
		
		// test code
		// String filename = "short.txt";
// 	  	counter.loadFromOriginalWordsFile(filename);
// 	 	System.out.println("----");
// 	 	counter.writeWordCountFile("counts_short.txt");
// 		counter.readWordCountFile("counts_short.txt");
//  		counter.writeWordCountFile("redo_counts_short.txt");
	}
}


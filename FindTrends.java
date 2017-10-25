/*
	Tatsuya Yokota
	FindTrends.java
	11/06/2016 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class FindTrends {
	private WordCounter wordCounter;
	private String wordCountBaseFilename;
	private	String wordCountNumberBegin;
	private String wordCountNumberEnd;
	private String[] trendWordList;
	private Integer writeOut = Integer.parseInt(System.console().readLine("Write data out to CSV file? Enter 1 for yes and 0 for no: "));
	private String fileName;
	private PrintWriter pw;
    private StringBuilder sb;
	
	public FindTrends( String[] argv ) throws FileNotFoundException {
		wordCounter = new WordCounter();
		wordCountBaseFilename = argv[0];
		wordCountNumberBegin = argv[1];
		wordCountNumberEnd = argv[2];
		trendWordList = new String[argv.length - 3];
		// add the specified trend words in an array
		for (int i = 3; i < argv.length; i++) {
			trendWordList[i-3] = argv[i];
		}
		
		// initialize things needed to write out to csv
		if (writeOut == 1) {
			fileName = System.console().readLine("Name the csv file (without .csv): ");
			pw = new PrintWriter(new File(fileName + ".csv"));
			sb = new StringBuilder();
		}
		
	}
	
	// prints out the frequency of appearance of the words in the trendWordList field
	public void findFreqenciesOfInterestingWords() throws FileNotFoundException, IOException {
		String ret = "";
		double freq;
		Integer yearDif = Integer.valueOf(wordCountNumberEnd) - Integer.valueOf(wordCountNumberBegin);
		System.out.println("Interesting words");
		// check for all specified trend word
		for (int j = 0; j < trendWordList.length; j++) {
			ret += trendWordList[j];
			if (writeOut == 1) {
				sb.append(trendWordList[j]);
			}
			// read the word count files of each year one by one
			for(int i = 0; i <= yearDif; i++) {
				int currentYear = Integer.valueOf(wordCountNumberBegin) + i;
				wordCounter.readWordCountFile(wordCountBaseFilename + currentYear+".txt");
				freq = wordCounter.getFrequency(trendWordList[j].toLowerCase());
				//System.out.println(trendWordList[j]);
				if (writeOut == 1) {
					sb.append(',');
					sb.append(freq);
				}
				ret += "  " + freq;	
			}
			if (writeOut == 1) {
				sb.append("\n");
			}
			System.out.println(ret);
			wordCounter.clear();
			ret = "";
		}
		if (writeOut == 1) {
			pw.write(sb.toString());
			pw.close();
		}
	}
	
	public static void main( String[] argv ) throws FileNotFoundException, IOException {
		FindTrends trendFinder = new FindTrends(argv);
		
		trendFinder.findFreqenciesOfInterestingWords();
	} 
}
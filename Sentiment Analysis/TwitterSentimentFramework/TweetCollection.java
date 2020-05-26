package student;


import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;

import org.apache.commons.csv.*;
import org.junit.Test;

import java.io.FileReader;


public class TweetCollection {

	private TreeMap<Integer, Tweet> tCol;
	private TreeMap<String, Vector<String>> iCol = new  TreeMap<String, Vector<String>>();
	private TreeMap<String, String> sWord = new  TreeMap<String, String>();
	
	public TweetCollection() {
		// Constructor

		tCol = new TreeMap<Integer, Tweet>();
	}
	
	/*
	 * functions for accessing individual tweets
	 */
	
	public Tweet getTweetByID (String ID) {
		// PRE: -
		// POST: Returns the Tweet object that with tweet ID
		
		for(int i = 0; i < tCol.size(); i++) {
			if(tCol.get(i).getID().compareTo(ID) ==0) {
				return tCol.get(i) ;
			}
		}
		return null ;
	}

	public Integer numTweets() {
		// PRE: -
		// POST: Returns the number of tweets in this collection
		
		return tCol.size();
	}
	

	/*
	 * functions for accessing sentiment words
	 */
	
	public Polarity getBasicSentimentWordPolarity(String w) {
		// PRE: w not null, basic sentiment words already read in from file
		// POST: Returns polarity of w
	
		if(sWord.containsKey(w) == true) {		
			if(sWord.get(w).compareTo("positive")==0) {
				return Polarity.POS;
			}else if(sWord.get(w).compareTo("negative")==0){
				return Polarity.NEG;
			}else {
				return Polarity.NEUT;					
			}
		}
		return Polarity.NONE;
	}
	
	public Polarity getFinegrainedSentimentWordPolarity(String w) {
		// PRE: w not null, finegrained sentiment words already read in from file
		// POST: Returns polarity of w
		
		// TODO

		return null;
	}
	
	public Strength getFinegrainedSentimentWordStrength(String w) {
		// PRE: w not null, finegrained sentiment words already read in from file
		// POST: Returns strength of w
		
		// TODO

		return null;
	}
	
	/*
	 * functions for reading in tweets
	 * 
	 */


	public void ingestTweetsFromFile(String fInName) throws IOException {
		// PRE: -
		// POST: Reads tweets from .csv file, stores in data structure
		
		// NOTES
		// Data source, file format description at http://help.sentiment140.com/for-students
		// Using apache csv reader: https://www.callicoder.com/java-read-write-csv-file-apache-commons-csv/
	
        try (
                Reader reader = Files.newBufferedReader(Paths.get(fInName));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            ) {
                
        	Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            int i =0;               
        	for (CSVRecord csvRecord : csvRecords) {
                // Accessing Values by Column Index

        		Tweet tw = new Tweet(csvRecord.get(0), // gold polarity
        				csvRecord.get(1), 				// ID
        				csvRecord.get(2), 				// date
        				csvRecord.get(4), 				// user
        				csvRecord.get(5));				// text
        		tCol.put(i, tw);
	        	i++;
            }
        }        
	}
	
	/*
	 * functions for sentiment words 
	 */
	
	public void importBasicSentimentWordsFromFile (String fInName) throws IOException {
		// PRE: -
		// POST: Read in and store basic sentiment words in appropriate data type

	    BufferedReader reader = new BufferedReader(new FileReader(fInName));  	 
		String newLine;
	    while ((newLine = reader.readLine()) != null) {  
		String temp[]= newLine.split(" ");
		sWord.put(temp[0], temp[1]);
		}
		reader.close();
	}
	
	public void importFinegrainedSentimentWordsFromFile (String fInName) throws IOException {
		// PRE: -
		// POST: Read in and store finegrained sentiment words in appropriate data type

		// TODO
	}
	
	
	public Boolean isBasicSentWord (String w) {
		// PRE: Basic sentiment words have been read in and stored
		// POST: Returns true if w is a basic sentiment word, false otherwise
		
		if(sWord.keySet().contains(w)){
			return true;
		}
		return false;
	}

	public Boolean isFinegrainedSentWord (String w) {
		// PRE: Finegrained sentiment words have been read in and stored
		// POST: Returns true if w is a finegrained sentiment word, false otherwise
		
		// TODO
		
		return null;
	}

	public void predictTweetSentimentFromBasicWordlist () {
		// PRE: Finegrained word sentiment already imported
		// POST: For all tweets in collection, tweet annotated with predicted sentiment
		//         based on count of sentiment words in sentWords

		for(int i =0; i < tCol.size(); i++) { 									
			int pos =0;
			int neg =0;
			String [] words;
			words = tCol.get(i).getWords();		
			for(int j = 0; j < words.length; j++) {  
				if(isBasicSentWord(words[j])==true){
					if(getBasicSentimentWordPolarity(words[j]) == Polarity.POS){
						pos++;
					}else if(getBasicSentimentWordPolarity(words[j]) == Polarity.NEG){  
						neg++;
					}
				}
			}	
			if(pos == 0 && neg == 0) { 								
				tCol.get(i).setPredictedPolarity(Polarity.NONE);
			}else if(pos < neg){											
				tCol.get(i).setPredictedPolarity(Polarity.NEG);
			}else if(pos > neg){										
				tCol.get(i).setPredictedPolarity(Polarity.POS);
			}else {															
				tCol.get(i).setPredictedPolarity(Polarity.NEUT);
			}
		}
	}

	public void predictTweetSentimentFromFinegrainedWordlist (Integer strongWeight, Integer weakWeight) {
		// PRE: Finegrained word sentiment already imported
		// POST: For all tweets in v, tweet annotated with predicted sentiment
		//         based on count of sentiment words in sentWords

		// TODO
	}

	/*
	 * functions for inverse index
	 * 
	 */
	
	public Map<String, Vector<String>> importInverseIndexFromFile (String fInName) throws IOException {
		// PRE: -
		// POST: Read in and returned contents of file as inverse index
		//         invIndex has words w as key, IDs of tweets that contain w as value

		// TODO
		
		BufferedReader reader = new BufferedReader(new FileReader(fInName)); 
		String newline;
		while ((newline = reader.readLine()) != null) {
			Vector<String> v = new Vector<String>();
			String key[]= newline.split(" ");
			String vec[] = key[1].split(",");
			for(int i =0 ; i<vec.length; i++) {
				v.add(vec[i]);
			}  
			iCol.put(key[0], v);
		}
		reader.close();
		return iCol;
	}


	/*
	 * functions for graph construction 
	 */
	
	public void constructSharedWordGraph(Map<String, Vector<String>> invIndex) {
		// PRE: invIndex has words w as key, IDs of tweets that contain w as value
		// POST: Graph constructed, with tweets as vertices, 
		//         and edges between them if they share a word

		for(Map.Entry<String, Vector<String>> entry : iCol.entrySet()) {
			for(int i=0;i<tCol.size();i++){
				if(entry.getValue().contains(tCol.get(i).getID())){
					for(int j=0;j<tCol.size();j++){
						if(entry.getValue().contains(tCol.get(j).getID()) && j!=i ){
							if(tCol.get(i).isNeighbour(tCol.get(j).getID())==false){
								tCol.get(i).addNeighbour(tCol.get(j).getID());
								tCol.get(j).addNeighbour(tCol.get(i).getID());
							}

							
						}
					}	
				}
			}	
		}  
	}


	public Integer numConnectedComponents() {
		// PRE: -
		// POST: Returns the number of connected components
		
		// TODO
		
		return null;
	}
	
	public void annotateConnectedComponents() {
		// PRE: -
		// POST: Annotates graph so that it is partitioned into components
		
		// TODO		
	}

	

	public Integer componentSentLabelCount(String ID, Polarity p) {
		// PRE: Graph components are identified, ID is a valid tweet
		// POST: Returns count of labels corresponding to Polarity p in component containing ID

		// TODO
		
		return null;
	}


	public void propagateLabelAcrossComponent(String ID, Polarity p, Boolean keepPred) {
		// PRE: ID is a tweet id in the graph
		// POST: Labels tweets in component with predicted polarity p 
		//         (if keepPred == T, only tweets w pred polarity None; otherwise all tweets

		// TODO
	}

	public void propagateMajorityLabelAcrossComponents(Boolean keepPred) {
		// PRE: Components are identified
		// POST: Tweets in each component are labelled with the majority sentiment for that component
		//       Majority label is defined as whichever of POS or NEG has the larger count;
		//         if POS and NEG are both zero, majority label is NONE
		//         otherwise, majority label is NEUT
		//       If keepPred is True, only tweets with predicted label None are labelled in this way
		//         otherwise, all tweets in the component are labelled in this way
		
		// TODO
	}
	
	

	/*
	 * functions for evaluation 
	 */
	
	public Double accuracy () {
		// PRE: -
		// POST: Calculates and returns accuracy of labelling
		
		double numCorrect = 0;
		double numPredicted =0;
		for(int i =0; i< tCol.size(); i++) { 
			if(tCol.get(i).getPredictedPolarity()!=Polarity.NONE) {
				numPredicted++;
				if(tCol.get(i).correctlyPredictedPolarity() == true){
				numCorrect++;
				}	
			}
		}
		return numCorrect/numPredicted;
	}
	
	public Double coverage () {
		// PRE: -
		// POST: Calculates and returns coverage of labelling
		
		double numPredicted =0; 
		for(int i =0; i< tCol.size(); i++) { 		
			if(tCol.get(i).getPredictedPolarity()!=Polarity.NONE) {
			numPredicted++;				
			}
		}
		return numPredicted/tCol.size();
	}
	
	
	
	public static void main(String[] args) {

		
	}
	
}

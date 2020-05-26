package student;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class TweetCollection {
    private Map<String, Tweet> m;
    private Map<String, Polarity> basicSentimentList;
    private Map<String, Strength> StrengthMap;
    private Map<String, Polarity> PolarityMap;
   
    // TODO: add appropriate data types

    public TweetCollection() {
        // Constructor

        // TODO
        m = new HashMap<>();
        basicSentimentList = new HashMap<>();
        PolarityMap = new HashMap<>();
        StrengthMap = new HashMap<>();
    }

	/*
     * functions for accessing individual tweets
	 */

    public Tweet getTweetByID(String ID) {
        // PRE: -
        // POST: Returns the Tweet object that with tweet ID

       
        return m.get(ID);
    }

    public Integer numTweets() {
        // PRE: -
        // POST: Returns the number of tweets in this collection

      
        return m.size();
    }


	/*
     * functions for accessing sentiment words
	 */

    public Polarity getBasicSentimentWordPolarity(String w) {
        // PRE: w not null, basic sentiment words already read in from file
        // POST: Returns polarity of w

        
        if (w == null) {
            return Polarity.NONE;
        }
        
        Polarity polarity = basicSentimentList.get(w);
        return polarity == null ? Polarity.NONE : polarity;
    }

    public Polarity getFinegrainedSentimentWordPolarity(String w) {
        // PRE: w not null, finegrained sentiment words already read in from file
        // POST: Returns polarity of w

        
        Polarity polarity = PolarityMap.get(w);
        return polarity == null ? Polarity.NONE : polarity;
    }

    public Strength getFinegrainedSentimentWordStrength(String w) {
        // PRE: w not null, finegrained sentiment words already read in from file
        // POST: Returns strength of w

        
        Strength strength = StrengthMap.get(w);
        return strength == null ? Strength.NONE : strength;
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

            for (CSVRecord csvRecord : csvRecords) {
                // Accessing Values by Column Index

                Tweet tw = new Tweet(csvRecord.get(0), // gold polarity
                        csvRecord.get(1),                // ID
                        csvRecord.get(2),                // date
                        csvRecord.get(4),                // user
                        csvRecord.get(5));                // text

                
                m.put(csvRecord.get(1), tw);
            }
        }
    }

	/*
     * functions for sentiment words
	 */

    public void importBasicSentimentWordsFromFile(String fInName) throws IOException {
        // PRE: -
        // POST: Read in and store basic sentiment words in appropriate data type

        
        BufferedReader reader = Files.newBufferedReader(Paths.get(fInName));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] wordAndPolarity = line.split("\\s");
            Polarity polarity;
            String word = wordAndPolarity[0];
            String polarityWord = wordAndPolarity[1];
            if (polarityWord.equals("negative")) {
                polarity = Polarity.NEG;
            } 
            else {
                polarity = Polarity.POS;
            }
            basicSentimentList.put(word, polarity);
        }
    }


    public void importFinegrainedSentimentWordsFromFile(String fInName) throws IOException {
        // PRE: -
        // POST: Read in and store finegrained sentiment words in appropriate data type

        
        BufferedReader reader = Files.newBufferedReader(Paths.get(fInName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] wordAndPolarity = line.split("\\s");
            String polarityStr = wordAndPolarity[5].split("=")[1];
            String word = wordAndPolarity[2].split("=")[1];
            String strengthStr = wordAndPolarity[0].split("=")[1];

            Polarity polarity = Polarity.NONE;
            if (polarityStr.equals("negative")) {
                polarity = Polarity.NEG;
            }
            if (polarityStr.equals("positive")) {
                polarity = Polarity.POS;
            }

            Strength strength = Strength.NONE;
            if (strengthStr.equals("weaksubj")) {
                strength = Strength.WEAK;
            }
            if (strengthStr.equals("strongsubj")) {
                strength = Strength.STRONG;
            }

            PolarityMap.put(word, polarity);
            StrengthMap.put(word, strength);
        }
    }


    public Boolean isBasicSentWord(String w) {
        // PRE: Basic sentiment words have been read in and stored
        // POST: Returns true if w is a basic sentiment word, false otherwise

        
        return basicSentimentList.containsKey(w);
    }

    public Boolean isFinegrainedSentWord(String w) {
        // PRE: Finegrained sentiment words have been read in and stored
        // POST: Returns true if w is a finegrained sentiment word, false otherwise

       
        return StrengthMap.containsKey(w);
    }

    public void predictTweetSentimentFromBasicWordlist() {
        // PRE: Finegrained word sentiment already imported
        // POST: For all tweets in collection, tweet annotated with predicted sentiment
        //         based on count of sentiment words in sentWords

       
        for (Tweet tweet : m.values()) {
            int posNum = 0;
            int negNum = 0;
            String[] words = tweet.getWords();
            for (String word : words) {
                Polarity polarityOfWord = getBasicSentimentWordPolarity(word);
                if (polarityOfWord.equals(Polarity.POS)) {
                    posNum++;
                } 
                else if (polarityOfWord.equals(Polarity.NEG)) {
                    negNum++;
                }
            }
            if (posNum == 0 && negNum == 0) {
                tweet.setPredictedPolarity(Polarity.NONE);
            } 
            else if (posNum > negNum) {
                tweet.setPredictedPolarity(Polarity.POS);
            } 
            else if (posNum < negNum) {
                tweet.setPredictedPolarity(Polarity.NEG);
            } 
            else {
                tweet.setPredictedPolarity(Polarity.NEUT);
            }
        }
    }

    public void predictTweetSentimentFromFinegrainedWordlist(Integer strongWeight, Integer weakWeight) {
        // PRE: Finegrained word sentiment already imported
        // POST: For all tweets in v, tweet annotated with predicted sentiment
        //         based on count of sentiment words in sentWords
    	
        
        for (Tweet tweet : m.values()) {
            int posNum = 0;
            int negNum = 0;
            String[] words = tweet.getWords();
            for (String word : words) {
                Polarity polarityOfWord = getFinegrainedSentimentWordPolarity(word);
                Strength strengthOfWord = getFinegrainedSentimentWordStrength(word);
                if (polarityOfWord.equals(Polarity.POS)) {
                    posNum += strengthOfWord.equals(Strength.STRONG) ? strongWeight : weakWeight;
                } 
                else if (polarityOfWord.equals(Polarity.NEG)) {
                    negNum += strengthOfWord.equals(Strength.STRONG) ? strongWeight : weakWeight;
                }
            }
            if (posNum == 0 && negNum == 0) {
                tweet.setPredictedPolarity(Polarity.NONE);
            } 
            else if (posNum > negNum) {
                tweet.setPredictedPolarity(Polarity.POS);
            } 
            else if (posNum < negNum) {
                tweet.setPredictedPolarity(Polarity.NEG);
            } 
            else {
                tweet.setPredictedPolarity(Polarity.NEUT);
            }
        }
    }

	/*
     * functions for inverse index
	 * 
	 */

    public Map<String, Vector<String>> importInverseIndexFromFile(String fInName) throws IOException {
        // PRE: -
        // POST: Read in and returned contents of file as inverse index
        //         invIndex has words w as key, IDs of tweets that contain w as value

        
        Map<String, Vector<String>> wordIdsMap = new HashMap<>();
        BufferedReader reader = Files.newBufferedReader(Paths.get(fInName));

        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] wordAndIds = line.split("\\s");
            String word = wordAndIds[0];
            String idsStr = wordAndIds[1];
            String[] ids = idsStr.split(",");
            Vector<String> idsVector = new Vector<>();
            idsVector.addAll(Arrays.asList(ids));
            wordIdsMap.put(word, idsVector);
        }
        return wordIdsMap;
    }


	/*
     * functions for graph construction
	 */

    public void constructSharedWordGraph(Map<String, Vector<String>> invIndex) {
        // PRE: invIndex has words w as key, IDs of tweets that contain w as value
        // POST: Graph constructed, with tweets as vertices,
        //         and edges between them if they share a word

        
        for (Tweet tweet : m.values()) {
            String[] words = tweet.getWords();
            for (String word : words) {
                if (invIndex.get(word) == null || invIndex.get(word).size() <= 1) {
                    continue;
                }

                for (String id : invIndex.get(word)) {
                    if (id.equals(tweet.getID())) {
                        continue;
                    }
                    if (m.containsKey(id)) {
                        tweet.addNeighbour(id);
                    }
                }
            }
        }
    }


    public Integer numConnectedComponents() {
        // PRE: -
        // POST: Returns the number of connected components

        
        Set<String> labelAnnotatedSet = new HashSet<>();
        for (Tweet tweet : m.values()) {
            labelAnnotatedSet.add(tweet.getLabelAnnotated());
        }
        return labelAnnotatedSet.size();
    }

    public void annotateConnectedComponents() {
        // PRE: -
        // POST: Annotates graph so that it is partitioned into components

       
        for (Tweet tweet : m.values()) {
            annotateConnectedComponents(tweet);
        }
    }

    private void annotateConnectedComponents(Tweet tweet) {
        for (String neighbourId : tweet.getNeighbourTweetIDs()) {
            Tweet neighbour = getTweetByID(neighbourId);
            if (neighbour.getLabelAnnotated().equals(tweet.getLabelAnnotated())) {
                continue;
            }
            neighbour.setLabelAnnotated(tweet.getLabelAnnotated());
            annotateConnectedComponents(neighbour);
        }
    }


    public Integer componentSentLabelCount(String ID, Polarity p) {
        // PRE: Graph components are identified, ID is a valid tweet
        // POST: Returns count of labels corresponding to  p in component containing ID

        
        Tweet tweet = getTweetByID(ID);
        String label = tweet.getLabelAnnotated();
        int res = 0;
        for (Tweet item : m.values()) {
            if (item.getLabelAnnotated().equals(label) && item.getPredictedPolarity().equals(p)) {
                res++;
            }
        }
        return res;
    }


    public void propagateLabelAcrossComponent(String ID, Polarity p, Boolean keepPred) {
        // PRE: ID is a tweet id in the graph
        // POST: Labels tweets in component with predicted polarity p
        //         (if keepPred == T, only tweets w pred polarity None; otherwise all tweets

        
        for (Tweet tweet : m.values()) {
            if (tweet.getLabelAnnotated().equals(getTweetByID(ID).getLabelAnnotated())) {
                if (keepPred && tweet.getPredictedPolarity().equals(Polarity.NONE)) {
                    tweet.setPredictedPolarity(p);
                }
                if (!keepPred) {
                    tweet.setPredictedPolarity(p);
                }
            }
        }
    }

    public void propagateMajorityLabelAcrossComponents(Boolean keepPred) {
        // PRE: Components are identified
        // POST: Tweets in each component are labelled with the majority sentiment for that component
        //       Majority label is defined as whichever of POS or NEG has the larger count;
        //         if POS and NEG are both zero, majority label is NONE
        //         otherwise, majority label is NEUT
        //       If keepPred is True, only tweets with predicted label None are labelled in this way
        //         otherwise, all tweets in the component are labelled in this way

        
        Map<String, Integer> negNumOfcomponents = new HashMap<>();
        Map<String, Integer> posNumOfcomponents = new HashMap<>();

        for (Tweet tweet : m.values()) {
            String labelAnnotated = tweet.getLabelAnnotated();
            if (tweet.getPredictedPolarity().equals(Polarity.NEG)) {
                negNumOfcomponents.putIfAbsent(labelAnnotated, 0);
                negNumOfcomponents.put(labelAnnotated, negNumOfcomponents.get(labelAnnotated) + 1);
            }
            if (tweet.getPredictedPolarity().equals(Polarity.POS)) {
                posNumOfcomponents.putIfAbsent(labelAnnotated, 0);
                posNumOfcomponents.put(labelAnnotated, posNumOfcomponents.get(labelAnnotated) + 1);
            }
        }
        for (Tweet tweet : m.values()) {
            String labelAnnotated = tweet.getLabelAnnotated();
            int negNumOfComponent = negNumOfcomponents.getOrDefault(labelAnnotated, 0);
            int posNumOfComponent = posNumOfcomponents.getOrDefault(labelAnnotated, 0);
            if (!keepPred || tweet.getPredictedPolarity().equals(Polarity.NONE)) {
                if (negNumOfComponent == 0 && posNumOfComponent == 0) {
                    tweet.setPredictedPolarity(Polarity.NONE);
                } 
                else if (negNumOfComponent > posNumOfComponent) {
                    tweet.setPredictedPolarity(Polarity.NEG);
                } 
                else if (negNumOfComponent < posNumOfComponent) {
                    tweet.setPredictedPolarity(Polarity.POS);
                } 
                else {
                    tweet.setPredictedPolarity(Polarity.NEUT);
                }
            }
        }
    }

	

	/*
     * functions for evaluation
	 */

    public Double accuracy() {
        // PRE: -
        // POST: Calculates and returns accuracy of labelling

        
        double numCorrect = 0;
        double numTotal = 0;
        for (Tweet tweet : m.values()) {
            if (tweet.getPredictedPolarity().equals(Polarity.NONE)) {
                continue;
            }
            numTotal++;
            if (tweet.getPredictedPolarity().equals(tweet.getGoldPolarity())) {
                numCorrect++;
            }
        }

        return numCorrect / numTotal;
    }

    public Double coverage() {
        // PRE: -
        // POST: Calculates and returns coverage of labelling

        
        double numTotal = 0;
        for (Tweet tweet : m.values()) {
            if (!tweet.getPredictedPolarity().equals(Polarity.NONE)) {
                numTotal++;
            }
        }

        return numTotal / m.size();
    }

    class PredictModel {
        int strongWeight;
        int weakWeight;

        double accuracy;
        double coverage;
        boolean isUsedGraph;

        @Override
        public String toString() {
            return String.format("strong weight : %d\n" +
                    "weak weight : %d\n" +
                    "is used graph : %s\n" +
                    "accuracy : %.2f\n" +
                    "coverage : %.2f\n", strongWeight, weakWeight, isUsedGraph, accuracy, coverage);
        }
    }

    public void myTweetSentimentPredictor() {
        double maxValue = 0;
        PredictModel bestPredictModel = null;
        for (int i = 0; i < 10; i += 1) {
            for (int j = 0; j < 10; j += 1) {
                PredictModel tmp = myTweetSentimentPredictor(i, j);
                if (tmp.accuracy * tmp.coverage >= maxValue) {
                    maxValue = tmp.accuracy * tmp.coverage;
                    bestPredictModel = tmp;
                }
            }
        }
        System.out.println(bestPredictModel);
    }

    public PredictModel myTweetSentimentPredictor(int strongWeight, int weakWeight) {
        // PRE: -
        // POST: All tweets are annotated with sentiment
    
        TweetCollection d = new TweetCollection();
        String SAMPLE_CSV_FILE_PATH = "training-40.csv";
        String INV_INDEX_FILE_PATH = "inv-index-40.txt";
        String FINEGRAINED_SENT_FILE_PATH = "finegrained-sent-words.txt";

    	    	
        PredictModel predictModel = new PredictModel();
        predictModel.strongWeight = strongWeight;
        predictModel.weakWeight = weakWeight;
        try {
            d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
            d.importFinegrainedSentimentWordsFromFile(FINEGRAINED_SENT_FILE_PATH);
            d.predictTweetSentimentFromFinegrainedWordlist(strongWeight, weakWeight);
            predictModel.accuracy = d.accuracy();
            predictModel.coverage = d.coverage();
            predictModel.isUsedGraph = false;

            Map<String, Vector<String>> invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
            d.constructSharedWordGraph(invIndex);
            d.annotateConnectedComponents();
            d.propagateMajorityLabelAcrossComponents(Boolean.TRUE);
            if (d.accuracy() * d.coverage() > predictModel.accuracy * predictModel.coverage) {
                predictModel.coverage = d.coverage();
                predictModel.accuracy = d.accuracy();
                predictModel.isUsedGraph = true;
            }
        } 
        
        catch (IOException e) {
            e.printStackTrace();
        }

    	return predictModel;
    }


    public static void main(String[] args) {
    	String SAMPLE_CSV_FILE_PATH = "C:\\Users\\Hank\\eclipse-workspace\\TwitterSentimentFramework\\data\\training-10.csv";
    	String BASIC_SENT_FILE_PATH = "C:\\Users\\Hank\\eclipse-workspace\\TwitterSentimentFramework\\data\\basic-sent-words.txt";
    	String INV_INDEX_FILE_PATH = "C:\\Users\\Hank\\eclipse-workspace\\TwitterSentimentFramework\\data\\inv-index-50.txt";
    	String FINEGRAINED_SENT_FILE_PATH = "C:\\Users\\Hank\\eclipse-workspace\\TwitterSentimentFramework\\data\\finegrained-sent-words.txt";
    	
	TweetCollection d = new TweetCollection();
			
			try {
				d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			}
			catch (IOException e) {
				System.out.println("in exception: " + e);
			}
			 System.out.println(d.numTweets());
	       
	    }

}

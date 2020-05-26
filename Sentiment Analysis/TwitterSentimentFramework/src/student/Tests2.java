package student;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;


public class Tests2 {

	String SAMPLE_CSV_FILE_PATH = "C:\\Users\\Hank\\eclipse-workspace\\TwitterSentimentFramework\\data\\training-40.csv";
	String BASIC_SENT_FILE_PATH = "C:\\Users\\Hank\\eclipse-workspace\\TwitterSentimentFramework\\data\\basic-sent-words.txt";
	String INV_INDEX_FILE_PATH = "C:\\Users\\Hank\\eclipse-workspace\\TwitterSentimentFramework\\data\\inv-index-40.txt";
	String FINEGRAINED_SENT_FILE_PATH = "C:\\Users\\Hank\\eclipse-workspace\\TwitterSentimentFramework\\data\\finegrained-sent-words.txt";

	// SAMPLE PASS-LEVEL TESTS
	
	@Test
	public void testNumTweets() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(40), d.numTweets());
	}
	
	@Test
	public void testTweetUser() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals("_TheSpecialOne_", d.getTweetByID("1467810369").getUser());
	}

	@Test
	public void testTweetGoldPolarity() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Polarity.POS, d.getTweetByID("2193602129").getGoldPolarity());
	}

	@Test
	public void testTweetPredictedPolarity() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Polarity.NONE, d.getTweetByID("2193602129").getPredictedPolarity());
	}

	@Test
	public void testTweetText() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals("Just woke up. Having no school is the best feeling ever ", d.getTweetByID("2193601966").getText());
	}

	@Test
	public void testTweetWords() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		String w = d.getTweetByID("1467810369").getWords()[0];
		assertEquals("awww", w);
	}

	@Test
	public void testTweetKeywordSentiment() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Polarity.POS, d.getTweetByID("2193602129").getPredictedPolarity());
	}

	@Test
	public void testTweetKeywordCorrectSentiment() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Boolean.TRUE, d.getTweetByID("2193602129").correctlyPredictedPolarity());
	}

	@Test
	public void testTweetKeywordIncorrectSentiment() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Boolean.FALSE, d.getTweetByID("2193579434").correctlyPredictedPolarity());
	}

	@Test
	public void testTweetKeywordAccuracy() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Double.valueOf(0.483), d.accuracy(), 0.01);
	}

	@Test
	public void testTweetKeywordCoverage() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Double.valueOf(0.725), d.coverage(), 0.01);
	}

	// SAMPLE CREDIT-LEVEL TESTS
	
	@Test
	public void testImportInverseIndex() {
		TweetCollection d = new TweetCollection();
		String[] IDs = {"1467813782", "2193578758"};
		Vector<String> v = new Vector<String>(Arrays.asList(IDs));
		Map<String, Vector<String>> invIndex = null;
		try {
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(v, invIndex.get("drinking"));
	}

	@Test
	public void testGraphConstruction() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.constructSharedWordGraph(invIndex);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(4), d.getTweetByID("2193602064").numNeighbours());
	}

	@Test
	public void testNumComponents() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.constructSharedWordGraph(invIndex);
			d.annotateConnectedComponents();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(16), d.numConnectedComponents());
	}

	@Test
	public void testComponentLabelCountPos() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
			d.constructSharedWordGraph(invIndex);
			d.annotateConnectedComponents();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(10), d.componentSentLabelCount("2193602064", Polarity.POS));
	}

	@Test
	public void testComponentLabelCountNeg() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
			d.constructSharedWordGraph(invIndex);
			d.annotateConnectedComponents();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(7), d.componentSentLabelCount("2193602064", Polarity.NEG));
	}

	@Test
	public void testComponentLabelCountNeut() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
			d.constructSharedWordGraph(invIndex);
			d.annotateConnectedComponents();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(3), d.componentSentLabelCount("2193602064", Polarity.NEUT));
	}

	@Test
	public void testComponentLabelCountNone() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
			d.constructSharedWordGraph(invIndex);
			d.annotateConnectedComponents();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Integer.valueOf(2), d.componentSentLabelCount("2193602064", Polarity.NONE));
	}


	@Test
	public void testLabelPropagationOverComponent1() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
			d.constructSharedWordGraph(invIndex);
			d.annotateConnectedComponents();
			d.propagateLabelAcrossComponent("2193602064", Polarity.NEUT, Boolean.TRUE);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Polarity.NEUT, d.getTweetByID("1467811372").getPredictedPolarity());
	}

	@Test
	public void testLabelPropagationOverComponent2() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
			d.constructSharedWordGraph(invIndex);
			d.annotateConnectedComponents();
			d.propagateLabelAcrossComponent("2193602064", Polarity.NEUT, Boolean.TRUE);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Polarity.POS, d.getTweetByID("2193602129").getPredictedPolarity());
	}

	@Test
	public void testComponentMajorityLabel() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.predictTweetSentimentFromBasicWordlist();
			d.constructSharedWordGraph(invIndex);
			d.annotateConnectedComponents();
			d.propagateMajorityLabelAcrossComponents(Boolean.TRUE);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Polarity.POS, d.getTweetByID("1467810369").getPredictedPolarity());
	}
	/*
	*/

	// SAMPLE DISTINCTION-LEVEL TESTS
	
	@Test
	public void testTweetFinegrainedKeywordSentiment() {
		TweetCollection d = new TweetCollection();
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			d.importFinegrainedSentimentWordsFromFile(FINEGRAINED_SENT_FILE_PATH);
			d.predictTweetSentimentFromFinegrainedWordlist(2, 1);
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		assertEquals(Polarity.NEG, d.getTweetByID("1467811594").getPredictedPolarity());
	}

	@Test
	public void testMyTweetSentimentPredictor() {
		TweetCollection d = new TweetCollection();
		Map<String, Vector<String>> invIndex = null;
		
		try {
			d.ingestTweetsFromFile(SAMPLE_CSV_FILE_PATH);
			d.importBasicSentimentWordsFromFile(BASIC_SENT_FILE_PATH);
			d.importFinegrainedSentimentWordsFromFile(FINEGRAINED_SENT_FILE_PATH);
			invIndex = d.importInverseIndexFromFile(INV_INDEX_FILE_PATH);
			d.constructSharedWordGraph(invIndex);
			//d.myTweetSentimentPredictor();
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
	}

}

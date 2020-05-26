package student;

import java.util.*;

enum Polarity {
	POS, NEG, NEUT, NONE;
}

enum Strength {
	STRONG, WEAK, NONE;
}

public class Tweet {

	private Polarity gold;
	private Polarity predicted;
	private String id;
	private String date;
	private String user;
	private String text;
	private TreeMap<String, Vector<String>> neighbours = new  TreeMap<String, Vector<String>>();
	private Vector<String> v = new Vector<String>();
	
	public Tweet(String p, String i, String d, String u, String t) {
		if(p.compareTo("0")==0){
			gold = Polarity.NEG;
		} else if(p.compareTo("2")==0){
			gold = Polarity.NEUT;
		} else if(p.compareTo("4")==0){
			gold = Polarity.POS;
		} else{
			gold = Polarity.NONE;
		}
		predicted = Polarity.NONE;
		this.id = i;
		this.date = d;
		this.user = u;
		this.text = t;
	}
	
	public void addNeighbour(String ID) {
		// PRE: -
		// POST: Adds a neighbour to the current tweet as part of graph structure
		
		v.add(ID);
		neighbours.put(this.getID(), v);
	}
	
	public Integer numNeighbours() {
		// PRE: -
		// POST: Returns the number of neighbours of this tweet
			
		return neighbours.get(this.getID()).size();
	}
	
	public void deleteAllNeighbours() {
		// PRE: -
		// POST: Deletes all neighbours of this tweet
		
		neighbours.clear();
	}
	
	public Vector<String> getNeighbourTweetIDs () {
		// PRE: -
		// POST: Returns IDs of neighbouring tweets as vector of strings
		
		return neighbours.get(this.getID());
	}

	public Boolean isNeighbour(String ID) {
		// PRE: -
		// POST: Returns true if ID is neighbour of the current tweet, false otherwise
		
		if(neighbours.isEmpty()==true || neighbours == null){
			return false;
		}
		if(neighbours.get(this.getID()).contains(ID) ==true) {
			return true;
		}

		return false;
	}
	
	
	public Boolean correctlyPredictedPolarity () {
		// PRE: -
		// POST: Returns true if predicted polarity matches gold, false otherwise
		
		if(gold == predicted){
			return true;
		} else{
			return false;
		}
	}
	
	public Polarity getGoldPolarity() {
		// PRE: -
		// POST: Returns the gold polarity of the tweet
		
		return this.gold;
	}
	
	public Polarity getPredictedPolarity() {
		// PRE: -
		// POST: Returns the predicted polarity of the tweet
		
		return this.predicted;
	}
	
	public void setPredictedPolarity(Polarity p) {
		// PRE: -
		// POST: Sets the predicted polarity of the tweet

		this.predicted = p;
	}
	
	public String getID() {
		// PRE: -
		// POST: Returns ID of tweet
		
		return this.id;
	}
	
	public String getDate() {
		// PRE: -
		// POST: Returns date of tweet
		
		return this.date;
	}
	
	public String getUser() {
		// PRE: -
		// POST: Returns identity of tweeter
		
		return this.user;
	}
	
	public String getText() {
		// PRE: -
		// POST: Returns text of tweet as a single string
		
		return this.text;
	}
	
	public String[] getWords() {
		// PRE: -
		// POST: Returns tokenised text of tweet as array of strings
				
		if (this.getText() == null)
			return null;

		String[] words = null;
		
		String tmod = this.getText();
		tmod = tmod.replaceAll("@.*?\\s", "");
		tmod = tmod.replaceAll("http.*?\\s", "");
		tmod = tmod.replaceAll("\\s+", " ");
		tmod = tmod.replaceAll("[\\W&&[^\\s]]+", "");
		tmod = tmod.toLowerCase();
		tmod = tmod.trim();
		words = tmod.split("\\s");
	
		return words;
		
	}
	
}

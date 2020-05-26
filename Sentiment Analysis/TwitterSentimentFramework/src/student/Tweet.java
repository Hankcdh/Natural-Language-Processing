package student;

import java.util.*;

enum Polarity {
    POS, NEG, NEUT, NONE;
}

enum Strength {
    STRONG, WEAK, NONE;
}

public class Tweet {

    private Polarity goldPolarity;
    private Polarity predictedPolarity;
    private String ID;
    private String Date;
    private String User;
    private String Text;

    private Set<String> Tweetadj;
    private String labeled;

    // TODO: Add appropriate data types

    public Tweet(String p, String i, String d, String u, String t) {
        // TODO
        if (p.equals("0")) {
            goldPolarity = Polarity.NEG;
        } 
        else if (p.equals("2")) {
            goldPolarity = Polarity.NEUT;
        } 
        else if (p.equals("4")) {
            goldPolarity = Polarity.POS;
        } 
        else {
            goldPolarity = Polarity.NONE;
        }
        
        predictedPolarity = Polarity.NONE;
        ID = i;
        Date = d;
        User = u;
        Text = t;
        labeled = ID;
        Tweetadj = new HashSet<>();
    }

    public void addNeighbour(String ID) {
        // PRE: -
        // POST: Adds a neighbour to the current tweet as part of graph structure

        // TODO
    	Tweetadj.add(ID);
    }

    public Integer numNeighbours() {
        // PRE: -
        // POST: Returns the number of neighbours of this tweet

        // TODO
        return Tweetadj.size();
    }

    public void deleteAllNeighbours() {
        // PRE: -
        // POST: Deletes all neighbours of this tweet

        // TODO
    	Tweetadj.clear();
    }

    public Vector<String> getNeighbourTweetIDs() {
        // PRE: -
        // POST: Returns IDs of neighbouring tweets as vector of strings

        // TODO
        return new Vector<>(Tweetadj);
    }

    public Boolean isNeighbour(String ID) {
        // PRE: -
        // POST: Returns true if ID is neighbour of the current tweet, false otherwise

        // TODO
        return Tweetadj.contains(ID);
    }


    public Boolean correctlyPredictedPolarity() {
        // PRE: -
        // POST: Returns true if predicted polarity matches gold, false otherwise

        // TODO
        return predictedPolarity.equals(goldPolarity);
    }

    public Polarity getGoldPolarity() {
        // PRE: -
        // POST: Returns the gold polarity of the tweet

        // TODO
        return goldPolarity;
    }

    public Polarity getPredictedPolarity() {
        // PRE: -
        // POST: Returns the predicted polarity of the tweet

        // TODO
        return predictedPolarity;
    }

    public void setPredictedPolarity(Polarity p) {
        // PRE: -
        // POST: Sets the predicted polarity of the tweet

        // TODO
        predictedPolarity = p;
    }

    public String getID() {
        // PRE: -
        // POST: Returns ID of tweet

        // TODO
        return ID;
    }

    public String getDate() {
        // PRE: -
        // POST: Returns date of tweet

        // TODO
        return Date;
    }

    public String getUser() {
        // PRE: -
        // POST: Returns identity of tweeter

        // TODO
        return User;
    }

    public String getText() {
        // PRE: -
        // POST: Returns text of tweet as a single string

        // TODO
        return Text;
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

    @Override
    public String toString() {
        return String.format("id:%s\ndate:%s\nuser:%s\ntext:%s\ngoldPolarity:%s\n", ID, Date, User, Text, goldPolarity);
    }


    public String getLabelAnnotated() {
        return labeled;
    }

    public void setLabelAnnotated(String labelAnnotated) {
        this.labeled = labelAnnotated;
    }
}

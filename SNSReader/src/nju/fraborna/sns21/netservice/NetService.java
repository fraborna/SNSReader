package nju.fraborna.sns21.netservice;

import java.util.ArrayList;

import nju.fraborna.sns21.model.Tweet;

public interface NetService {

	public ArrayList<Tweet> getTweets(int num);

	public boolean publish(String content);
	
}

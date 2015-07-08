package com.RockstartWebServices;

public class TwitterAnaysisBAL {
	TwitterAnalysisDAL dalObject= new TwitterAnalysisDAL();
	String string1[];
	
	public String extractTime (String requestAnalysis)
	{
		String tweetimestamp= null;
		string1 = requestAnalysis.split("&");
		String string2[]= string1[1].split("=");
		tweetimestamp= string2[1];
		tweetimestamp = tweetimestamp.replaceAll("%20", " ");
		return tweetimestamp;
	}
	public String extractUserID (String requestAnalysis)
	{
		String userID= null;
		String string2[]= string1[0].split("=");
		userID= string2[1];
		return userID;
	}
	
	public String getanalysisResponse (String userID, String tweettimestamp)
	{
		String outputfromDAL= dalObject.gettweetData(userID,tweettimestamp);
		
		return outputfromDAL;
	}

}

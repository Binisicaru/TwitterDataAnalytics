package com.RockstartWebServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TwitterAnalysisDAL {
	Connection conn = null;
	
	TwitterAnalysisDAL(){
		try
		{
			String connDriver= "com.mysql.jdbc.Driver";
			Class.forName(connDriver);
			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/twitanalysis","root","");
		}
		catch (Exception ex)
		{
			System.out.println(ex.getStackTrace());
		}
	}
	public String gettweetData ( String userID, String tweetimestamp)
	{
		String tweetDataOutput="";
	    try
	    {
	    	Statement dbStatement= conn.createStatement();
	    	String query= "Select * from twitAnalysisData where userID='"+ userID + "' and tweettimestamp='"+tweetimestamp + "';";
	    	ResultSet results= dbStatement.executeQuery(query);
	    	
	    	while (results.next()) {
	    		tweetDataOutput+= results.getString(2)+":"+ results.getString(4)+":"+ results.getString(5);
	    		
	    		tweetDataOutput+="\n";
				
			}
	    }
	    catch (Exception ex)
	    {
	    	System.out.println(ex.toString());
	    }
	    return tweetDataOutput;
		
	}

}

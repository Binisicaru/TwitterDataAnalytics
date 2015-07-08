package com.RockstartWebServices;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class TwitterAnalysisFrontEndService {
	private static String tweettime= null;
	private static String userID=null;
	private static String analysisresponse= null;
	private static String requestAnalysis= null;
	static TwitterAnaysisBAL twitteranalysisBAL = new TwitterAnaysisBAL(); 
	
	public static void main(String[] args) {
		Undertow server = Undertow.builder()
				.addHttpListener(8080, "localhost")
				.setHandler(new HttpHandler() {
					@Override
					public void handleRequest(final HttpServerExchange exchange) throws Exception {
					 requestAnalysis = exchange.getQueryString();
						if(requestAnalysis.length() != 0)
						{
							 tweettime = twitteranalysisBAL.extractTime(requestAnalysis);
							
							 userID = twitteranalysisBAL.extractUserID(requestAnalysis);
							
							 analysisresponse = twitteranalysisBAL.getanalysisResponse(userID, tweettime);
							
							exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
							exchange.getResponseSender().send("theRockstars" + "," + 
									"," + "\n" + analysisresponse);
						}
					}
				}).build();
		server.start();
	}

}

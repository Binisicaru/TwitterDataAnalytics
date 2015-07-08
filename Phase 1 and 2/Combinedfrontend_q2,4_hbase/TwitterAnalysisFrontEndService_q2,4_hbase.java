import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class TwitterAnalysisFrontEndService {
	private static String tweettime= "";
	private static String userID="";
	private static String analysisresponse= "";
	private static String requestAnalysis= "";
	static TwitterAnalysisBAL twitteranalysisBAL = new TwitterAnalysisBAL();
	// Front-end cache to capture data.
	static Map<String, String> tweetCache = new LinkedHashMap<String, String>(300000, 1f) {
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 299999;
		}
	};
	
	// Before uploading, change HttpListener port and Host,
	// TwitterAnalysisBAL getConnection values.
	public static void main(String[] args) {
		twitteranalysisBAL.scanTable();
		Undertow server = Undertow.builder()
				.addHttpListener(80, "ec2-54-173-190-145.compute-1.amazonaws.com")
				.setHandler(new HttpHandler() {
					@Override
					public void handleRequest(final HttpServerExchange exchange) throws Exception {
						requestAnalysis = exchange.getQueryString();
						String stepCompare = exchange.getRelativePath();
						if(requestAnalysis.length() != 0)
						{
							if(stepCompare=="/q2?")
							{
							// Extract the required fields from the input request.
							tweettime = twitteranalysisBAL_q2.extractTime(requestAnalysis);
							userID = twitteranalysisBAL_q2.extractUserID(requestAnalysis);
							// If date is before the specified date, do not hit the database.
							if(twitteranalysisBAL_q2.checkTwitterDate(tweettime)) {
								// Check if requested data is present in the cache.
								analysisresponse = twitteranalysisBAL_q2.getCachedData(userID, tweettime);
								// If not, fetch it from the database.
								if(analysisresponse.equals("")) {
									analysisresponse = twitteranalysisBAL_q2.getanalysisResponse(userID, tweettime);
								} 
								// Obtain the proper unicode characters from the data.
								analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
								} else 
								{
								analysisresponse = "";
								}
							}
							else if (stepCompare="/q4?")
							{
								Starttweettime = twitteranalysisBAL_q4.extractStartTimeStamp(requestAnalysis);
								Endtweettime = twitteranalysisBAL_q4.extractEndTimeStamp(requestAnalysis);
								hashtag = twitteranalysisBAL_q4.extracthashtag(requestAnalysis);
								//caching part and analysis response how to show?
								analysisresponse = twitteranalysisBAL_q4.getCachedData(hashtag);
								if(analysisresponse.equals("")) {
									analysisresponse = twitteranalysisBAL_q4.getanalysisResponse(hashtag,Starttweettime,Endtweettime);
								} else {
									twitteranalysisBAL_q4.addToCache(analysisresponse);
								}
								analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);

							}
						}
						exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain; charset=UTF-8");
						exchange.getResponseSender().send("TeamRockStars,327811142774,617128749441,910842319737\n" + analysisresponse);
					}
				}).build();
		server.start();
	}
}

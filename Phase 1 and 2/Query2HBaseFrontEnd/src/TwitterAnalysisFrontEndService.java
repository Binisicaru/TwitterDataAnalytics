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
						if(requestAnalysis.length() != 0)
						{
							// Extract the required fields from the input request.
							tweettime = twitteranalysisBAL.extractTime(requestAnalysis);
							userID = twitteranalysisBAL.extractUserID(requestAnalysis);
							// If date is before the specified date, do not hit the database.
							if(twitteranalysisBAL.checkTwitterDate(tweettime)) {
								// Check if requested data is present in the cache.
								analysisresponse = twitteranalysisBAL.getCachedData(userID, tweettime);
								// If not, fetch it from the database.
								if(analysisresponse.equals("")) {
									analysisresponse = twitteranalysisBAL.getanalysisResponse(userID, tweettime);
								} 
								// Obtain the proper unicode characters from the data.
								analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
							} else {
								analysisresponse = "";
							}
						}
						exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain; charset=UTF-8");
						exchange.getResponseSender().send("TeamRockStars,327811142774,617128749441,910842319737\n" + analysisresponse);
					}
				}).build();
		server.start();
	}
}

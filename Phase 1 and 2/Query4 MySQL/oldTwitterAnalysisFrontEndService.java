import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

// The comments are similar to that in the HBase code.
public class TwitterAnalysisFrontEndService {
	private static String tweettime= "";
	private static String userID="";
	private static String analysisresponse= "";
	private static String requestAnalysis= "";
	static TwitterAnalysisBAL twitteranalysisBAL = new TwitterAnalysisBAL();
	static Map<String, String> tweetCache = new LinkedHashMap<String, String>(100000, 1f) {
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999;
		}
	};

	public static void main(String[] args) {
		Undertow server = Undertow.builder()
				.addHttpListener(80, "localhost")
				.setHandler(new HttpHandler() {
					@Override
					public void handleRequest(final HttpServerExchange exchange) throws Exception {
						requestAnalysis = exchange.getQueryString();
						if(requestAnalysis.length() != 0)
						{
							#retrieving starttime, endtime and hashtag;
							Starttweettime = twitteranalysisBAL.extractStartTimeStamp(requestAnalysis);
							Endtweettime = twitteranalysisBAL.extractEndTimeStamp(requestAnalysis);
							hashtag = twitteranalysisBAL.extracthashtag(requestAnalysis);
							//caching part and analysis response how to show?
								analysisresponse = twitteranalysisBAL.getCachedData(hashtag);
								if(analysisresponse.equals("")) {
									analysisresponse = twitteranalysisBAL.getanalysisResponse(hashtag,Starttweettime,Endtweettime);
								} else {
									twitteranalysisBAL.addToCache(analysisresponse);
								}
								analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
							
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

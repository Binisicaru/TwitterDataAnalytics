package q3FrontEnd;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

// The comments are similar to that in the HBase code.
public class CombinedFrontend {
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
							//query1 check

							String stepCompare = exchange.getRelativePath();
							if(stepCompare=="/q1?")
							{
							String res = decodeReq.parseHttpReq(request);
							Date date= Calendar.getInstance().getTime();
							Timestamp  ts = new Timestamp(date.getTime());
							String time = ts.toString();
							SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date currentDate;
							currentDate = date_format.parse(time);
							String time_val = date_format.format(currentDate);
							
							}
							//query2 check
							else if(stepCompare=="/q2?")
							{
							tweettime = twitteranalysisBAL_q2.extractTime(requestAnalysis);
							userID = twitteranalysisBAL_q2.extractUserID(requestAnalysis);
							if(twitteranalysisBAL_q2.checkTwitterDate(tweettime)) {
								analysisresponse = twitteranalysisBAL_q2.getCachedData(userID, tweettime);
								if(analysisresponse.equals("")) {
									analysisresponse = twitteranalysisBAL_q2.getanalysisResponse(userID, tweettime);
								} else {
									twitteranalysisBAL_q2.addToCache(userID+"#"+tweettime, analysisresponse);
								}
								analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
							} else {
								analysisresponse = "";
							}
							}
							//query3 check
							else if (stepCompare == "/q3?")
							{
							}
							//query4 check
							else if (stepCompare=="/q4")
							{
								//retrieving starttime, endtime and hashtag
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

								analysisresponse = "";
							}
							exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain; charset=UTF-8");
							exchange.getResponseSender().send("TeamRockStars,327811142774,617128749441,910842319737\n" + analysisresponse);
						}
					}
				}).build();
		server.start();
	}
}

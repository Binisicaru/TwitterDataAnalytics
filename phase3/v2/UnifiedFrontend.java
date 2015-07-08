import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringEscapeUtils;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

// The comments are similar to that in the HBase code.
public class UnifiedFrontend {
	private static String tweetTime= "";
	private static String userID="";
	private static String analysisresponse= "";
	//private static String requestAnalysis= "";

	//for q4
	private static String startTweetTime = "";
	private static String endTweetTime = "";
	private static String hashTag = "";
	
	//for q5
	private static String user_list = "";
	
	//for q6
	private static BigDecimal m;
	private static BigDecimal n;

	static TwitterAnalysisBAL_q2 twitteranalysisBAL_q2 = new TwitterAnalysisBAL_q2();
	static TwitterAnalysisBAL_q4 twitteranalysisBAL_q4 = new TwitterAnalysisBAL_q4();
	//q5 and q6
	static TwitterAnalysisBAL_q5 twitteranalysisBAL_q5 = new TwitterAnalysisBAL_q5();
	static TwitterAnalysisBAL_q6 twitteranalysisBAL_q6 = new TwitterAnalysisBAL_q6();
	static Cipher cipher = new Cipher();

	public static String processQuery1(String requestAnalysis) throws ParseException {
		String decodedMessage = cipher.getDecodeMessage(requestAnalysis);
		Date date= Calendar.getInstance().getTime();
		Timestamp  currentTime = new Timestamp(date.getTime());
		String currentTimeString = currentTime.toString();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDateTime = dateFormat.parse(currentTimeString);
		String formattedDateTime = dateFormat.format(currentDateTime);
		return formattedDateTime + "\n"+ decodedMessage + "\n";
	}

	public static String processQuery2(String requestAnalysis) throws ParseException {
		userID = twitteranalysisBAL_q2.extractUserID(requestAnalysis);
		tweetTime = twitteranalysisBAL_q2.extractTime(requestAnalysis);
		
		if(twitteranalysisBAL_q2.checkTwitterDate(tweetTime)) {
			analysisresponse = twitteranalysisBAL_q2.getCachedData(userID, tweetTime);
			if(analysisresponse.equals("")) {
				analysisresponse = twitteranalysisBAL_q2.getanalysisResponse(userID, tweetTime);
			} else {
				twitteranalysisBAL_q2.addToCache(userID+"#"+tweetTime, analysisresponse);
			}
			analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
		} else {
			analysisresponse = "";
		}
		return analysisresponse;
	}

	public static void processQuery3() {

	}

	public static void processQuery4() {

	}

	public static void main(String[] args) {
		Undertow server = Undertow.builder()
								.setWorkerThreads(4096)
								.setIoThreads(Runtime.getRuntime().availableProcessors()*2)
				.addHttpListener(80, "ec2-52-5-190-28.compute-1.amazonaws.com")
				.setHandler(new HttpHandler() {
					@Override
					public void handleRequest(final HttpServerExchange exchange) {
						try {
							String requestAnalysis = exchange.getQueryString();
							if(requestAnalysis.length() != 0)
							{
								String stepCompare = exchange.getRelativePath();
								if(stepCompare.contains("/q1")) {
									analysisresponse = processQuery1(requestAnalysis);
								}

								else if(stepCompare.contains("/q2")) {
									analysisresponse = processQuery2(requestAnalysis);
								}
								//query3 check
								else if (stepCompare.contains("/q3"))
								{
									//								userID = twitteranalysisBAL_q3.extracthashtag(requestAnalysis);
									//
									//								analysisresponse = twitteranalysisBAL_q3.getanalysisResponse(userID);
									//
									//								analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
								}
								//query4 check
								else if (stepCompare.contains("/q4"))
								{
									//retrieving starttime, endtime and hashtag
									startTweetTime = twitteranalysisBAL_q4.extractStartTimeStamp(requestAnalysis);
									endTweetTime = twitteranalysisBAL_q4.extractEndTimeStamp(requestAnalysis);
									hashTag = twitteranalysisBAL_q4.extracthashtag(requestAnalysis);
									//caching part and analysis response how to show?
									//analysisresponse = twitteranalysisBAL_q4.getCachedData(hashtag);
									//if(analysisresponse.equals("")) {
									analysisresponse = twitteranalysisBAL_q4.getanalysisResponse(hashTag,startTweetTime,endTweetTime);
									//} else {
									//twitteranalysisBAL_q4.addToCache(analysisresponse);
									//}
									analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);

									//analysisresponse = "";
								}
								else if (stepCompare.contains("/q5"))
								{
									user_list = twitteranalysisBAL_q5.extractUserList(requestAnalysis);
									startTweetTime = twitteranalysisBAL_q5.extractStartTimeStamp(requestAnalysis);
									endTweetTime = twitteranalysisBAL_q5.extractEndTimeStamp(requestAnalysis);
									analysisresponse = twitteranalysisBAL_q5.getanalysisResponse(user_list,startTweetTime,endTweetTime);
									analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
								}
								else if (stepCompare.contains("/q6"))
								{
									m = twitteranalysisBAL_q6.extractM(requestAnalysis);
									n = twitteranalysisBAL_q6.extractN(requestAnalysis);
									analysisresponse = twitteranalysisBAL_q6.getanalysisResponse(m,n);
									analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
								}								
								else if(stepCompare.contains("/index.html"))
								{
									analysisresponse = "heartbeat";
								}
								exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain; charset=UTF-8");
								exchange.getResponseSender().send("TeamRockStars,327811142774,617128749441,910842319737\n" + analysisresponse);
							}
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
				}).build();
		server.start();
	}
}

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringEscapeUtils;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class UnifiedFrontend {
	private static String tweetTime = "";
	private static String userID = "";
	private static String analysisresponse = "";

	// For q4
	private static String startTweetTime = "";
	private static String endTweetTime = "";
	private static String hashTag = "";
	
	// For q5
	private static String userList = "";
	
	// For q6
	private static BigDecimal m;
	private static BigDecimal n;

	static UnifiedModel unifiedModel = new UnifiedModel();
	static UnifiedCache unifiedCache = new UnifiedCache();
	static UnifiedDataAccess unifiedDataAccess = new UnifiedDataAccess();

	public static void main(String[] args) {
		Undertow server = Undertow.builder()
				.setWorkerThreads(4096)
				.setIoThreads(Runtime.getRuntime().availableProcessors()*2)
				.addHttpListener(80, "ec2-52-6-54-89.compute-1.amazonaws.com")
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
								else if (stepCompare.contains("/q3")) {
									analysisresponse = processQuery3(requestAnalysis);
								}
								else if (stepCompare.contains("/q4")) {
									analysisresponse = processQuery4(requestAnalysis);
								}
								else if (stepCompare.contains("/q5")) {
									analysisresponse = processQuery5(requestAnalysis);
								}
								else if (stepCompare.contains("/q6")) {
									analysisresponse = processQuery6(requestAnalysis);
								}
								else if(stepCompare.contains("/index.html")) {
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

	public static String processQuery1(String requestAnalysis) throws ParseException {
		String decodedMessage = unifiedModel.decodeMessage(requestAnalysis);
		Date date= Calendar.getInstance().getTime();
		Timestamp  currentTime = new Timestamp(date.getTime());
		String currentTimeString = currentTime.toString();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDateTime = dateFormat.parse(currentTimeString);
		String formattedDateTime = dateFormat.format(currentDateTime);
		return formattedDateTime + "\n" + decodedMessage + "\n";
	}

	public static String processQuery2(String requestAnalysis) throws ParseException {
		userID = unifiedModel.extractUserID(requestAnalysis);
		tweetTime = unifiedModel.extractTime(requestAnalysis);

		if(unifiedModel.isTweetDateValid(tweetTime)) {
			analysisresponse = unifiedCache.getCache2Data(userID, tweetTime);
			if(analysisresponse.equals("")) {
				analysisresponse = unifiedDataAccess.getCensoredTextAndScore(userID, tweetTime);
			} else {
				unifiedCache.addToCache2(userID + "#" + tweetTime, analysisresponse);
			}
			analysisresponse = StringEscapeUtils.unescapeJava(analysisresponse);
		} else {
			analysisresponse = "";
		}
		return analysisresponse;
	}

	public static String processQuery3(String requestAnalysis) {
		userID = unifiedModel.extractUserID(requestAnalysis);
		analysisresponse = unifiedDataAccess.getRetweetBuddies(userID);
		return analysisresponse;
	}

	public static String processQuery4(String requestAnalysis) {
		hashTag = unifiedModel.extractHashtag(requestAnalysis);
		startTweetTime = unifiedModel.extractStartTimeStamp(requestAnalysis);
		endTweetTime = unifiedModel.extractEndTimeStamp(requestAnalysis);
		analysisresponse = unifiedDataAccess.getHashtagTweets(hashTag, startTweetTime, endTweetTime);
		return StringEscapeUtils.unescapeJava(analysisresponse);
	}
	
	public static String processQuery5(String requestAnalysis) {
		userList = unifiedModel.extractUserList(requestAnalysis);
		startTweetTime = unifiedModel.extractStartTimeStamp(requestAnalysis);
		endTweetTime = unifiedModel.extractEndTimeStamp(requestAnalysis);
		analysisresponse = unifiedDataAccess.getCounts(userList, startTweetTime, endTweetTime);
		return StringEscapeUtils.unescapeJava(analysisresponse);
	}
	
	public static String processQuery6(String requestAnalysis) {
		m = unifiedModel.extractM(requestAnalysis);
		n = unifiedModel.extractN(requestAnalysis);
		analysisresponse = unifiedDataAccess.getHermitCount(m,n);
		return StringEscapeUtils.unescapeJava(analysisresponse);
	}
}

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class TwitterAnalysisFrontEndService {
	private static String tweettime= null;
	private static String userID=null;
	private static String analysisresponse= null;
	private static String requestAnalysis= null;
	static TwitterAnalysisBAL twitteranalysisBAL = new TwitterAnalysisBAL(); 
	
	public static void main(String[] args) {
		Undertow server = Undertow.builder()
				.addHttpListener(80, "ec2-54-86-95-82.compute-1.amazonaws.com")
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
							exchange.getResponseSender().send("TeamRockStars,327811142774,617128749441,910842319737\n" + analysisresponse);
						}
					}
				}).build();
		server.start();
	}
}

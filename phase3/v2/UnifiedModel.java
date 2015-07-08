import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class UnifiedModel {
	
	static Map<String, String> tweetCache = new LinkedHashMap<String, String>(100000, 1f) {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999;
		}
	};
	
	// Logic for Query 1
	public String decodeMessage(String httpRequest) {
		int keyParamIndex = httpRequest.indexOf("key");
		int messageParamIndex = httpRequest.indexOf("&message");
		
		String key = httpRequest.substring(keyParamIndex+4, messageParamIndex);
		String message = httpRequest.substring(messageParamIndex+9);
		
		BigInteger XY = new BigInteger(key);
		//return decode(message,XY);
		return null;
	}
	
	// Logic for Query 2
	public boolean isTweetDateValid(String date) throws ParseException {
		final String TWITTER="yyyy-MM-dd+HH:mm:ss";
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);
		Date tweetDate = sf.parse(date);
		Date stdDate = sf.parse("2014-04-20+00:00:00");
		if(tweetDate.after(stdDate)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String extractUserID(String requestAnalysis) {
		String[] requestParts = requestAnalysis.split("&");
		String userID = requestParts[0].split("=")[1];
		return userID;
	}
	
	public String extractTime(String requestAnalysis) {
		String[] requestParts = requestAnalysis.split("&");
		String tweetTimestamp = requestParts[1].split("=")[1];
		tweetTimestamp = tweetTimestamp.replaceAll("%20", " ");
		return tweetTimestamp;
	}
	
	public String getCachedData(String userID, String tweettimestamp) {
		String tweetDataOutput = "";
		String dataretrieve= userID+"#"+tweettimestamp;
		if(tweetCache.containsKey(dataretrieve)) {
			tweetDataOutput = tweetCache.get(dataretrieve);
		}
		return tweetDataOutput;
	}
	
	public void addToCache(String userID, String tweet) {
		tweetCache.put(userID, tweet);
	}
	
	// Logic for Query 4
	public String extractHashtag(String requestAnalysis)
	{
		String hashtag;
		String string2[] = requestAnalysis.split("&");
		String string3[] = string2[0].split("=");
		hashtag= string3[1];
		return hashtag;
	}

	public String extractStartTimeStamp (String requestAnalysis)
	{
		String startTimeStamp;
		String string2[] = requestAnalysis.split("&");
		String string3[] = string2[1].split("=");
		startTimeStamp = string3[1];
		startTimeStamp = startTimeStamp.replaceAll("%20", " ");
		return startTimeStamp;
	}
	
	public String extractEndTimeStamp (String requestAnalysis)
	{
		String endTimeStamp;
		String string2[] = requestAnalysis.split("&");
		String string3[] = string2[2].split("=");
		endTimeStamp = string3[1];
		endTimeStamp = endTimeStamp.replaceAll("%20", " ");
		return endTimeStamp;
	}
}

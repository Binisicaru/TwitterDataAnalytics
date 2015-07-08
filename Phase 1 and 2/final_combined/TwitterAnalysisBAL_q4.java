import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.sql.Timestamp;

public class TwitterAnalysisBAL_q4{
	Connection conn = null;
	String string1[];
	String sql = "select hashtag,result from hashTagAnalysis where hashtag=? and timestampval>=? and timestampval<=?";
	PreparedStatement preparedStatement;

	TwitterAnalysisBAL_q4(){
		try
		{
			String connDriver= "com.mysql.jdbc.Driver";
			Class.forName(connDriver);
			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/twitanalysis","remote","remote_pass");
			//conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/twitanalysis","root","");
			preparedStatement = conn.prepareStatement(sql);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/*public boolean checkTwitterDate(String date) throws ParseException {
		final String TWITTER="yyyy-MM-dd+HH:mm:ss";
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);
		Date tweetDate = sf.parse(date);
		Date stdDate = sf.parse("2014-04-20+00:00:00");
		if(tweetDate.after(stdDate))
			return true;
		else
			return false;
	}*/

	public String extracthashtag(String requestAnalysis)
	{
		String hashtag= null;
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[0].split("=");
		hashtag= string3[1];
		return hashtag;
	}

	public String extractStartTimeStamp (String requestAnalysis)
	{
		String StartTimeStamp= null;
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[1].split("=");
		StartTimeStamp= string3[1];
		StartTimeStamp = StartTimeStamp.replaceAll("%20", " ");
		return StartTimeStamp;
	}
	
	public String extractEndTimeStamp (String requestAnalysis)
	{
		String EndTimeStamp= null;
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[2].split("=");
		EndTimeStamp= string3[1];
		EndTimeStamp = EndTimeStamp.replaceAll("%20", " ");
		return EndTimeStamp;
	}

	public String getanalysisResponse (String hashtag, String StartTimeStamp, String EndTimeStamp)
	{
		String tweetDataOutput = "";
		StartTimeStamp += " 00:00:00";
		EndTimeStamp += " 23:59:59";
		try
		{
			preparedStatement.setString(1, hashtag);
			preparedStatement.setTimestamp(2, Timestamp.valueOf(StartTimeStamp));
			preparedStatement.setTimestamp(3, Timestamp.valueOf(EndTimeStamp));
			ResultSet results= preparedStatement.executeQuery();
			String comparison = "";
			
			while (results.next()) {
				if(hashtag.equals(results.getString(1)))
					tweetDataOutput+= results.getString(2)+"\n";
			}
			//tweetDataOutput = "\n";
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return tweetDataOutput;
	}

	/*public String getCachedData(String hashtag) {
		String tweetDataOutput = "";
		String dataretrieve= hashtag;
		if(TwitterAnalysisFrontEndService.tweetCache.containsKey(dataretrieve)) {
			tweetDataOutput = TwitterAnalysisFrontEndService.tweetCache.get(dataretrieve);
		}
		return tweetDataOutput;
	}*/
	
	/*public void addToCache(String hashtag, String tweet) {
		TwitterAnalysisFrontEndService.tweetCache.put(hashtag, tweet);
	}*/
}

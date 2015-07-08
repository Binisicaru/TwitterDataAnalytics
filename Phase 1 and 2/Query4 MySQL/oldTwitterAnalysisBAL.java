import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TwitterAnalysisBAL{
	Connection conn = null;
	String string1[];
	String sql = "select twitdetails from tweetHash where hashtag=? and timestamp>=? and timestamp<=?";
	PreparedStatement preparedStatement;

	TwitterAnalysisBAL(){
		try
		{
			String connDriver= "com.mysql.jdbc.Driver";
			Class.forName(connDriver);
			conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/twitanalysis","root","");
			preparedStatement = conn.prepareStatement(sql);

		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
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
		string1 = requestAnalysis.split("?");
		String string2[]= string1[2].split("&");
		String string3[]=string2[1].split("=");
		hashtag= string3[2];
		//tweetimestamp = tweetimestamp.replaceAll("%20", " ");
		return hashtag;
	}

	public String extractStartTimeStamp (String requestAnalysis)
	{
		String StartTimeStamp= null;
		String string1[]= string1.split("?");
		String string2[]= string1[2].split("&");
		String string3[]=string2[2].split("=");
		StartTimeStamp= string3[2];
		return StartTimeStamp;
	}
	public String extractEndTimeStamp (String requestAnalysis)
	{
		String EndTimeStamp= null;
		String string1[]= string1.split("?");
		String string2[]= string1[2].split("&");
		String string3[]=string2[3].split("=");
		EndTimeStamp= string3[2];
		return EndTimeStamp;
	}

	public String getanalysisResponse (String hashtag, String StartTimeStamp, String EndTimeStamp)
	{
		String tweetDataOutput = "";
		String dataretrieve= hashtag;
		try
		{
			preparedStatement.setString(1, hashtag);
			preparedStatement.setString(2, StartTimeStamp);
			preparedStatement.setString(3, EndTimeStamp);
			ResultSet results= preparedStatement.executeQuery();
			while (results.next()) {
				tweetDataOutput+= results.getString(1)+"\n";
			}
			//tweetDataOutput = "\n";
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
		}
		return tweetDataOutput;
	}

	public String getCachedData(String hashtag) {
		String tweetDataOutput = "";
		String dataretrieve= hashtag;
		if(TwitterAnalysisFrontEndService.tweetCache.containsKey(dataretrieve)) {
			tweetDataOutput = TwitterAnalysisFrontEndService.tweetCache.get(dataretrieve);
		}
		return tweetDataOutput;
	}
	
	public void addToCache(String hashtag, String tweet) {
		TwitterAnalysisFrontEndService.tweetCache.put(hashtag, tweet);
	}
}

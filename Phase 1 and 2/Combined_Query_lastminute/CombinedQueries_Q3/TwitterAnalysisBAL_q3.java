import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.sql.Timestamp;

public class TwitterAnalysisBAL_q3{
	Connection conn = null;
	String string1[];
	String sql = "select hashtag,result from hashTagAnalysis where hashtag=? and timestampval>=? and timestampval<=?";
	PreparedStatement preparedStatement;

	TwitterAnalysisBAL_q3(){
		try
		{
			String connDriver= "com.mysql.jdbc.Driver";
			Class.forName(connDriver);
			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/twitanalysis","root","");
			//conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/twitanalysis","root","");
			preparedStatement = conn.prepareStatement(sql);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public String extracthashtag(String requestAnalysis)
	{
		String hashtag= null;
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[0].split("=");
		hashtag= string3[1];
		return hashtag;
	}

	public String getanalysisResponse (String userid)
	{
		String tweetDataOutput = "";
		//StartTimeStamp += " 00:00:00";
		//EndTimeStamp += " 23:59:59";
		try
		{
			preparedStatement.setString(1, userid);
			//preparedStatement.setTimestamp(2, Timestamp.valueOf(StartTimeStamp));
			//preparedStatement.setTimestamp(3, Timestamp.valueOf(EndTimeStamp));
			ResultSet results= preparedStatement.executeQuery();
			String comparison = "";
			
			while (results.next()) {
				String[] currentData = results.getString().split(";");
				for(String currentRow: currentData)
					tweetDataOutput+= currentRow+"\n";
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

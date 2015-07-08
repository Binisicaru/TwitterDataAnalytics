import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TwitterAnalysisBAL{
	Connection conn = null;
	String string1[];
	
	TwitterAnalysisBAL(){
		try
		{
			String connDriver= "com.mysql.jdbc.Driver";
			Class.forName(connDriver);
			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/twitanalysis","remote","remote_pass");
		}
		catch (Exception ex)
		{
			System.out.println(ex.getStackTrace());
		}
	}
	
	public String extractTime (String requestAnalysis)
	{
		String tweetimestamp= null;
		string1 = requestAnalysis.split("&");
		String string2[]= string1[1].split("=");
		tweetimestamp= string2[1];
		tweetimestamp = tweetimestamp.replaceAll("%20", " ");
		return tweetimestamp;
	}
	public String extractUserID (String requestAnalysis)
	{
		String userID= null;
		String string2[]= string1[0].split("=");
		userID= string2[1];
		return userID;
	}
	public String getanalysisResponse (String userID, String tweettimestamp)
	{
		String tweetDataOutput = "";
		String dataretrieve= userID+"#"+tweettimestamp;
		try
	    {
	    	Statement dbStatement= conn.createStatement();
	    	String query= "Select twitdata from twitAnalysisData where userID='"+ dataretrieve + "';";
	    	ResultSet results= dbStatement.executeQuery(query);
			while (results.next())
				tweetDataOutput+= results.getString(1);			
	    }
	    catch (Exception ex)
	    {
	    	System.out.println(ex.toString());
	    }
		return tweetDataOutput;
	}
}

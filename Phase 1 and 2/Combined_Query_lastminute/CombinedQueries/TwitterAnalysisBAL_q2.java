import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class TwitterAnalysisBAL_q2{
	Connection conn = null;
	String string1[];
	String sql = "Select twitdata from twitAnalysisData where userID=?";
	PreparedStatement preparedStatement;
	DataSource dataSource;

	TwitterAnalysisBAL_q2(){
		try
		{
			HikariConfig hikariConfig = new HikariConfig();
			hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
			hikariConfig.setMaximumPoolSize(150);
			hikariConfig.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/twitanalysis");
			hikariConfig.addDataSourceProperty("user", "root");
			hikariConfig.addDataSourceProperty("password", "");
			hikariConfig.addDataSourceProperty("cachePrepStmts", true);
			hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
			hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
			hikariConfig.addDataSourceProperty("prepStmtCacheSize", 300);
			
			dataSource = new HikariDataSource(hikariConfig);
//			String connDriver= "com.mysql.jdbc.Driver";
//			Class.forName(connDriver);
//			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/twitanalysis","remote","remote_pass");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public boolean checkTwitterDate(String date) throws ParseException {
		final String TWITTER="yyyy-MM-dd+HH:mm:ss";
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);
		Date tweetDate = sf.parse(date);
		Date stdDate = sf.parse("2014-04-20+00:00:00");
		if(tweetDate.after(stdDate))
			return true;
		else
			return false;
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
			conn = dataSource.getConnection();
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, dataretrieve);
			ResultSet results= preparedStatement.executeQuery();
			while (results.next()) {
				tweetDataOutput+= results.getString(1);
			}
			tweetDataOutput += "\n";
			results.close();
			preparedStatement.close();
			conn.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return tweetDataOutput;
	}

	public String getCachedData(String userID, String tweettimestamp) {
		String tweetDataOutput = "";
		String dataretrieve= userID+"#"+tweettimestamp;
		if(CombinedFrontend.tweetCache.containsKey(dataretrieve)) {
			tweetDataOutput = CombinedFrontend.tweetCache.get(dataretrieve);
		}
		return tweetDataOutput;
	}
	
	public void addToCache(String userID, String tweet) {
		CombinedFrontend.tweetCache.put(userID, tweet);
	}
}

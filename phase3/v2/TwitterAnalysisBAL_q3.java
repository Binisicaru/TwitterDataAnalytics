import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class TwitterAnalysisBAL_q3{
//	Connection conn = null;
	String sql = "select twitdata from twitAnalysisData where userID=?";
//	PreparedStatement preparedStatement;
	DataSource dataSource;
	
	static Map<String, String> tweetCache = new LinkedHashMap<String, String>(100000, 1f) {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999;
		}
	};

	TwitterAnalysisBAL_q3(){
		try
		{
			HikariConfig hikariConfig = new HikariConfig();
			hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
			hikariConfig.setMaximumPoolSize(150);
			hikariConfig.addDataSourceProperty("url", "jdbc:mysql://127.0.0.1:3306/twitanalysis");
			hikariConfig.addDataSourceProperty("user", "root");
			hikariConfig.addDataSourceProperty("password", "");
			hikariConfig.addDataSourceProperty("cachePrepStmts", true);
			hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
			hikariConfig.addDataSourceProperty("prepStmtCacheSize", 300);
			
			dataSource = new HikariDataSource(hikariConfig);
//			conn = dataSource.getConnection();
//			preparedStatement = conn.prepareStatement(sql);
//			String connDriver= "com.mysql.jdbc.Driver";
//			Class.forName(connDriver);
//			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/twitanalysis","root","");
			//preparedStatement = conn.prepareStatement(sql);
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

	public String getanalysisResponse (String userID, String tweettimestamp)
	{
		String tweetDataOutput = "";
		String dataretrieve= userID+"#"+tweettimestamp;
		try
		{
			Connection conn = dataSource.getConnection();
			//conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/twitanalysis","root","");
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, dataretrieve);
			ResultSet results= preparedStatement.executeQuery();
			while (results.next()) {
				tweetDataOutput+= results.getString(1);
			}
			tweetDataOutput += "\n";
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
		if(tweetCache.containsKey(dataretrieve)) {
			tweetDataOutput = tweetCache.get(dataretrieve);
		}
		return tweetDataOutput;
	}
	
	public void addToCache(String userID, String tweet) {
		tweetCache.put(userID, tweet);
	}
}

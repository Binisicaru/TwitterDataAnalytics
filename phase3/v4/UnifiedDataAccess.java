import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class UnifiedDataAccess {
	HikariDataSource dataSource;

	String query2 = "select twitdata from twitAnalysisData where userID=?";
	String query3 = "select result from relation_table where userid=?";
	String query4 = "select hashtag, result from hashTagAnalysis where hashtag=? and timestampval>=? and timestampval<=?";
	String query5 = "select count(*), max(frndCount), max(follCount) from ranksterAnalysis where userid=? and timestampval>=? and timestampval<=?";
	String query6 = "select count(*) from hermitAnalysis where userid>=? and userid<=?";

	RelationComparator relationComparator;
	
	public UnifiedDataAccess() {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Data Access for Query 2
	public String getCensoredTextAndScore(String userID, String tweettimestamp) {
		String tweetDataOutput = "";
		String dataretrieve= userID + "#" + tweettimestamp;
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(query2);
			preparedStatement.setString(1, dataretrieve);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				tweetDataOutput+= results.getString(1);
			}
			tweetDataOutput += "\n";
			results.close();
			preparedStatement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tweetDataOutput;
	}

	// Data Access for Query 3
	public String getRetweetBuddies(String userID) {
		String tweetDataOutput = "";
		List<String> relationsValues = new ArrayList<>();
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(query3);
			preparedStatement.setString(1, userID);
			ResultSet results = preparedStatement.executeQuery();

			while (results.next()) {
				String[] currentData = results.getString(1).split(":");
				for(String currentRow : currentData) {
					relationsValues.add(currentRow);
				}
			}
			try {
				relationComparator = new RelationComparator(relationsValues);
				Collections.sort(relationsValues, relationComparator);
			} catch(Exception e) {
				
			}
			for(String relationsValue : relationsValues) {
				tweetDataOutput += relationsValue + "\n";
			}
			results.close();
			preparedStatement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tweetDataOutput;
	}

	// Data Access for Query 4
	public String getHashtagTweets(String hashtag, String startTimeStamp, String endTimeStamp) {
		String tweetDataOutput = "";
		startTimeStamp += " 00:00:00";
		endTimeStamp += " 23:59:59";
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(query4);
			preparedStatement.setString(1, hashtag);
			preparedStatement.setTimestamp(2, Timestamp.valueOf(startTimeStamp));
			preparedStatement.setTimestamp(3, Timestamp.valueOf(endTimeStamp));
			ResultSet results= preparedStatement.executeQuery();

			while (results.next()) {
				if(hashtag.equals(results.getString(1))) {
					tweetDataOutput += results.getString(2) + "\n";
				}
			}
			results.close();
			preparedStatement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(tweetDataOutput);
		return tweetDataOutput;
	}

	// Data Access for Query 5
	public String getCounts(String userList, String startTimeStamp, String endTimeStamp) {
		String tweetDataOutput = "";
		String[] users = userList.split(",");
		startTimeStamp += " 00:00:00";
		endTimeStamp += " 23:59:59";
		int score = 0;
		TreeMap<Integer,String> res = new TreeMap<Integer,String>();
		ResultSet results = null;
		for(String user : users) {
			try {
				Connection conn = dataSource.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query5);
				preparedStatement.setString(1, user);
				preparedStatement.setTimestamp(2, Timestamp.valueOf(startTimeStamp));
				preparedStatement.setTimestamp(3, Timestamp.valueOf(endTimeStamp));
				results= preparedStatement.executeQuery();

				while (results.next()) {
					score = results.getInt(1) + (3*results.getInt(2)) + (5*results.getInt(3)); 
				}			
				res.put(score, user);
				results.close();
				preparedStatement.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<Integer> keys = new ArrayList<Integer>(res.keySet());
		for(int keyCounter = keys.size()-1 ; keyCounter >= 0 ; keyCounter--) {
			tweetDataOutput += res.get(keys.get(keyCounter)) + "," + keys.get(keyCounter) + "\n";
		}
		return tweetDataOutput;
	}

	// Data Access for Query 6
	public String getHermitCount(BigDecimal m, BigDecimal n) {
		String tweetDataOutput = "";
		try
		{
			Connection conn = dataSource.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(query6);
			preparedStatement.setBigDecimal(1, m);
			preparedStatement.setBigDecimal(2, n);
			ResultSet results= preparedStatement.executeQuery();

			while (results.next()) {
				tweetDataOutput += results.getString(1);
			}
			results.close();
			preparedStatement.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tweetDataOutput;
	}
}

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class TwitterAnalysisBAL_q6 {
	//Connection conn = null;
	String sql = "select count(*) from hermitAnalysis where userid>=? and userid<=?";
	//PreparedStatement preparedStatement;
	DataSource dataSource;
	
	TwitterAnalysisBAL_q6(){
		try
		{
			HikariConfig hikariConfig = new HikariConfig();
			hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
			hikariConfig.setMaximumPoolSize(150);
			hikariConfig.addDataSourceProperty("url", "jdbc:mysql://127.0.0.1:3306/twitanalysis");
			hikariConfig.addDataSourceProperty("user", "root");
			hikariConfig.addDataSourceProperty("password", "");
			hikariConfig.addDataSourceProperty("cachePrepStmts", true);
			hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
			hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
			hikariConfig.addDataSourceProperty("prepStmtCacheSize", 300);
			
			dataSource = new HikariDataSource(hikariConfig);
			
			//String connDriver= "com.mysql.jdbc.Driver";
			//Class.forName(connDriver);
			//conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/twitanalysis","root","");
			//preparedStatement = conn.prepareStatement(sql);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public String getanalysisResponse (BigDecimal m, BigDecimal n)
	{
		String tweetDataOutput = "";
		try
		{
			Connection conn = dataSource.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setBigDecimal(1, m);
			preparedStatement.setBigDecimal(2, n);
			
			ResultSet results= preparedStatement.executeQuery();
			
			while (results.next()) {
					tweetDataOutput += results.getString(1);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return tweetDataOutput;
	}
	
	public BigDecimal extractM(String requestAnalysis) {
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[0].split("=");
		BigDecimal m = new BigDecimal(string3[1]);
		return m;
	}
	
	public BigDecimal extractN(String requestAnalysis) {
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[1].split("=");
		BigDecimal n = new BigDecimal(string3[1]);
		return n;
	}
}

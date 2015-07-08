import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class TwitterAnalysisBAL_q6 {
	Connection conn = null;
	String sql = "select count(*) from hermitAnalysis where userid>=? and userid<=?";
	PreparedStatement preparedStatement;

	TwitterAnalysisBAL_q6(){
		try
		{
			String connDriver= "com.mysql.jdbc.Driver";
			Class.forName(connDriver);
			conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/twitanalysis","root","");
			preparedStatement = conn.prepareStatement(sql);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public String getanalysisResponse (int m, int n)
	{
		String tweetDataOutput = "";
		try
		{
			preparedStatement.setInt(1, m);
			preparedStatement.setInt(2, n);
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
	
	public int extractM(String requestAnalysis) {
		int m = 0;
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[0].split("=");
		m = Integer.parseInt(string3[1]);
		return m;
	}
	
	public int extractN(String requestAnalysis) {
		int n = 0;
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[1].split("=");
		n = Integer.parseInt(string3[1]);
		return n;
	}
}

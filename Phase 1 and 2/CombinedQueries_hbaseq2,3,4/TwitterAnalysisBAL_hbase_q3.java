import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class TwitterAnalysisBAL{
	Configuration config = null;
	String string1[];
	HTable table;

	// Create a connection to the HBase table.
	TwitterAnalysisBAL(){
		config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum","ec2-54-173-190-145.compute-1.amazonaws.com");
		config.set("hbase.zookeeper.property.clientPort", "2181");

		try {
			table = new HTable(config, "relationTable");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	// Extract userID;
	public String extractuserid(String requestAnalysis)
	{
		String userid= null;
		String string2[]= requestAnalysis.split("&");
		String string3[]=string2[0].split("=");
		userid= string3[1];
		return userid;
	}


	// Scan the HBase table to cache data.
	public void scanTable() {
		int counter=0;
		try {
			Scan scanRequest = new Scan();
			scanRequest.setCaching(500000);
			ResultScanner scanner = table.getScanner(scanRequest);
			for(Result result=scanner.next() ; result!=null ; result=scanner.next()) {
				TwitterAnalysisFrontEndService.tweetCache.put(Bytes.toString(result.getRow()), Bytes.toString(result.getValue(Bytes.toBytes("userid"), Bytes.toBytes(""))));
				counter++;
				if(counter==299998) {
					break;
				}
			}
			System.out.println("Caching done...");
		} catch(IOException ioe) {
			System.out.println("Counter: " + counter);
			ioe.printStackTrace();
		}
	}

	// Get the value for the input key from the HBase table.
	public String getanalysisResponse (String hashtag, String starttimestamp, String endtimestamp)
	{
		String tweetDataOutput = "";
		try {
			String dataretrieve= userID+"#"+tweettimestamp;
			Get getRequest = new Get(Bytes.toBytes(dataretrieve));
			Result result = table.get(getRequest);
			byte[] byteValue = result.getValue(Bytes.toBytes("userID"), Bytes.toBytes(""));
			tweetDataOutput = Bytes.toString(byteValue);
			tweetDataOutput += "\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tweetDataOutput;
	}
	
	// Check if data is present in the cache.
	public String getCachedData(String userID, String tweettimestamp) {
		String tweetDataOutput = "";
		String dataretrieve= userID+"#"+tweettimestamp;
		if(TwitterAnalysisFrontEndService.tweetCache.containsKey(dataretrieve)) {
			tweetDataOutput = TwitterAnalysisFrontEndService.tweetCache.get(dataretrieve);
		}
		tweetDataOutput += "\n";
		return tweetDataOutput;
	}
}

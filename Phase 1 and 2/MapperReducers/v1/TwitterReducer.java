import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TwitterReducer {
	public static void main(String[] args) {
		String mapperTweetDetails;
		String[] tweetDetails;
		String ids[];
		String combinedIds;
		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while((mapperTweetDetails=reader.readLine()) != null) {
				tweetDetails = mapperTweetDetails.split("\\t");
				combinedIds = tweetDetails[0];
				ids = combinedIds.split("#");
				System.out.println(ids[0] + "#" + tweetDetails[1] + "\t" + ids[1] + ":" + tweetDetails[2] + ":" + tweetDetails[3]);
			}
		}
		catch(IOException io){
			io.printStackTrace();
		}
	}
	
}
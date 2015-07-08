import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Mapper {
	public static void main(String[] args) {
		Gson gson = new Gson();
		JsonObject retweeted_status;
		StringBuilder userId = new StringBuilder();
		StringBuilder retweeted_userId= new StringBuilder();
		StringBuilder  returnString = new StringBuilder(); 

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input;

			while((input = br.readLine()) != null){		
				try
				{
					returnString.setLength(0);
					retweeted_userId.setLength(0);
					userId.setLength(0);

					Tweet obj = gson.fromJson(input, Tweet.class);					
					JsonObject tweet = new JsonParser().parse(input).getAsJsonObject();

					if(obj.retweeted == true)
					{
						retweeted_status = tweet.get("retweeted_status").getAsJsonObject();
						retweeted_userId.append(retweeted_status.get("user").getAsJsonObject().get("id_str").getAsString());
						userId.append(tweet.get("user").getAsJsonObject().get("id_str").getAsString());
						returnString.append(userId.toString() + "\t" + retweeted_userId.toString());
						System.out.println(returnString.toString());
					}
				}
				catch(Exception io)
				{
					continue;
				}
			}
			br.close();
		}
		catch(Exception io){
			io.printStackTrace();
		}
	}
}

class Tweet{
	Boolean retweeted;
}

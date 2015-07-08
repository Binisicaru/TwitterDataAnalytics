package query3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Mapper {
	private static Boolean checkValidity(String hash)
	{
		return hash.matches("^[a-zA-Z0-9]*$");
	}
	public static void main(String[] args) {
		Gson gson = new Gson();
		JsonArray hashTagArray;
		String tweetId = "";
		String userId="";
		String timestamp = "";
		String returnString=""; 
		String hashTag = "";
		Boolean isValid = false;

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input;

			while((input = br.readLine()) != null){		
				try
				{
					Tweet obj = gson.fromJson(input, Tweet.class);					
					JsonObject tweet = new JsonParser().parse(input).getAsJsonObject();

					hashTagArray = tweet.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray();
					tweetId = obj.id;
					timestamp = obj.created_at;
					userId = tweet.get("user").getAsJsonObject().get("id_str").getAsString();

					for(JsonElement js:hashTagArray)
					{
						hashTag = js.getAsJsonObject().get("text").getAsString();
						isValid = checkValidity(hashTag);
						if(isValid == true)
						{
							returnString = hashTag + ":" + tweetId + "\t" +  tweetId + "," + userId + "," + timestamp;
							System.out.println(returnString);
						}
						else
						{
							System.out.println("Not valid::" + hashTag);
						}
					}
				}
				catch(Exception io)
				{
					continue;
				}
			}
		}
		catch(Exception io){
			io.printStackTrace();
		}
	}
}

class Tweet{
	String id;
	String created_at;
}
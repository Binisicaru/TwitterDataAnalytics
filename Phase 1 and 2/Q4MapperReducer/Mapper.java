import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

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

	public static String convertDateFormat(String jsonDate) throws ParseException {
		final String JSON_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		final String REQUEST_DATE_FORMAT = "yyyy-MM-dd+HH:mm:ss";
		SimpleDateFormat jsonDateFormat = new SimpleDateFormat(JSON_DATE_FORMAT);
		SimpleDateFormat requestDateFormat = new SimpleDateFormat(REQUEST_DATE_FORMAT);
		jsonDateFormat.setLenient(true);
		Date tweetDate = jsonDateFormat.parse(jsonDate);
		return requestDateFormat.format(tweetDate);
	}

	public static void main(String[] args) {
		Gson gson = new Gson();
		JsonArray hashTagArray;
		StringBuilder tweetId = new StringBuilder();
		StringBuilder userId= new StringBuilder();
		StringBuilder timestamp = new StringBuilder();
		StringBuilder  returnString = new StringBuilder(); 
		StringBuilder hashTag = new StringBuilder();
		Boolean isValid = false;
		StringBuilder formattedTimeStamp = new StringBuilder();

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input;

			while((input = br.readLine()) != null){		
				try
				{
					returnString.setLength(0);
					tweetId.setLength(0);
					userId.setLength(0);
					timestamp.setLength(0);
					hashTag.setLength(0);
					formattedTimeStamp.setLength(0);

					Tweet obj = gson.fromJson(input, Tweet.class);					
					JsonObject tweet = new JsonParser().parse(input).getAsJsonObject();

					hashTagArray = tweet.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray();
					tweetId.append(obj.id);
					timestamp.append(obj.created_at.trim());
					userId.append(tweet.get("user").getAsJsonObject().get("id_str").getAsString());
					formattedTimeStamp.append(convertDateFormat(timestamp.toString())); 
					for(JsonElement js:hashTagArray)
					{
						hashTag.setLength(0);
						returnString.setLength(0);
						hashTag.append(js.getAsJsonObject().get("text").getAsString());
						isValid = checkValidity(hashTag.toString());
						if(isValid == true)
						{
							returnString.append(hashTag.toString() + ":" + tweetId.toString() + "\t" +  tweetId.toString() + "," + userId.toString() + "," + formattedTimeStamp.toString());
							System.out.println(returnString.toString());
						}
					}
				}
				catch(Exception io)
				{

					io.printStackTrace();
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
	String id;
	String created_at;
}

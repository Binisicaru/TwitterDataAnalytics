
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Mapper {
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
		
		String followers_count = "";
		String userId="";
		String timestamp = "";
		String returnString=""; 
		String friends_count = "";

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input="";
			while((input = br.readLine()) != null){		
				try
				{
					Tweet obj = gson.fromJson(input, Tweet.class);					
					JsonObject tweet = new JsonParser().parse(input).getAsJsonObject();
					timestamp = obj.created_at;
					timestamp = convertDateFormat(timestamp);
					userId = tweet.get("user").getAsJsonObject().get("id_str").getAsString();
					friends_count= tweet.get("user").getAsJsonObject().get("friends_count").getAsString();
					followers_count= tweet.get("user").getAsJsonObject().get("followers_count").getAsString();	
					returnString = userId + "\t" +timestamp  + "\t" +friends_count + "\t" + followers_count ;
					System.out.println(returnString);	
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
	String created_at;
}
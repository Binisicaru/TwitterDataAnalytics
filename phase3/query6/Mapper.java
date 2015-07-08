import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Mapper {
	

	public static void main(String[] args) {
		String userId="";
		String returnString=""; 
		String location="";

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			//BufferedReader br = new BufferedReader(new FileReader("D:\\Tweet\\q4\\tweets.txt"));
			String input;

			while((input = br.readLine()) != null){		
				try
				{					
					JsonObject tweet = new JsonParser().parse(input).getAsJsonObject();
					userId = tweet.get("user").getAsJsonObject().get("id_str").getAsString();
					JsonElement loc = tweet.get("place");
					try{
					if(loc != null)
						location = loc.getAsJsonObject().get("name").getAsString();
					}
					catch(Exception e)
					{}
					if(location.length() == 0)
					{
						returnString = userId + "\t" + "1";
					}
					else
					{
						returnString = userId + "\t" + "0";
						location = "";
					}
					System.out.println(returnString);
				}
				catch(Exception io)
				{
					io.printStackTrace();
				}
			}
			br.close();
		}
		catch(Exception io){
			io.printStackTrace();
		}
	}
}
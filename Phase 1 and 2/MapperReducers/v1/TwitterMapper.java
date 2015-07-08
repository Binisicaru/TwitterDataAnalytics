import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringEscapeUtils;


class Tweet {
	String id;
	String text;
	String created_at;
}

public class TwitterMapper {
	private static Map<String, Integer> scores = new HashMap<String, Integer>();
	public static final String url = "https://s3.amazonaws.com/15619s15files/afinn.txt";
	public static final String bannedurl = "https://s3.amazonaws.com/15619s15files/banned.txt";
	public static List<String> bannedWordList = new ArrayList<>();
	public static int finalscore = 0;

	/**
	 * 
	 * @param text
	 * @return This method returns the score of a particular word in the tweet
	 *         text
	 */
	public static int getScore(String text) {
		int score = 0;
		if (scores.containsKey(text))
			score = scores.get(text);
		return score;
	}

	/**
	 * 
	 * generates a hashmap having each word with its corresponding score
	 * 
	 * @throws IOException
	 */
	public static void readSentimentsFromFile() throws IOException {

		URL affinityurl = new URL(url);
		Scanner affinityurlScanner = new Scanner(affinityurl.openStream());
		while (affinityurlScanner.hasNextLine()) {
			String line = affinityurlScanner.nextLine();
			String[] line_elements = line.split("\\s+");
			try{
				scores.put(line_elements[0], Integer.parseInt(line_elements[1]));
			}
			catch(NumberFormatException e){
				continue;
			}
		}
		affinityurlScanner.close();
	}

	/**
	 * generates an arraylist having all the censored word
	 * 
	 * @throws IOException
	 */
	public static void readCensoredFromFile() throws IOException {
		URL bannedwordsurl = new URL(bannedurl);
		Scanner bannedwordsurlScanner = new Scanner(bannedwordsurl.openStream());
		while (bannedwordsurlScanner.hasNextLine()) {
			String currentBannedWord = bannedwordsurlScanner.nextLine().trim();
			String rotate13 = rotateWord13(currentBannedWord);
			bannedWordList.add(rotate13);
		}
		bannedwordsurlScanner.close();

	}

	/**
	 * 
	 * @param word
	 * @return to rotate the word to generate the correct censored word
	 */
	public static String rotateWord13(String word) {
		StringBuilder stringbuilder = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			char wordpart = word.charAt(i);
			if ((wordpart >= 'a' && wordpart <= 'm')
					|| (wordpart >= 'A' && wordpart <= 'M')) {
				wordpart += 13;
			} else if ((wordpart >= 'n' && wordpart <= 'z')
					|| (wordpart >= 'N' && wordpart <= 'Z')) {
				wordpart -= 13;
			}
			stringbuilder.append(wordpart);
		}
		return stringbuilder.toString();
	}

	/**
	 * 
	 * @param text
	 * @return transforms the tweet text and censors the required parts
	 */
	public static String removeBannedPartsTransform(String text) {
		StringBuilder tweet = new StringBuilder();
		StringBuilder word = new StringBuilder();
		char[] wordtobeadded;
		int j = 0;
		finalscore = 0;
		char[] eachtext = text.toCharArray();
		char characterInTweet;
		for (int i = 0; i < text.length(); i++) {
			characterInTweet = eachtext[i];
			if (Character.isLetter(characterInTweet)
					|| Character.isDigit(characterInTweet)) {
				word.append(characterInTweet);
			} else if (word.length() != 0) {
				if (bannedWordList.contains(word.toString().toLowerCase())) {
					wordtobeadded = word.toString().toCharArray();
					tweet.append(wordtobeadded[0]);
					for (j = 1; j < word.toString().length() - 1; j++) {
						tweet.append("*");
					}
					tweet.append(wordtobeadded[j]);
					tweet.append(eachtext[i]);
					finalscore += getScore(word.toString().toLowerCase());
					word.setLength(0);
				}
				else {
					tweet.append(word.toString());
					finalscore += getScore(word.toString().toLowerCase());
					word.setLength(0);
					tweet.append(eachtext[i]);
				}
			} else {
				tweet.append(eachtext[i]);
			}
		}
		if (word.length() != 0) {
			if (bannedWordList.contains(word.toString().toLowerCase())) {
				wordtobeadded = word.toString().toCharArray();
				tweet.append(wordtobeadded[0]);
				for (j = 1; j < word.toString().length() - 1; j++) {
					tweet.append("*");
				}
				tweet.append(wordtobeadded[j]);
				finalscore += getScore(word.toString().toLowerCase());
				word.setLength(0);
			} else {
				tweet.append(word.toString());
				finalscore += getScore(word.toString().toLowerCase());
				word.setLength(0);
			}

		}
		return tweet.toString();
	}

	public static boolean checkTwitterDate(String date) throws ParseException {
		final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);
		Date tweetDate = sf.parse(date);
		Date stdDate = sf.parse("Sun Apr 20 00:00:00 +0000 2014");
		if(tweetDate.after(stdDate))
			return true;
		else
			return false;
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
		StringBuilder outputTweetDetails = new StringBuilder();

		try {
			readSentimentsFromFile();
			readCensoredFromFile();

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			String inputTweetDetails;
			Tweet tweet;
			JsonObject jsonTweet;
			String tweetId;
			String timeStamp;
			String formattedTimeStamp;
			String userId;
			String javaEscapedTweet;
			String transformedTweet;

			while ((inputTweetDetails=reader.readLine()) != null) {
				try {
					outputTweetDetails.setLength(0);
					tweet = gson.fromJson(inputTweetDetails, Tweet.class);
					jsonTweet = new JsonParser().parse(inputTweetDetails).getAsJsonObject();

					timeStamp = tweet.created_at.trim();
					if(timeStamp==null || timeStamp.length()==0) {
						continue;
					}

					if(checkTwitterDate(timeStamp) == true)	{
						userId = jsonTweet.get("user").getAsJsonObject().get("id_str").getAsString();
						userId = userId.trim();
						if(userId == null || userId.length() == 0) {
							continue;
						}

						tweetId = tweet.id.trim();
						if(tweetId==null || tweetId.length()==0) {
							continue;
						}
						
						formattedTimeStamp = convertDateFormat(timeStamp);

						javaEscapedTweet = StringEscapeUtils.escapeJava(tweet.text);
						transformedTweet = removeBannedPartsTransform(javaEscapedTweet);

						outputTweetDetails.append(userId);

						outputTweetDetails.append("#");
						outputTweetDetails.append(tweetId);

						outputTweetDetails.append("\t");
						outputTweetDetails.append(formattedTimeStamp);

						outputTweetDetails.append("\t");					
						outputTweetDetails.append(String.valueOf(finalscore));

						outputTweetDetails.append("\t");					
						outputTweetDetails.append(transformedTweet);


						System.out.println(outputTweetDetails.toString());
					} // end if
				} catch(Exception e) {
					//System.out.println(e.getMessage());
				}
			} // end while
			reader.close();
		} catch(IOException io){
			io.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
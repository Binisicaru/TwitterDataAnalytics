import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Solution {
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
		try {
			System.out.println(convertDateFormat("Fri Mar 21 17:35:09 +0000 2014"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
//		String urlContent = "ec2-52-0-154-146.compute-1.amazonaws.com=651.72";
//		System.out.println(urlContent.split("=")[1]);
//		String[] urlContentParts = urlContent.split("\\.");
//		System.out.println(urlContentParts.length);
//		for(int partCounter=0; partCounter<urlContentParts.length; partCounter++) {
//			System.out.println("Part " + partCounter + ":" + urlContentParts[partCounter]);
//		}
	}
}

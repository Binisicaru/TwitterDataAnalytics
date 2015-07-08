import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnifiedModel {
	
	// Logic for Query 1
	public String decodeMessage(String httpRequest) {
		int keyParamIndex = httpRequest.indexOf("key");
		int messageParamIndex = httpRequest.indexOf("&message");
		
		String key = httpRequest.substring(keyParamIndex+4, messageParamIndex);
		String message = httpRequest.substring(messageParamIndex+9);
		
		BigInteger XY = new BigInteger(key);
		return decode(message,XY);
	}
	
	private String decode(String message, BigInteger XY) {
		String decodedMessage;
		
		int length = message.length();
		int squareRootLength = (int) Math.sqrt(length);
		
		char[][] arr = new char[squareRootLength][squareRootLength];
		int messageIndex = 0;
		char[] tempString = new char[length];
		char[] decodedArray = new char[length];
		int startRowIndex = 0;
		int startColmnIndex = 0;
		int endingRowIndex = squareRootLength;
		int endingColumnIndex = squareRootLength;
		int loopIterator;
		BigInteger X = new BigInteger("8271997208960872478735181815578166723519929177896558845922250595511921395049126920528021164569045773");
		BigInteger Y = XY.divide(X);

		BigInteger YMod25 = Y.mod(new BigInteger("25"));
		int YPlus1 = YMod25.intValue() + 1;

		for(int counterOne = 0; counterOne < squareRootLength; counterOne++) {
			for(int internalCounter = 0; internalCounter < squareRootLength; internalCounter++) {
				arr[counterOne][internalCounter] = message.charAt(messageIndex++);
			}
		}
		messageIndex = 0;

		while(startRowIndex < endingRowIndex && startColmnIndex < endingColumnIndex) {
			for (loopIterator = startColmnIndex; loopIterator < endingColumnIndex; ++loopIterator) {
				tempString[messageIndex++] = arr[startRowIndex][loopIterator];
			}
			startRowIndex++;

			for (loopIterator = startRowIndex; loopIterator < endingRowIndex; ++loopIterator) {
				tempString[messageIndex++] = arr[loopIterator][endingColumnIndex-1];
			}
			endingColumnIndex--;

			if(startRowIndex < endingRowIndex) {
				for (loopIterator = endingColumnIndex-1; loopIterator >= startColmnIndex; --loopIterator) {
					tempString[messageIndex++] = arr[endingRowIndex-1][loopIterator];
				}
				endingRowIndex--;
			}

			if(startColmnIndex < endingColumnIndex) {
				for (loopIterator = endingRowIndex-1; loopIterator >= startRowIndex; --loopIterator) {
					tempString[messageIndex++] = arr[loopIterator][startColmnIndex];
				}
				startColmnIndex++;    
			}        
		}
		for(loopIterator = 0; loopIterator < length; loopIterator++) {
			if(tempString[loopIterator]-'A'+1 <= YPlus1) {
				int temp = tempString[loopIterator]-'A'+1;
				int offset = temp - YPlus1;
				decodedArray[loopIterator] = (char) ('Z' + offset);
			}
			else
				decodedArray[loopIterator] = (char) (tempString[loopIterator] - YPlus1);
		}
		decodedMessage = new String(decodedArray);
		return decodedMessage;
	}
	
	// Logic for Query 2
	public boolean isTweetDateValid(String date) throws ParseException {
		final String TWITTER="yyyy-MM-dd+HH:mm:ss";
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);
		Date tweetDate = sf.parse(date);
		Date stdDate = sf.parse("2014-04-20+00:00:00");
		if(tweetDate.after(stdDate)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String extractUserID(String requestAnalysis) {
		String[] requestParts = requestAnalysis.split("&");
		String userID = requestParts[0].split("=")[1];
		return userID;
	}
	
	public String extractTime(String requestAnalysis) {
		String[] requestParts = requestAnalysis.split("&");
		String tweetTimestamp = requestParts[1].split("=")[1];
		tweetTimestamp = tweetTimestamp.replaceAll("%20", " ");
		return tweetTimestamp;
	}
	
	// Logic for Query 4
	public String extractHashtag(String requestAnalysis) {
		String hashtag;
		String[] requestParts = requestAnalysis.split("&");
		String[] hashtagParts = requestParts[0].split("=");
		hashtag = hashtagParts[1];
		return hashtag;
	}

	public String extractStartTimeStamp (String requestAnalysis) {
		String startTimeStamp;
		String[] requestParts = requestAnalysis.split("&");
		String[] startTimeStampParts = requestParts[1].split("=");
		startTimeStamp = startTimeStampParts[1];
		startTimeStamp = startTimeStamp.replaceAll("%20", " ");
		return startTimeStamp;
	}
	
	public String extractEndTimeStamp (String requestAnalysis) {
		String endTimeStamp;
		String[] requestParts = requestAnalysis.split("&");
		String[] endTimeStampParts = requestParts[2].split("=");
		endTimeStamp = endTimeStampParts[1];
		endTimeStamp = endTimeStamp.replaceAll("%20", " ");
		return endTimeStamp;
	}
	
	// Logic for Query 5
	public String extractUserList(String requestAnalysis) {
		String userlist = null;
		String[] requestParts = requestAnalysis.split("&");
		String[] userListParts = requestParts[0].split("=");
		userlist = userListParts[1];
		return userlist;
	}
	
	// Logic for Query 6
	public BigDecimal extractM(String requestAnalysis) {
		String[] requestParts = requestAnalysis.split("&");
		String[] mParts = requestParts[0].split("=");
		BigDecimal m = new BigDecimal(mParts[1]);
		return m;
	}
	
	public BigDecimal extractN(String requestAnalysis) {
		String[] requestParts = requestAnalysis.split("&");
		String[] nParts = requestParts[1].split("=");
		BigDecimal n = new BigDecimal(nParts[1]);
		return n;
	}
}

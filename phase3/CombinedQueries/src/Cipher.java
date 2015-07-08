import java.math.BigInteger;

public class Cipher {
	String decodedMessage;

	public Cipher() {
		decodedMessage = null;
	}

	public String decode(String initial_message, BigInteger XY) {
		int length = initial_message.length();
		int sq_length = (int) Math.sqrt(length);
		char[][] arr = new char[sq_length][sq_length];
		int messageIndex = 0;
		char[] tempString = new char[length];
		char[] decodedArray = new char[length];
		int k = 0, l = 0, m = sq_length, n = sq_length, i;
		BigInteger X = new BigInteger("8271997208960872478735181815578166723519929177896558845922250595511921395049126920528021164569045773");
		BigInteger Y = XY.divide(X);

		BigInteger YMod25 = Y.mod(new BigInteger("25"));
		int YPlus1 = YMod25.intValue() + 1;

		for(int counterOne = 0; counterOne < sq_length; counterOne++) {
			for(int internalCounter = 0; internalCounter < sq_length; internalCounter++) {
				arr[counterOne][internalCounter] = initial_message.charAt(messageIndex++);
			}
		}
		messageIndex = 0;

		while (k < m && l < n) {
			for (i = l; i < n; ++i) {
				tempString[messageIndex++] = arr[k][i];
			}
			k++;

			for (i = k; i < m; ++i) {
				tempString[messageIndex++] = arr[i][n-1];
			}
			n--;

			if ( k < m) {
				for (i = n-1; i >= l; --i) {
					tempString[messageIndex++] = arr[m-1][i];
				}
				m--;
			}

			if (l < n) {
				for (i = m-1; i >= k; --i)
					tempString[messageIndex++] = arr[i][l];
				l++;    
			}        
		}
		for(i = 0; i < length; i++) {
			if(tempString[i]-'A'+1 <= YPlus1) {
				int temp = tempString[i]-'A'+1;
				int offset = temp - YPlus1;
				decodedArray[i] = (char) ('Z' + offset);
			}
			else
				decodedArray[i] = (char) (tempString[i] - YPlus1);
		}
		decodedMessage = new String(decodedArray);
		return decodedMessage;
	}

	public String getDecodeMessage(String httpReq){
		int keyParamIndex = httpReq.indexOf("key");
		int messageParamIndex = httpReq.indexOf("&message");

		String key = httpReq.substring(keyParamIndex+4, messageParamIndex);
		String message = httpReq.substring(messageParamIndex+9);
		//System.out.println("Key: " + key);
		//System.out.println("Message: " + message);
		BigInteger XY = new BigInteger(key);
		return decode(message,XY);
	}
}
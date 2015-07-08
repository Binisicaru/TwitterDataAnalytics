import java.math.BigInteger;

public class Cipher {
	String final_message;

	public Cipher(){
		final_message = null;
	}

	public String decode(String initial_message, BigInteger XY){
		int length = initial_message.length();
		int sq_length = (int) Math.sqrt(length);
		char[][] arr = new char[sq_length][sq_length];
		int message_index = 0;
		char[] intermediate = new char[length];
		char[] final_array = new char[length];
		int k = 0, l = 0, m = sq_length, n = sq_length, i;
		BigInteger X = new BigInteger("8271997208960872478735181815578166723519929177896558845922250595511921395049126920528021164569045773");
		BigInteger Y = XY.divide(X);

		BigInteger YMod25 = Y.mod(new BigInteger("25"));
		int YPlus1 = YMod25.intValue() + 1;

		for(int i1 = 0; i1 < sq_length; i1++)
		{
			for(int j = 0; j < sq_length; j++)
			{
				arr[i1][j] = initial_message.charAt(message_index++);
			}
		}
		message_index = 0;

		while (k < m && l < n)
		{
			for (i = l; i < n; ++i)
			{
				intermediate[message_index++] = arr[k][i];
			}
			k++;

			for (i = k; i < m; ++i)
			{
				intermediate[message_index++] = arr[i][n-1];
			}
			n--;

			if ( k < m)
			{
				for (i = n-1; i >= l; --i)
				{
					intermediate[message_index++] = arr[m-1][i];
				}
				m--;
			}

			if (l < n)
			{
				for (i = m-1; i >= k; --i)
				{
					intermediate[message_index++] = arr[i][l];
				}
				l++;    
			}        
		}

		for(i = 0; i < length; i++)
		{
			if(intermediate[i]-'A'+1 <= YPlus1)
			{
				int temp = intermediate[i]-'A'+1;
				int offset = temp - YPlus1;
				final_array[i] = (char) ('Z' + offset);
			}
			else
			{
				final_array[i] = (char) (intermediate[i] - YPlus1);
			}
		}
		final_message = new String(final_array);
		return final_message;
	}

	public String parseHttpReq(String httpReq){
		System.out.println("################### Received Request ::" + httpReq);
		int key_index = httpReq.indexOf("key");
		int message_index = httpReq.indexOf("&message");
		
		String key_str = httpReq.substring(key_index + 4, message_index);
		String message = httpReq.substring(message_index+9);
		BigInteger XY = new BigInteger(key_str);
		String result = decode(message,XY);
		return result;
	}
}
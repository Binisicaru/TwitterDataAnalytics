import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.lang3.StringEscapeUtils;
public class JoinReducer{
	public static void main(String[] args) {		
				
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
			String input;
			String[] str_arr;
			Boolean first_line = true;
			String prev = null;
			String first_column = null;
			String second_column = null;
			while((input = br.readLine()) != null){			
				
				str_arr = input.split("\t");
				if(first_line == true)
				{
					prev = str_arr[0];
					first_column = str_arr[0];
					second_column = str_arr[1];
					first_line = false;
				}
				else
				{
					if(str_arr[0].equals(prev))
					{
						second_column = second_column + StringEscapeUtils.escapeJava("\n" + str_arr[1]);
					}
					else
					{
						System.out.println(first_column + "\t" + second_column);
						first_column = str_arr[0];
						second_column = str_arr[1];
						prev = str_arr[0];
					}
				}
			}
			System.out.println(first_column + "\t" + second_column);
		}
		catch(IOException io){
			io.printStackTrace();
		}
	}
}

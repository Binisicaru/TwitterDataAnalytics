import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reducer {

	public static void main(String[] args) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input = "";
			String hashTag = "";
			String ts = "";
			String[] arr;
			String str;
			Boolean first_line = true;
			String prev = null;
			String first_column = "";

			while((input = br.readLine()) != null){	
			try
			{	
				first_column = (input.split("\t"))[0];
				if(first_line == true)
				{
					first_line = false;
					arr = first_column.split(":");
					hashTag = arr[0];
					prev = first_column;
					ts = ((input.split("\t"))[1].split(","))[2];
					str = (input.split("\t"))[1];
					str = hashTag + "\t" + ts + "\t" + str;
					System.out.println(str);
				}
				else
				{
					if(prev.equals(first_column))
					{
						continue;
					}
					else
					{
						arr = (input.split("\t"))[0].split(":");
						hashTag = arr[0];
						ts = ((input.split("\t"))[1].split(","))[2];
						str = (input.split("\t"))[1];
						str = hashTag + "\t" + ts + "\t" + str;
						prev = first_column;
						System.out.println(str);
					}
				}
			}
			catch(Exception e)
			{
				continue;
			}
			}
			br.close();
		}
		catch(IOException io){
			io.printStackTrace();
		}
	}

}

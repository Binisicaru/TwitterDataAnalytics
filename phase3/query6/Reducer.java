import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reducer {

	public static void main(String[] args) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Harini\\Desktop\\q6test.txt"));
			String input = "";
			Boolean first_line = true;
			Boolean location = false;
			String prev = null;
			String first_column = "";
			String second_column = "";
			
			while((input = br.readLine()) != null){	
				first_column = (input.split("\t"))[0];
				second_column = (input.split("\t"))[1];
				if(first_line == true)
				{
					first_line = false;
					prev = first_column;
					if(second_column.equals("0"))
						location = true;
				}
				else
				{
					if(prev.equals(first_column))
					{
						if(location == true)
							continue;
						else if(second_column.equals("0"))
							location = true;
					}
					else
					{
						if(location == true)
						{
							location = false;
						}
						else
						{
							System.out.println(prev);
						}
						prev = first_column;
						if(second_column.equals("0"))
							location = true;
					}
				}
			}
			if(location == true)
			{
				location = false;
			}
			else
			{
				System.out.println(prev);
			}
		}
		catch(IOException io){
			io.printStackTrace();
		}
	}

}
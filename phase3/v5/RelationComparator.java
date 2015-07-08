import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RelationComparator implements Comparator<String> {
	List<String> retweetDetails = new ArrayList<>();
	String[] retweetParts1;
	String[] retweetParts2;
	
	String relationOperator1 = "";
	int relationCount1 = 0;
	long retweetID1;
	
	String relationOperator2 = "";
	int relationCount2 = 0;
	long retweetID2;
	
	public RelationComparator(List<String> retweetDetails) {
		this.retweetDetails = retweetDetails;
	}
	
	@Override
	public int compare(String retweetDetail1, String retweetDetail2) {
		int relationComparison = 0;
		retweetParts1 = retweetDetail1.split(",");
		relationOperator1 = retweetParts1[0];
		relationCount1 = Integer.parseInt(retweetParts1[1]);
		retweetID1 = Long.parseLong(retweetParts1[2]);
		
		retweetParts2 = retweetDetail2.split(",");
		relationOperator2 = retweetParts2[0];
		relationCount2 = Integer.parseInt(retweetParts2[1]);
		retweetID2 = Long.parseLong(retweetParts2[2]);
		
		relationComparison = relationOperator1.compareTo(relationOperator2);
		if(relationComparison == 0) {
			relationComparison = relationCount2 - relationCount1;
			if(relationComparison == 0) {
				if(retweetID1 > retweetID2) {
					relationComparison = 1; 
				} else {
					relationComparison = -1;
				}
			}
		}
		return relationComparison;
	}
}
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;

public class UnifiedCache {
	static Map<String, String> cache2 = new LinkedHashMap<String, String>(100000, 1f) {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999;
		}
	};
	
	static Map<String, String> cache6 = new LinkedHashMap<String, String>(100000000, 0.75f) {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999999;
		}
	};
	
//	static Set<Long> cache62 = new NavigableSet<Long>() {
//	}; 
	
	public String getCache2Data(String userID, String tweettimestamp) {
		String tweetDataOutput = "";
		String dataretrieve= userID + "#" + tweettimestamp;
		if(cache2.containsKey(dataretrieve)) {
			tweetDataOutput = cache2.get(dataretrieve);
		}
		return tweetDataOutput;
	}
	
	public void addToCache2(String userID, String tweet) {
		cache2.put(userID, tweet);
	}
	
	public void addToCache6(String userID, String tweet) {
		cache6.put(userID, tweet);
	}
}

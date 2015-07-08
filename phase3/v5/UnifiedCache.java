import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class UnifiedCache {
	static Map<String, String> cache2 = new LinkedHashMap<String, String>(100000, 1f) {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999;
		}
	};
	
	static Map<String, String> cache3 = new LinkedHashMap<String, String>(100000, 1f) {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999;
		}
	};
	
	static Map<String, String> cache4 = new LinkedHashMap<String, String>(100000, 1f) {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999;
		}
	};
	
	static Map<String, String> cache5 = new LinkedHashMap<String, String>(100000, 1f) {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return size() > 99999;
		}
	};
	
//	static NavigableSet<BigDecimal> cache6 = new TreeSet<>();
	static NavigableSet<Long> cache6 = new TreeSet<>();
	
//	public UnifiedCache(UnifiedDataAccess unifiedDataAccess) {
//		//cache6 = unifiedDataAccess.getHermits();
//	}
	
	public void initializeCaches(NavigableSet<Long> cache6) {
		UnifiedCache.cache6 = cache6;
	}
	
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
	
//	public int getHermitCount(BigDecimal startID, BigDecimal endID) {
//		int hermitCount = cache6.subSet(startID, true, endID, true).size();
//		return hermitCount;
//	}
	
	public int getHermitCount(long startID, long endID) {
		int hermitCount = cache6.subSet(startID, true, endID, true).size();
		return hermitCount;
	}
	
//	public void addToCache6(BigDecimal userID) {
//		cache6.add(userID);
//	}
	
	public void addToCache6(long userID) {
		cache6.add(userID);
	}
}

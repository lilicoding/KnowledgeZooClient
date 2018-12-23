package edu.monash.neo4j.knowledgezooclient.csv;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AndroZooParser 
{
	public static Map<String, APK> parse(String androzooDataPath, Set<String> sha256FromJson) throws IOException
	{
		Map<String, APK> apks = new HashMap<String, APK>();
		BufferedReader br = new BufferedReader(new FileReader(androzooDataPath));
		Item item = new Item();
		
		String line;
		while ((line = br.readLine()) != null)
		{
			if (line.startsWith("sha256"))
			{
				continue;
			}

			item.parse(line);
			// we only interested in the apks which is in the json
			// this can significantly avoid OutOfMemory exception
			if (!sha256FromJson.contains(item.sha256))
				continue;
			
			APK apk = new APK();
			apk.apkId = item.sha256;
			apk.sha1 = item.sha1;
			apk.md5 = item.md5;
			apk.name = item.sha256;
			apk.apkSize = item.apkSize;
			apk.dexSize = item.dexSize;
			apk.dexDate = item.dexDate;
			apk.dexDateInMillis = item.dexDateTimeInMillis;
			
			String marketStr = item.market;
			
			if (marketStr.contains("|"))
	        {
	        		String[] ss = marketStr.split("\\|");
	        		for (String s : ss)
	        		{
	        			String marketName = s.trim();
	        			
	        			MARKET market = new MARKET();
	        			market.marketId = marketName;
	        			market.name = marketName;
	        			
	        			apk.markets.add(market);
	        		}
	        }
	        else
	        {
		        	String marketName = marketStr.trim();
	    			
	    			MARKET market = new MARKET();
	    			market.marketId = marketName;
	    			market.name = marketName;
	    			
	    			apk.markets.add(market);
	        }
			
			apks.put(apk.apkId, apk);
		}
		
		br.close();
		return apks;
	}
}

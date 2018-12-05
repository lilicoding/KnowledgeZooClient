package edu.monash.neo4j.knowledgezooclient.csv;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AndroZooParser 
{
	public static Map<String, APK> apks = new HashMap<String, APK>();
	
	public static void parse(String androzooDataPath) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(androzooDataPath));
		
		String line;
		while ((line = br.readLine()) != null)
		{
			if (line.startsWith("sha256"))
			{
				continue;
			}
			
			Item item = new Item(line);
			
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
	}
}

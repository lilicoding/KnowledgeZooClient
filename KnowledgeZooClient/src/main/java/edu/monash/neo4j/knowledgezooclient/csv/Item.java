package edu.monash.neo4j.knowledgezooclient.csv;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Item 
{
	public String sha256;
	public String sha1;
	public String md5;
	public String dexDate;
	public int apkSize;
	public String pkgName;
	public int versionCode;
	public int vtCount;
	public String vtScanDate;
	public int dexSize;
	public String market;
	
	public long dexDateTimeInMillis;
	public long vtScanDateTimeInMillis;

	public void parse(String line)
	{
		String[] strs = line.split(",");
		sha256 = strs[0];
		sha1 = strs[1];
		md5 = strs[2];
		dexDate = strs[3];
		apkSize = Integer.parseInt(strs[4]);
		pkgName = strs[5].replace("\"", "");
		
		if (strs[6].isEmpty())
		{
			versionCode = -1;
		}
		else
		{
			versionCode = Integer.parseInt(strs[6]);
		}
		
		if (strs[7].isEmpty())
		{
			vtCount = -1;
		}
		else
		{
			vtCount = Integer.parseInt(strs[7]);
		}
		
		vtScanDate = strs[8];
		dexSize = Integer.parseInt(strs[9]);
		market = strs[10];
		
		dexDateTimeInMillis = date2long(dexDate);
		
		if (! vtScanDate.isEmpty())
		{
			vtScanDateTimeInMillis = date2long(vtScanDate);	
		}
	}

	@Override
	public String toString() 
	{
		return sha256 + "," + 
			sha1 + "," + 
			md5 + "," + 
			dexDate + "," +
			apkSize + "," + 
			pkgName + "," + 
			versionCode + "," + 
			vtCount + "," + 
			vtScanDate + "," + 
			dexSize + "," + 
			market;
	}


	public static long date2long(String date)
	{
		Calendar c = Calendar.getInstance();
		
		try
		{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			c.setTime(format.parse(date));
			
			return c.getTimeInMillis();
		}
		catch (ParseException ex)
		{
			return -1;
		}

		
	}
}

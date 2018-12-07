package edu.monash.neo4j.knowledgezooclient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.monash.neo4j.knowledgezooclient.cypher.ApkInfo;
import edu.monash.neo4j.knowledgezooclient.cypher.IntentFilterInfo;

public class MetadataParser 
{
	public static Map<String, ApkInfo> apkInfos;

	static Gson gson = new GsonBuilder().create();
	
	public static void parse(String inputJsonPath) throws FileNotFoundException
	{
		InputStream is = new FileInputStream(inputJsonPath);
		InputStreamReader reader = new InputStreamReader(is);
		
		TypeToken<Map<String, ApkInfo>> token = new TypeToken<Map<String, ApkInfo>>() {};
		Type listType = token.getType();
		
		apkInfos = gson.fromJson(reader, listType);
	}
	
	public static Map<String, ApkInfo> parseJson(String inputJson) {
		TypeToken<Map<String, ApkInfo>> token = new TypeToken<Map<String, ApkInfo>>() {};
		Type listType = token.getType();
		
		apkInfos = gson.fromJson(inputJson, listType);
		return apkInfos;
	}
	
	public static IntentFilterInfo parseIntentFilter(String intentFilterStr)
	{
		 return gson.fromJson(intentFilterStr, IntentFilterInfo.class);
	}
}

package edu.monash.neo4j.knowledgezooclient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.monash.neo4j.knowledgezooclient.cypher.ApkInfo;
import edu.monash.neo4j.knowledgezooclient.cypher.IntentFilterInfo;

public class MetadataParser 
{
	static Gson gson = new GsonBuilder().create();
	
	public static Map<String, ApkInfo> parse(String inputJsonPath) throws FileNotFoundException
	{
		InputStream is = new FileInputStream(inputJsonPath);
		InputStreamReader reader = new InputStreamReader(is);
		
		TypeToken<Map<String, ApkInfo>> token = new TypeToken<Map<String, ApkInfo>>() {};
		Type listType = token.getType();
		
		Map<String, ApkInfo> apkInfos = gson.fromJson(reader, listType);
		// json lack of "sha256" key. add it manually
		for (Entry<String, ApkInfo> e : apkInfos.entrySet()) {
			e.getValue().sha256 = e.getKey();
		}
		return apkInfos;
	}
	
	public static Map<String, ApkInfo> parseJson(String inputJson) {
		TypeToken<Map<String, ApkInfo>> token = new TypeToken<Map<String, ApkInfo>>() {};
		Type listType = token.getType();
		
		Map<String, ApkInfo> apkInfos = gson.fromJson(inputJson, listType);
		// json lack of "sha256" key. add it manually
		for (Entry<String, ApkInfo> e : apkInfos.entrySet()) {
			e.getValue().sha256 = e.getKey();
		}
		return apkInfos;
	}
	
	public static IntentFilterInfo parseIntentFilter(String intentFilterStr)
	{
		 return gson.fromJson(intentFilterStr, IntentFilterInfo.class);
	}
}

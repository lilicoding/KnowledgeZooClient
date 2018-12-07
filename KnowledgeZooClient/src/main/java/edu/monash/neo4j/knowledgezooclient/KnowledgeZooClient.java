package edu.monash.neo4j.knowledgezooclient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.monash.neo4j.knowledgezooclient.csv.CsvHeader;
import edu.monash.neo4j.knowledgezooclient.csv.CsvImportClient;
import edu.monash.neo4j.knowledgezooclient.cypher.ApkInfo;
import edu.monash.neo4j.knowledgezooclient.cypher.KnowledgeZooCypherBuilder;
import edu.monash.neo4j.knowledgezooclient.util.CommonUtils;

public class KnowledgeZooClient 
{
	static Map<String, ApkInfo> apkInfos;
	
	public static void main(String[] args) throws IOException
	{
		Config.load();
		
		if (args.length == 1)
		{
			String option = args[0];
			if ("-csvheader".equals(option))
			{
				CsvHeader.generateHeaderCsvFiles();
			}
			else
			{
				showErrorMessage();
			}
		}
		else if (args.length == 2)
		{
			String option = args[0];
			String inputJsonPath = args[1];

			MetadataParser.parse(inputJsonPath);
			apkInfos = MetadataParser.apkInfos;
			
			if ("-csv".equals(option))
			{
				CsvImportClient.main(new String[] {Config.androZooLatestCsvPath, inputJsonPath, Config.knowledgeZooCsvPath});
			}
			else if ("-cypher".equals(option))
			{
				for (String sha256 : apkInfos.keySet())
				{
					ApkInfo info = apkInfos.get(sha256);
					String script = KnowledgeZooCypherBuilder.parseApk(info);
					
					CommonUtils.writeResultToFile(Config.knowledgeZooCypherPath + File.separator + CommonUtils.getFileName(inputJsonPath).replace(".json", ".txt"), script);
				}
			}
			else
			{
				showErrorMessage();
			}
		}
		else
		{
			showErrorMessage();
		}
	}
	
	private static void showErrorMessage()
	{
		throw new RuntimeException("Parameter Error, Usage: -csv (or -cypher) inputJsonPath");
	}
	
	// public API
	
	/**
	 * parse json and conver it to cypher scripts. json contains metadata of multiple apks. each apk has an identifier called sha256.
	 * each apk requires multiple scripts(stored in a list), which can be used to create this apk info in the database.
	 * @param json contains metadata of multiple apks
	 * @return Map<sha256, scripts>
	 */
	public static Map<String, List<String>> convertJsonToScripts(String json) {
		// parse json to ApkInfo structure
		apkInfos = MetadataParser.parseJson(json);
		// convert ApkInfo to scripts
		Map<String, List<String>> scripts = new HashMap<>(apkInfos.size());
		for (Entry<String, ApkInfo> apk : apkInfos.entrySet()) {
			String sha256 = apk.getKey();
			ApkInfo info = apk.getValue();
			List<String> script = KnowledgeZooCypherBuilder.parseApkAsScriptList(info);
			scripts.put(sha256, script);
		}
		return scripts;
	}
}
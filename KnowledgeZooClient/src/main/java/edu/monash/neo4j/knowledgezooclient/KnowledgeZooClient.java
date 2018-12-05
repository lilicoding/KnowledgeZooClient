package edu.monash.neo4j.knowledgezooclient;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
	
	public static void showErrorMessage()
	{
		throw new RuntimeException("Parameter Error, Usage: -csv (or -cypher) inputJsonPath");
	}
}
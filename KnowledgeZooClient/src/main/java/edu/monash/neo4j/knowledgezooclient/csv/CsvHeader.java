package edu.monash.neo4j.knowledgezooclient.csv;

import java.io.File;

import edu.monash.neo4j.knowledgezooclient.Config;
import edu.monash.neo4j.knowledgezooclient.util.CommonUtils;

public class CsvHeader 
{
	public static final String apkHeader = "apkId:ID,name,sha1,md5,packageName,versionCode,versionName,sdkVersion:int,mainActivity,usesNativeCode:boolean,usesDynamicCode:boolean,usesReflection:boolean,usesObfuscation:boolean,dexDate,dexDateInMillis:long,dexSize:long,apkSize:long,:LABEL";
	public static final String permHeader = "permId:ID,name,:LABEL";
	public static final String pkgHeader = "apkId:ID,name,:LABEL";
	public static final String certHeader = "certId:ID,name,certOwner,:LABEL";
	public static final String marketHeader = "marketId:ID,name,:LABEL";
	public static final String compHeader = "compId:ID,name,compType,:LABEL";
	

	public static final String actionHeader = "actionId:ID,name,:LABEL";
	public static final String categoryHeader = "categoryId:ID,name,:LABEL";
	
	public static final String relationshipHeader = ":START_ID,:END_ID,:TYPE";
	
	public static void generateHeaderCsvFiles()
	{
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "node_apk_header.csv", apkHeader);
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "node_perm_header.csv", permHeader);
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "node_pkg_header.csv", pkgHeader);
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "node_cert_header.csv", certHeader);
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "node_market_header.csv", marketHeader);
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "node_comp_header.csv", compHeader);
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "node_action_header.csv", actionHeader);
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "node_category_header.csv", categoryHeader);
		
		CommonUtils.writeResultToFile(Config.knowledgeZooCsvPath + File.separator + "relationship_all_header.csv", relationshipHeader);
	}
	
	public static void main(String[] args)
	{
		Config.load();
		
		generateHeaderCsvFiles();
	}
}

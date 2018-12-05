package edu.monash.neo4j.knowledgezooclient;

import java.util.Set;

import edu.monash.neo4j.knowledgezooclient.util.CommonUtils;

public class Config
{
	public static String configFile = "neo4j.config";
	
	public static String url = "";
	public static String username = "";
	public static String password = "";
	
	public static String androZooLatestCsvPath = "";
	public static String knowledgeZooCsvPath = "";
	public static String knowledgeZooCypherPath = "";
	
	public final static String URL_TAG = "URL";
	public final static String USERNAME_TAG = "USERNAME";
	public final static String PASSWORD_TAG = "PASSWORD";
	
	public final static String AndroZooLatestCsvPath_TAG = "AndroZooLatestCsvPath";
	public final static String KnowledgeZooCsvPath_TAG = "KnowledgeZooCsvPath";
	public final static String KnowledgeZooCypherPath_TAG = "KnowledgeZooCypherPath";
	
    public static void load()
    {
    		Set<String> lines = CommonUtils.loadFile(configFile);
    		for (String line : lines)
    		{
    			if (line.isEmpty())
    			{
    				continue;
    			}
    			
    			String[] strs = line.split("=");
    			
    			if (URL_TAG.equals(strs[0].trim()))
    			{
    				url = strs[1].trim();
    			}
    			else if (USERNAME_TAG.equals(strs[0].trim()))
    			{
    				username = strs[1].trim();
    			}
    			else if (PASSWORD_TAG.equals(strs[0].trim()))
    			{
    				password = strs[1].trim();
    			}
    			else if (AndroZooLatestCsvPath_TAG.equals(strs[0].trim()))
    			{
    				androZooLatestCsvPath = strs[1].trim();
    			}
    			else if (KnowledgeZooCsvPath_TAG.equals(strs[0].trim()))
    			{
    				knowledgeZooCsvPath = strs[1].trim();
    			}
    			else if (KnowledgeZooCypherPath_TAG.equals(strs[0].trim()))
    			{
    				knowledgeZooCypherPath = strs[1].trim();
    			}
    			else
    			{
    				throw new RuntimeException("Configuration Error!");
    			}
    		}
    }
    
    public static void setConfigFilePath(String configFilePath)
    {
    		configFile = configFilePath;
    }
}

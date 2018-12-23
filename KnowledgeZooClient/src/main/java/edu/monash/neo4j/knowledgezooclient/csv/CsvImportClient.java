package edu.monash.neo4j.knowledgezooclient.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import edu.monash.neo4j.knowledgezooclient.MetadataParser;
import edu.monash.neo4j.knowledgezooclient.cypher.ApkInfo;
import edu.monash.neo4j.knowledgezooclient.cypher.IntentFilterInfo;
import edu.monash.neo4j.knowledgezooclient.util.CommonUtils;

public class CsvImportClient 
{
	static Map<String, APK> apks;
	static Map<String, ApkInfo> apkInfos;
	static String csvPath;
	
	public static void main(String[] args)
	{
		String latestCsvPath = args[0];
		String inputJsonPath = args[1];
		csvPath = args[2];
		
		try 
		{
			apkInfos = MetadataParser.parse(inputJsonPath);
			apks = AndroZooParser.parse(latestCsvPath, apkInfos.keySet());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		modelConstruction();
		model2csv();
		
		apkInfos = null;
		apks = null;
	}
	
	public static void model2csv()
	{
		StringBuilder apkSB = new StringBuilder();
		StringBuilder certSB = new StringBuilder();
		StringBuilder permSB = new StringBuilder();
		StringBuilder pkgSB = new StringBuilder();
		StringBuilder marketSB = new StringBuilder();
		StringBuilder compSB = new StringBuilder();
		
		StringBuilder actionSB = new StringBuilder();
		StringBuilder categorySB = new StringBuilder();
		
		StringBuilder relationshipSB = new StringBuilder();
		
		for (String sha256 : apkInfos.keySet())
		{
			if (! apks.containsKey(sha256))
			{
				continue;
			}
			
			APK apk = apks.get(sha256);
			
			apkSB = appendNode(apkSB, apk.toString());
			
			certSB = appendNode(certSB, apk.cert.toString());
			
			//relationshipSB.append(apk.apkId + ",\"" + apk.name + "\"," + apk.cert.certId + "," + "SignedBy" + "\n");
			relationshipSB = appendRelationship(relationshipSB, apk.apkId, apk.cert.certId, "SignedBy");
			
			for (PERM p : apk.perms)
			{
				permSB = appendNode(permSB, p.toString());
				
				//relationshipSB.append(apk.apkId + ",\"" + apk.name + "\"," + p.permId + "," + "Has" + "\n");
				relationshipSB = appendRelationship(relationshipSB, apk.apkId, p.permId, "Has");
			}
			
			for (PKG p : apk.pkgs)
			{
				pkgSB = appendNode(pkgSB, p.toString());
				
				//relationshipSB.append(apk.apkId + ",\"" + apk.name + "\"," + p.pkgId + "," + "Has" + "\n");
				relationshipSB = appendRelationship(relationshipSB, apk.apkId, p.pkgId, "Has");
			}
			
			for (MARKET m : apk.markets)
			{
				marketSB = appendNode(marketSB, m.toString());
				
				//relationshipSB.append(apk.apkId + ",\"" + apk.name + "\"," + m.marketId + "," + "From" + "\n");
				relationshipSB = appendRelationship(relationshipSB, apk.apkId, m.marketId, "From");
			}
			
			for (COMP c : apk.comps)
			{
				compSB = appendNode(compSB, c.toString());
				
				//relationshipSB.append(apk.apkId + ",\"" + apk.name + "\"," + c.compId + "," + "Has" + "\n");
				relationshipSB = appendRelationship(relationshipSB, apk.apkId, c.compId, "Has");
				
				for (ACTION a : c.actions)
				{
					actionSB = appendNode(actionSB, a.toString());
					
					//relationshipSB.append(c.compId + ",\"" + c.name + "\"," + a.actionId + "," + "Has" + "\n");
					relationshipSB = appendRelationship(relationshipSB, c.compId, a.actionId, "Has");
				}
				
				for (CATEGORY ca : c.categories)
				{
					categorySB = appendNode(categorySB, ca.toString());
					

					//relationshipSB.append(c.compId + ",\"" + c.name + "\"," + ca.categoryId + "," + "Has" + "\n");
					relationshipSB = appendRelationship(relationshipSB, c.compId, ca.categoryId, "Has");
				}
			}
			
			// write csv of this apk into file, to avoid over-sized StringBuilder
			CommonUtils.writeResultToFile(csvPath + File.separator + "node_" + "apk.csv", apkSB.toString(), true);
			CommonUtils.writeResultToFile(csvPath + File.separator + "node_" + "cert.csv", certSB.toString(), true);
			CommonUtils.writeResultToFile(csvPath + File.separator + "node_" + "perm.csv", permSB.toString(), true);
			CommonUtils.writeResultToFile(csvPath + File.separator + "node_" + "pkg.csv", pkgSB.toString(), true);
			CommonUtils.writeResultToFile(csvPath + File.separator + "node_" + "market.csv", marketSB.toString(), true);
			CommonUtils.writeResultToFile(csvPath + File.separator + "node_" + "comp.csv", compSB.toString(), true);
			CommonUtils.writeResultToFile(csvPath + File.separator + "node_" + "action.csv", actionSB.toString(), true);
			CommonUtils.writeResultToFile(csvPath + File.separator + "node_" + "category.csv", categorySB.toString(), true);
			
			CommonUtils.writeResultToFile(csvPath + File.separator + "relationship_" + "all.csv", relationshipSB.toString(), true);
			// clear all contents of StringBuilders to let it be able to construct next apk
			apkSB.setLength(0);
			certSB.setLength(0);
			permSB.setLength(0);
			pkgSB.setLength(0);
			marketSB.setLength(0);
			compSB.setLength(0);
			actionSB.setLength(0);
			categorySB.setLength(0);
			relationshipSB.setLength(0);
		}
	}
	
	public static StringBuilder appendNode(StringBuilder sb, String nodeStr)
	{
		if (CommonUtils.isASCIIString(nodeStr))
		{
			sb.append(nodeStr + "\n");
		}
		
		return sb;
	}
	
	public static StringBuilder appendRelationship(StringBuilder sb, String startId, String endId, String type)
	{
		if (! CommonUtils.isEmptyString(startId) && ! CommonUtils.isEmptyString(endId) &&
			CommonUtils.isASCIIString(startId) && CommonUtils.isASCIIString(endId))
		{
			if (! startId.contains(",") && ! endId.contains(","))
			{
				sb.append(startId + "," + endId + "," + type + "\n");
			}
		}
		
		return sb;
	}
	
	public static void modelConstruction()
	{
		for (Map.Entry<String, ApkInfo> entry : apkInfos.entrySet())
		{
			String sha256 = entry.getKey();
			ApkInfo info = entry.getValue();
			
			info.sha256 = sha256;
			
			if (! apks.containsKey(sha256))
			{
				continue;
			}
			
			APK apk = apks.get(sha256);
			apk.mainActivity = info.mainActivity;
			apk.packageName = info.packageName;
			
			apk.usesDynamicCode = info.usesDynamicCode;
			apk.usesNativeCode = info.usesNativeCode;
			apk.usesObfuscation = info.usesObfuscation;
			apk.usesReflection = info.usesReflection;
			apk.versionCode = info.versionCode;
			apk.versionName = info.versionName;
			
			if (! CommonUtils.isEmptyString(info.sdkVersion))
			{
				apk.sdkVersion = Integer.parseInt(info.sdkVersion);
			}
			else
			{
				apk.sdkVersion = -1;
			}
			
			if (null != info.certFingerprint)
			{
				CERT cert = new CERT();
				cert.certId = info.certFingerprint;
				cert.name = info.certFingerprint;
				cert.certOwner = info.certOwner;
				
				apk.cert = cert;
			}
			
			if (null != info.permissions)
			{
				for (String perm : info.permissions)
			    {
					if (perm.isEmpty())
					{
						continue;
					}
					
			    		PERM p = new PERM();
			    		p.permId = perm;
			    		p.name = perm;
			    		
			    		apk.perms.add(p);
			    }
			}
			
			if (null != info.packages)
			{
				for (String pkg : info.packages)
			    {
					if (pkg.isEmpty())
					{
						continue;
					}
					
					PKG p = new PKG();
			    		p.pkgId = pkg;
			    		p.name = pkg;
			    		
			    		apk.pkgs.add(p);
			    }
			}
			
			parseComp(apk, "activity", info.activities);
			parseComp(apk, "service", info.services);
			parseComp(apk, "receiver", info.receivers);
			
			//parseComp(apk, "provider", info.providers);
			if (! CommonUtils.isEmptyString(info.providers))
			{
				String[] providers = info.providers.split(",");
				for (String provider : providers)
				{
					String compName = provider.replaceAll("'", "").trim();
					
					COMP c = new COMP();
					c.compId = compName;
					c.name = compName;
					c.compType = "provider";
					
					apk.comps.add(c);
				}
				
			}
		}
	}
	
	public static void parseComp(APK apk, String compType, ArrayList<String> comps)
	{
		if (null != comps)
		{
			for (String str : comps)
		    {
				String compName = str;
				String filterStr = null;
				
				if (str.contains("{"))
				{
					compName = str.substring(0, str.indexOf('{')).trim();
					filterStr = str.substring(str.indexOf('{'), str.lastIndexOf('}')+1);
				}
				
				COMP c = new COMP();
				c.compId = compName;
				c.name = compName;
				c.compType = compType;
				
				if (null != filterStr)
				{
					IntentFilterInfo filterInfo = MetadataParser.parseIntentFilter(filterStr);
					
		            if (null != filterInfo.action)
		            {
		            		for (String action : filterInfo.action)
		            		{
		            			if (action.isEmpty())
		            			{
		            				continue;
		            			}
		            			
		            			ACTION a = new ACTION();
		            			a.actionId = action;
		            			a.name = action;
		            			
		            			c.actions.add(a);
		            		}
		            }
		            
		            if (null != filterInfo.category)
		            {
		    	            	for (String category : filterInfo.category)
		            		{
		    	            		if (category.isEmpty())
		    	            		{
		    	            			continue;
		    	            		}
		    	            		
	    	            			CATEGORY ca = new CATEGORY();
		            			ca.categoryId = category;
		            			ca.name = category;
		            			
		            			c.categories.add(ca);
		            		}
			            }
				}
				
				apk.comps.add(c);
		    }
		}
	}
}
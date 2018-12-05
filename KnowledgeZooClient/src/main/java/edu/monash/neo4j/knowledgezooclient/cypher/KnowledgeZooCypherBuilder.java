package edu.monash.neo4j.knowledgezooclient.cypher;

import java.util.ArrayList;

import edu.monash.neo4j.knowledgezooclient.MetadataParser;
import edu.monash.neo4j.knowledgezooclient.util.CypherUtils;

public class KnowledgeZooCypherBuilder 
{	
	public static String parseApk(ApkInfo info)
	{
		StringBuilder cypherScripts = new StringBuilder();
		
		cypherScripts.append(CypherUtils.addNode(NodeType.APK.toString(), info.sha256) + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.packageName.toString(), info.packageName) + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.versionCode.toString(), info.versionCode) + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.versionName.toString(), info.versionName) + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.sdkVersion.toString(), info.sdkVersion) + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.mainActivity.toString(), info.mainActivity) + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.usesNativeCode.toString(), info.usesNativeCode + "") + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.usesDynamicCode.toString(), info.usesDynamicCode + "") + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.usesReflection.toString(), info.usesReflection + "") + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.usesObfuscation.toString(), info.usesObfuscation + "") + "\n");
		
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.dexDate.toString(), info.dexDate) + "\n");
		
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.dexSize.toString(), info.dexSize) + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.apkSize.toString(), info.apkSize) + "\n");
        
		cypherScripts.append(CypherUtils.addNode(NodeType.CERT.toString(), info.certFingerprint) + "\n");
		cypherScripts.append(CypherUtils.addAttribute(NodeType.CERT.toString(), info.certFingerprint, NodeAttribute.certOwner.toString(), info.certOwner) + "\n");
		cypherScripts.append(CypherUtils.addRelationship(NodeType.APK.toString(), info.sha256, NodeType.CERT.toString(), info.certFingerprint, "SignedBy") + "\n");
        
        if (null != info.permissions)
        {
        		for (String perm : info.permissions)
	        {
	        		cypherScripts.append(CypherUtils.addNode(NodeType.PERM.toString(), perm) + "\n");
	        		cypherScripts.append(CypherUtils.addRelationship(NodeType.APK.toString(), info.sha256, NodeType.PERM.toString(), perm, "Has") + "\n");
	        }
        }
	        
        if (null != info.packages)
        {
        		for (String pkg : info.packages)
	        {
	        		cypherScripts.append(CypherUtils.addNode(NodeType.PKG.toString(), pkg) + "\n");
	        		cypherScripts.append(CypherUtils.addRelationship(NodeType.APK.toString(), info.sha256, NodeType.PKG.toString(), pkg, "Has") + "\n");
	        }
        }
	        

        cypherScripts.append(parseComp(info.sha256, "activity", info.activities) + "\n");
        cypherScripts.append(parseComp(info.sha256, "service", info.services) + "\n");
        cypherScripts.append(parseComp(info.sha256, "receiver", info.receivers) + "\n");
        //cypherScripts.append(parseComp(info.sha256, "provider", info.providers) + "\n");
        
        return cypherScripts.toString();
	}
	
	public static String parseComp(String sha256, String compType, ArrayList<String> comps)
	{
		StringBuilder cypherScripts = new StringBuilder();
		
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
        			
        			cypherScripts.append(CypherUtils.addNode(NodeType.COMP.toString(), compName) + "\n");
        			cypherScripts.append(CypherUtils.addAttribute(NodeType.COMP.toString(), compName, NodeAttribute.compType.toString(), compType) + "\n");
        			cypherScripts.append(CypherUtils.addRelationship(NodeType.APK.toString(), sha256, NodeType.COMP.toString(), compName, "Has") + "\n");
        			
        			if (null != filterStr)
        			{
        				IntentFilterInfo filterInfo = MetadataParser.parseIntentFilter(filterStr);
        				
        	            if (null != filterInfo.action)
        	            {
        	            		for (String action : filterInfo.action)
        	            		{
        	            			cypherScripts.append(CypherUtils.addNode(NodeType.ACTION.toString(), action) + "\n");
        	            			cypherScripts.append(CypherUtils.addRelationship(NodeType.COMP.toString(), compName, NodeType.ACTION.toString(), action, "Has") + "\n");
        	            		}
        	            }
        	            
        	            if (null != filterInfo.category)
        	            {
            	            	for (String category : filterInfo.category)
        	            		{
            	            		cypherScripts.append(CypherUtils.addNode(NodeType.CATEGORY.toString(), category) + "\n");
            	            		cypherScripts.append(CypherUtils.addRelationship(NodeType.COMP.toString(), compName, NodeType.CATEGORY.toString(), category, "Has") + "\n");
        	            		}
        	            }
        			}
            }
        }
		
		return cypherScripts.toString();
	}
}

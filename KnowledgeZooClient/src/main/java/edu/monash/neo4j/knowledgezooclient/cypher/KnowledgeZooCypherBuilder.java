package edu.monash.neo4j.knowledgezooclient.cypher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.monash.neo4j.knowledgezooclient.MetadataParser;
import edu.monash.neo4j.knowledgezooclient.util.CypherUtils;

public class KnowledgeZooCypherBuilder 
{	
	/**
	 * generate cypher scripts according to the input ApkInfo. multiple scripts will be generated, and are stored in the returned list.
	 * @param info
	 * @return
	 */
	public static List<String> parseApkAsScriptList(ApkInfo info) {
		@SuppressWarnings("serial")
		List<String> cypherScripts = new LinkedList<String>() {
			@Override
			public boolean add(String s) {
				// if CypherUtils.addAttribute() cannot find the attribute, it will return ""
				// we don't want this added to the list
				return s.contains("" + "\n") ? false : super.add(s);
			}
		};
		
		cypherScripts.add(CypherUtils.addNode(NodeType.APK.toString(), info.sha256) + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.packageName.toString(), info.packageName) + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.versionCode.toString(), info.versionCode) + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.versionName.toString(), info.versionName) + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.sdkVersion.toString(), info.sdkVersion) + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.mainActivity.toString(), info.mainActivity) + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.usesNativeCode.toString(), info.usesNativeCode + "") + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.usesDynamicCode.toString(), info.usesDynamicCode + "") + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.usesReflection.toString(), info.usesReflection + "") + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.usesObfuscation.toString(), info.usesObfuscation + "") + "\n");
		
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.dexDate.toString(), info.dexDate) + "\n");
		
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.dexSize.toString(), info.dexSize) + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.APK.toString(), info.sha256, NodeAttribute.apkSize.toString(), info.apkSize) + "\n");
        
		cypherScripts.add(CypherUtils.addNode(NodeType.CERT.toString(), info.certFingerprint) + "\n");
		cypherScripts.add(CypherUtils.addAttribute(NodeType.CERT.toString(), info.certFingerprint, NodeAttribute.certOwner.toString(), info.certOwner) + "\n");
		cypherScripts.add(CypherUtils.addRelationship(NodeType.APK.toString(), info.sha256, NodeType.CERT.toString(), info.certFingerprint, "SignedBy") + "\n");
        
        if (null != info.permissions)
        {
        		for (String perm : info.permissions)
	        {
	        		cypherScripts.add(CypherUtils.addNode(NodeType.PERM.toString(), perm) + "\n");
	        		cypherScripts.add(CypherUtils.addRelationship(NodeType.APK.toString(), info.sha256, NodeType.PERM.toString(), perm, "Has") + "\n");
	        }
        }
	        
        if (null != info.packages)
        {
        		for (String pkg : info.packages)
	        {
	        		cypherScripts.add(CypherUtils.addNode(NodeType.PKG.toString(), pkg) + "\n");
	        		cypherScripts.add(CypherUtils.addRelationship(NodeType.APK.toString(), info.sha256, NodeType.PKG.toString(), pkg, "Has") + "\n");
	        }
        }
	        

        parseComp(info.sha256, "activity", info.activities, cypherScripts);
        parseComp(info.sha256, "service", info.services, cypherScripts);
        parseComp(info.sha256, "receiver", info.receivers, cypherScripts);
        //parseComp(info.sha256, "provider", info.providers, cypherScripts);
		
		return cypherScripts;
	}
	
	public static String parseApk(ApkInfo info)
	{
		List<String> cypherScripts = parseApkAsScriptList(info);
		StringBuilder cypherBuilder = new StringBuilder();
		for (String s : cypherScripts)
			cypherBuilder.append(s);
		return cypherBuilder.toString();
	}
	
	private static void parseComp(String sha256, String compType, ArrayList<String> comps, List<String> cypherScripts)
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
    			
    			cypherScripts.add(CypherUtils.addNode(NodeType.COMP.toString(), compName) + "\n");
    			cypherScripts.add(CypherUtils.addAttribute(NodeType.COMP.toString(), compName, NodeAttribute.compType.toString(), compType) + "\n");
    			cypherScripts.add(CypherUtils.addRelationship(NodeType.APK.toString(), sha256, NodeType.COMP.toString(), compName, "Has") + "\n");
    			
    			if (null != filterStr)
    			{
    				IntentFilterInfo filterInfo = MetadataParser.parseIntentFilter(filterStr);
    				
    	            if (null != filterInfo.action)
    	            {
	            		for (String action : filterInfo.action)
	            		{
	            			cypherScripts.add(CypherUtils.addNode(NodeType.ACTION.toString(), action) + "\n");
	            			cypherScripts.add(CypherUtils.addRelationship(NodeType.COMP.toString(), compName, NodeType.ACTION.toString(), action, "Has") + "\n");
	            		}
    	            }
    	            
    	            if (null != filterInfo.category)
    	            {
    	            	for (String category : filterInfo.category)
	            		{
    	            		cypherScripts.add(CypherUtils.addNode(NodeType.CATEGORY.toString(), category) + "\n");
    	            		cypherScripts.add(CypherUtils.addRelationship(NodeType.COMP.toString(), compName, NodeType.CATEGORY.toString(), category, "Has") + "\n");
	            		}
    	            }
    			}
            }
        }
	}
}

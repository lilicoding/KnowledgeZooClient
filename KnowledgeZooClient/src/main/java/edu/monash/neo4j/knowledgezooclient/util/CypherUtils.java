package edu.monash.neo4j.knowledgezooclient.util;

public class CypherUtils 
{
	public static String addNode(String nodeType, String nodeName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("MERGE (n:" + nodeType + "{name:'" + nodeName + "'})");
		
		final String statement = sb.toString();
		System.out.println(statement  + ";" + "\n");
		
		return statement;
	}
	
	public static String deleteNode(String nodeType, String nodeName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("MATCH (");
		sb.append("n: " + nodeType + " ");
		sb.append("{name: '" + nodeName + "'})");
		sb.append(" DETACH DELETE n");;
		
		final String statement = sb.toString();
		System.out.println(statement  + ";" + "\n");
		
		return statement;
	}
	
	public static String addAttribute(String nodeType, String nodeName, String attrKey, String attrValue)
	{
		if (null != attrValue && !attrValue.isEmpty())
		{
			if (attrValue.contains("'"))
			{
				attrValue = attrValue.replace("'", "\\'");
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("MATCH (n:" + nodeType + " {name:'" + nodeName + "'})" + "\n");
			sb.append("SET n." + attrKey + "='" + attrValue + "'" + "\n");
			sb.append("RETURN n");
			
			final String statement = sb.toString();
			System.out.println(statement  + ";" + "\n");
			
			return statement;
		}
		else
		{
			//System.out.println("Warning:" + nodeType + "/" + nodeName + ": " + attrKey + " is null");
			return "";
		}
	}
	
	public static String addRelationship(String firstNodeType, String firstNodeName, String secondNodeType, String secondNodeName, String relationshipName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("MATCH (firstNode:" + firstNodeType + " " + "{name:" + "'" + firstNodeName + "'" + "})" + "\n");
		sb.append("MATCH (secondNode:" + secondNodeType + " " + "{name:" + "'" + secondNodeName + "'" + "})" + "\n");
		sb.append("MERGE (firstNode)-[:" + relationshipName + "]->(secondNode)");
		
		final String statement = sb.toString();
		System.out.println(statement  + ";" + "\n");
		
		return statement;
	}
}

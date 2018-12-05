package edu.monash.neo4j.knowledgezooclient.cypher;

import java.util.ArrayList;

public class IntentFilterInfo 
{
	public ArrayList<String> action;
	public ArrayList<String> category;
	
	@Override
	public String toString() {
		return "IntentFilterInfo [action=" + action + ", category=" + category + "]";
	}
}

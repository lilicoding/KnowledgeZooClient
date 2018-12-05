package edu.monash.neo4j.knowledgezooclient.csv;

public class ACTION 
{
	public String actionId;
	public String name;
	public String LABEL = "ACTION";
	
	@Override
	public String toString() {
		return actionId + ",\"" + name + "\"," + LABEL;
	}
}

package edu.monash.neo4j.knowledgezooclient.csv;

public class MARKET 
{
	public String marketId;
	public String name;
	public String LABEL = "MARKET";
	
	@Override
	public String toString() {
		return marketId + ",\"" + name + "\"," + LABEL;
	}
}

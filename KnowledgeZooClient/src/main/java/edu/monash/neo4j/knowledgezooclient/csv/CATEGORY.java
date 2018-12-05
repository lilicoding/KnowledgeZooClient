package edu.monash.neo4j.knowledgezooclient.csv;

public class CATEGORY 
{
	public String categoryId;
	public String name;
	public String LABEL = "CATEGORY";
	
	@Override
	public String toString() {
		return categoryId + ",\"" + name + "\"," + LABEL;
	}
}

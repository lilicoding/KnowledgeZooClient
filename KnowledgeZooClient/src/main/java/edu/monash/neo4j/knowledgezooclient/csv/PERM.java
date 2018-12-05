package edu.monash.neo4j.knowledgezooclient.csv;

public class PERM 
{
	public String permId;
	public String name;
	public String LABEL = "PERM";
	
	@Override
	public String toString() {
		if (permId.contains(","))
		{
			permId = permId.substring(0, permId.indexOf(','));
			name = permId;
		}
		
		
		return permId + ",\"" + name + "\"," + LABEL;
	}
}

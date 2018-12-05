package edu.monash.neo4j.knowledgezooclient.csv;

public class PKG 
{
	public String pkgId;
	public String name;
	public String LABEL = "PKG";
	
	@Override
	public String toString() {
		return pkgId + ",\"" + name + "\"," + LABEL;
	}
}

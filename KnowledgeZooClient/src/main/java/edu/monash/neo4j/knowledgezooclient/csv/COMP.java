package edu.monash.neo4j.knowledgezooclient.csv;

import java.util.HashSet;
import java.util.Set;

public class COMP 
{
	public String compId;
	public String name;
	public String compType;
	public String LABEL = "COMP";
	
	public Set<ACTION> actions = new HashSet<ACTION>();
	public Set<CATEGORY> categories = new HashSet<CATEGORY>();
	
	@Override
	public String toString() {
		return compId + ",\"" + name + "\",\"" + compType + "\"," + LABEL;
	}
}

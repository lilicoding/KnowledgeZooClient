package edu.monash.neo4j.knowledgezooclient.csv;

public class CERT 
{
	public String certId;
	public String name;
	public String certOwner;
	public String LABEL = "CERT";
	
	final String delimiter = "(-_-)";
	final String quotationMark = "(_-_)";
	
	@Override
	public String toString() {
		String owner = certOwner;
		
		//why owner is null
		if (null != owner)
		{
			if (owner.contains(","))
				owner = owner.replaceAll(",", delimiter);
			
			owner = owner.replace("\"", quotationMark);
			
			//carriage return
			if (owner.contains("\r"))
			{
				owner = owner.replace("\r", "");
			}
		}
		
		return certId + ",\"" + name + "\",\"" + owner + "\"," + LABEL;
	}
}

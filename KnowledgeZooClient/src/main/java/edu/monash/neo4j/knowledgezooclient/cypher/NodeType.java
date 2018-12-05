package edu.monash.neo4j.knowledgezooclient.cypher;

public enum NodeType {
	APK ("APK"),
	PERM ("PERM"),
	PKG ("PKG"),
	CERT ("CERT"),
	MARKET ("MARKET"),
	MALABLE ("MALABLE"),
	COMP ("COMP"),
	ACTION ("ACTION"),
	CATEGORY ("CATEGORY");
	
	private final String nodeType;

	NodeType(final String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public String toString() {
        return nodeType;
    }
}
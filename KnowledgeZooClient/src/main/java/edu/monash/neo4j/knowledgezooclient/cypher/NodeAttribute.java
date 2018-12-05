package edu.monash.neo4j.knowledgezooclient.cypher;

public enum NodeAttribute {
	//for Node APK
	packageName ("packageName"),
	versionCode ("versionCode"),
	versionName ("versionName"),
	sdkVersion ("sdkVersion"),
	mainActivity ("mainActivity"),
	usesNativeCode ("usesNativeCode"),
	usesDynamicCode ("usesDynamicCode"),
	usesReflection ("usesReflection"),
	usesObfuscation ("usesObfuscation"),
	dexDate ("dexDate"),
	dexDateInMillis ("dexDateInMillis"),
	dexSize ("dexSize"),
	apkSize ("apkSize"),
	
	//for Node COMP
	compType ("compType"),
	
	//for Node CERT
	certOwner ("certOwner");
	
	private final String value;

	NodeAttribute(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
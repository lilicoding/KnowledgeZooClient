package edu.monash.neo4j.knowledgezooclient.csv;

import java.util.HashSet;
import java.util.Set;

public class APK 
{
	public String apkId;
	public String name;
	
	public String sha1;
	public String md5;
	
	public String packageName;
	public String versionCode;
	public String versionName;
	public int sdkVersion;
	public String mainActivity;
	public boolean usesNativeCode;
	public boolean usesDynamicCode;
	public boolean usesReflection;
	public boolean usesObfuscation;
	
	public String dexDate;
	public long dexDateInMillis;
	public int dexSize;
	public int apkSize;
	public String LABEL = "APK";
	
	public CERT cert = new CERT();
	
	public Set<MARKET> markets = new HashSet<MARKET>();
	public Set<COMP> comps = new HashSet<COMP>();
	public Set<PERM> perms = new HashSet<PERM>();
	public Set<PKG> pkgs = new HashSet<PKG>();
	
	
	@Override
	public String toString() {
		return apkId + ",\"" + name + "\",\"" + sha1.toUpperCase() + "\",\"" + md5.toUpperCase() + "\",\"" +
				packageName + "\",\"" + versionCode + "\",\"" + versionName + "\"," + 
				sdkVersion + ",\"" + mainActivity + "\"," + usesNativeCode + "," + usesDynamicCode + "," + 
				usesReflection + "," + usesObfuscation + ",\"" + dexDate + "\"," + dexDateInMillis + "," + dexSize + "," + 
				apkSize + "," + LABEL;
	}
}

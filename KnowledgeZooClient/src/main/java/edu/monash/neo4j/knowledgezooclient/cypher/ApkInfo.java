package edu.monash.neo4j.knowledgezooclient.cypher;

import java.util.ArrayList;

public class ApkInfo 
{
	public String sha256;
	
	public String packageName;
	public String versionCode;
	public String versionName;
	public String sdkVersion;
	public String mainActivity;
	
	public boolean usesNativeCode;
	public boolean usesDynamicCode;
	public boolean usesReflection;
	public boolean usesObfuscation;
	
	public String dexDate;
	public String dexSize;
	public String apkSize;
	
	public String certOwner;
	public String certFingerprint;
	
	public ArrayList<String> packages;
	public ArrayList<String> permissions;
	
	public ArrayList<String> activities;
	public ArrayList<String> services;
	public ArrayList<String> receivers;
	//public ArrayList<String> providers;
	public String providers;
	
	@Override
	public String toString() {
		return "ApkInfo [sha256=" + sha256 + ", mainActivity=" + mainActivity + ", packageName=" + packageName
				+ ", versionCode=" + versionCode + ", versionName=" + versionName + ", sdkVersion=" + sdkVersion
				+ ", usesNativeCode=" + usesNativeCode + ", usesDynamicCode=" + usesDynamicCode + ", usesReflection="
				+ usesReflection + ", usesObfuscation=" + usesObfuscation + ", dexDate=" + dexDate + ", dexSize=" 
				+ dexSize + ", apkSize=" + apkSize + ", cert_owner=" + certOwner
				+ ", cert_fingerprint=" + certFingerprint + ", packages=" + packages + ", permissions=" + permissions
				+ ", activities=" + activities + ", services=" + services + ", receivers=" + receivers + ", providers="
				+ providers + "]";
	}
}

package com.asadali.sonar.tsql;

import org.sonar.api.config.Settings;
import org.sonar.api.resources.AbstractLanguage;

public class TSQL extends AbstractLanguage {

	public static final String KEY = "sql";
	private static final String[] fileSuffixes = new String[] { ".sql" };
	

	private Settings settings;

	public TSQL(Settings configuration) {
		super(KEY, "Transact SQL");
		this.settings = configuration;
	}

	public Settings getSettings() {
		return this.settings;
	}

	public String[] getFileSuffixes() {
		return fileSuffixes;
	}
	
	
	public static boolean isTSQLFile(java.io.File file) {
		boolean returnVal = false;
		if(file != null) {
			String fileName = file.getName();
			String suffix = fileName.substring(fileName.indexOf("."));
			returnVal = (suffix == fileSuffixes[0]);
		} 
		return returnVal;		
    }

}

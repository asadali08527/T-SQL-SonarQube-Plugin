package com.asadali.codeanalyzer.tsql;

import java.io.File;
import java.io.FilenameFilter;

public class SQLFileExtnFilter implements FilenameFilter {
		
    private static String ext = ".sql" ;
    
  
	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		return name.toLowerCase().endsWith(ext);
	}

}

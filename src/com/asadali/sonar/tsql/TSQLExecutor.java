package com.asadali.sonar.tsql;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.ProjectClasspath;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.utils.TimeProfiler;

import com.asadali.codeanalyzer.CodeAnalysisRule;
import com.asadali.codeanalyzer.CodeAnalyzerException;
import com.asadali.codeanalyzer.reporting.Report;
import com.asadali.codeanalyzer.tsql.SourceCodeProcessor;

public class TSQLExecutor implements BatchExtension {

	private final Project project;
	  private final ProjectFileSystem projectFileSystem;
	  private final RulesProfile rulesProfile;
	  private final ClassLoader projectClassloader;
	
	public TSQLExecutor(Project project, ProjectFileSystem projectFileSystem, RulesProfile rulesProfile,ProjectClasspath classpath) {
		this.project = project;
	    this.projectFileSystem = projectFileSystem;
	    this.rulesProfile = rulesProfile;	  
	    this.projectClassloader = classpath.getClassloader();
	    
	}
	
	
	public Report execute() throws CodeAnalyzerException {
		TimeProfiler profiler = new TimeProfiler().start("Execute TSQL Analyzer ");
		 ClassLoader initialClassLoader = Thread.currentThread().getContextClassLoader();
		 try {
		 
			 Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		      return executeAnalyzer();
  	     } finally {
		      Thread.currentThread().setContextClassLoader(initialClassLoader);
		      profiler.stop();
		 }
		
	}


	private Report executeAnalyzer() throws CodeAnalyzerException {
		
		Report report = null;		
		List<ActiveRule> activeRules = rulesProfile.getActiveRulesByRepository(TSQLConstants.REPOSITORY_KEY);
		List<Class<? extends CodeAnalysisRule>> ruleList = getTSQLAnalyzerRules(activeRules);
		List<InputFile> inputFiles = projectFileSystem.mainFiles(TSQL.KEY);
		File[] files = getFiles(inputFiles);
		SourceCodeProcessor processor = new SourceCodeProcessor();
		report = processor.analyzeFiles(files, ruleList);
		
		return report;
	}
	
	
	
	


	private List<Class<? extends CodeAnalysisRule>> getTSQLAnalyzerRules(List<ActiveRule> activeRules) {
		
		List<Class<? extends CodeAnalysisRule>> ruleList = new ArrayList<Class<? extends CodeAnalysisRule>>(activeRules.size());
		
		for (ActiveRule activeRule : activeRules) {
			String className = activeRule.getRule().getConfigKey();
			ruleList.add(loadClass(className));
		}
		return ruleList;
	}
	
	private Class<? extends CodeAnalysisRule> loadClass(String className) {
		Class<? extends CodeAnalysisRule> clazz = null;
		try {
			clazz = (Class<? extends CodeAnalysisRule>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clazz;
	}
	
	private File[] getFiles(List<InputFile> inputFiles) {
		File[] files = new File[inputFiles.size()];
		int i=0;
		for (InputFile inputFile : inputFiles) {
			files[i] = inputFile.getFile();
			i++;
		}
		return files;		
	}
	
}

package com.asadali.codeanalyzer;

/**
 * @author CWR.ASAD.ALI
 */
import com.asadali.codeanalyzer.output.ConsoleReporter;
import com.asadali.codeanalyzer.output.Reporter;
import com.asadali.codeanalyzer.reporting.Report;
import com.asadali.codeanalyzer.tsql.SourceCodeProcessor;

/** 
 * Entry point for the toolkit
 * 
 * 
 * 
 *
 */
public class CodeAnalyzer {
	
	
	public static void main(String[] args) throws Exception {		
		
		String dirPath = "c:/temp/sqls/src";
		String ruleset = "ALL";
		
		if(args.length == 0) {
			System.err.println("Usage java -cp <classpath> CodeAnalyzer <dir>[mandatory] <rules>[optional]");
			//System.exit(0);
		}
		
		if(args.length >= 1) {
			dirPath = args[0];
		}
		
		if(args.length >= 2) {
			ruleset = args[1];
		}
		
		//List<Class<? extends CodeAnalysisRule>> ruleList= getRuleList(ruleset);
		SourceCodeProcessor processor = new SourceCodeProcessor();
		
		Report report = processor.analyzeDirectory(dirPath);
		printReport(report);
		
	}
	
	private static void printReport(Report report) {
		
		Reporter console = new ConsoleReporter();
		console.report(report);
		
	}
}



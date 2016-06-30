package com.asadali.codeanalyzer.tsql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.asadali.codeanalyzer.CodeAnalysisRule;
import com.asadali.codeanalyzer.CodeAnalyzerException;
import com.asadali.codeanalyzer.Processor;
import com.asadali.codeanalyzer.reporting.FileSummary;
import com.asadali.codeanalyzer.reporting.Report;
import com.asadali.codeanalyzer.reporting.RuleViolation;
import com.asadali.codeanalyzer.tsql.util.XMLUtils;
import com.asadali.codeanalyzer.tsql.xml.RuleSet;
import com.asadali.codeanalyzer.tsql.xml.RuleSets;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.parser.GOLDParser;




public class SourceCodeProcessor implements Processor{
	
	private static Pattern NEWLINE_PATTERN = Pattern.compile("("+System.getProperty("line.separator")+")");
	

	/*public static void main(String[] args) {
		String content = "select 'abc' from Test as test1 INNER JOIN (select * from tab where 1 = 1) as test2 ON table1.column_name=table2.column_name ";
		String content2 = "SELECT @@IDENTITY AS test";
		String content3 = "select abc from Test as test1 INNER JOIN (SELECT @@IDENTITY AS test) as test2 ON table1.column_name=table2.column_name ";
		String content4 = "SELECT [T2].[c3], [T1].[c3] FROM [dbo].[Table2] T2, [dbo].[Table1] T1 WHERE T1.ID = T2.ID";
		String s1 = "CREATE PROCEDURE test AS " +
				"BEGIN " +
					"DECLARE @sql VARCHAR(255) \n" +
					"DECLARE @cnt int \n" +
					"SET @sql = 'SELECT COUNT(1) FROM myTable '\n" +
					//"SET @cnt = EXEC( @sql ) \n" +
					"IF @cnt > 0 \n" +
						"BEGIN \n" +
							"PRINT 'A' \n" +
							"DECLARE @sql VARCHAR(255) \n" +
						"END \n" +
					"ELSE \n" +
						"BEGIN \n" +
							"PRINT 'A' \n" +
							"DECLARE @sql VARCHAR(255) \n" +
						"END \n" +
					"DECLARE @sql VARCHAR(255) \n" +
					"select abcde from dddd where xyz=123 \n"+
					"DECLARE @sql VARCHAR(255) \n" +
					"DECLARE @sql VARCHAR(255) \n" +
				"END \n";
		//System.out.println(s1);
		String content5 = "CREATE TABLE [dbo].[TableWithWarning] ( [ID] INT NOT NULL IDENTITY(0, 1), [c1] INT NOT NULL PRIMARY KEY, [c2] INT, [c3] INT,[SmallString] VARCHAR(2)) ";
		String content6 = "CREATE TABLE [dbo].[TableWithProblemColumn] ( [ID] INT NOT NULL IDENTITY(0, 1), [Small'String] VARCHAR(10))";
		String content7 = "CREATE PROCEDURE [dbo].[sp_procWithWarning] ( @Value1 INT ) AS BEGIN RETURN 0;END";
		String content8 = "CREATE TYPE test FROM varchar(11) NOT NULL ;";
		String content9 = "SELECT * FROM dbo.[Table2] WHERE Comment LIKE '%pples' or status = 'Active'"; 
		String content10 = "SELECT * WHERE (@param1 - 5 > [c1])";
		//SourceCodeProcessor engine = new SourceCodeProcessor();
		//engine.analyze(content10);

	}*/

	
	@Override
	public Report analyzeDirectory(String dirPath) throws CodeAnalyzerException {
		
		return analyzeDirectory(dirPath,"ALL");
		
	}


	@Override
	public Report analyzeFiles(File[] files) throws CodeAnalyzerException {
		
		return analyzeFiles(files,"ALL");
		
	}	
	
	
	@Override
	public Report analyzeDirectory(String dirPath, String ruleSetId) throws CodeAnalyzerException {
		try {
			List<Class<? extends CodeAnalysisRule>> rules = getRuleList(ruleSetId);
			return analyzeDirectory(dirPath, rules);
		} catch (Exception exception) {
			throw new CodeAnalyzerException(exception);
		}
	}


	@Override
	public Report analyzeFiles(File[] files, String ruleSetId) throws CodeAnalyzerException {
		try {
			List<Class<? extends CodeAnalysisRule>> rules = getRuleList(ruleSetId);
			return analyzeFiles(files, rules);
		} catch (Exception exception) {
			throw new CodeAnalyzerException(exception);
		}
	}	
	

	
	
	
	public Report analyzeDirectory(String dirPath, List<Class<? extends CodeAnalysisRule>> ruleList) throws CodeAnalyzerException {
		
		
		File folder = new File(dirPath);
		// only pick those files who have an extension of .sql
		File[] listOfFiles = folder.listFiles(new SQLFileExtnFilter());
		
		return analyzeFiles(listOfFiles,ruleList);
	}
	
	
	public Report analyzeFiles(File[] files, List<Class<? extends CodeAnalysisRule>> ruleList) throws CodeAnalyzerException {
		
		Report executionReport = new Report();		
		executionReport.setExecutionStart(System.currentTimeMillis());
		try {
			List<CodeAnalysisRule> rules = instantiateRules(ruleList);		
			for (File file : files) {
				FileSummary summary = analyze(file, rules);
				executionReport.addFileSummary(summary);
			}
			return executionReport;
		} catch (Exception exception) {
			throw new CodeAnalyzerException(exception);
		}
		finally {
			executionReport.setExecutionEnd(System.currentTimeMillis());
		}
		
	}
	
	private FileSummary analyze(File file, List<CodeAnalysisRule> ruleList) throws CodeAnalyzerException{
		FileSummary summary = new FileSummary(file.getName(),file.getAbsolutePath());
		summary.setExecutionStart(System.currentTimeMillis());
		
		String sourceCode = getFileContents(file);
		summary.setLinesOfCode(getLinesOfCode(sourceCode));		
		Reduction root = generateAST(sourceCode);		
		for (CodeAnalysisRule rule : ruleList) {
			
			switch(rule.getType()) {			
				case AST_BASED:
					rule.evaluate(root);
					break;
				case PATTERN_BASED:	
					rule.evaluate(sourceCode);
					break;			
			}
						
			if(rule.hasIssueBeenReported()) {
				summary.addViolation(new RuleViolation(rule.getId(), rule.description(),rule.getLineNumber()));
			}
		}
		
		summary.setExecutionEnd(System.currentTimeMillis());
		
		return summary;
	}
	 
	
	
	private long getLinesOfCode(String sourceCode) {
		Matcher m = NEWLINE_PATTERN.matcher(sourceCode);
		int lines = 0;
		while (m.find())
		{
		    lines++;
		}
		return lines;
	}


	private List<CodeAnalysisRule> instantiateRules(List<Class<? extends CodeAnalysisRule>> ruleList) {
		List<CodeAnalysisRule> ruleObjectsList = new ArrayList<CodeAnalysisRule>();
		try {
			for (Class<? extends CodeAnalysisRule> clazz : ruleList) {				
					ruleObjectsList.add(clazz.newInstance());				
			}
		} catch (InstantiationException ie) {			
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
		return ruleObjectsList;
	}

	
	private Reduction generateAST(String sourceCode) throws CodeAnalyzerException {
		  // Use the compiled grammar file inside the jar
        GOLDParser parser = new GOLDParser(
        	SourceCodeProcessor.class.getResourceAsStream("t-sql.egt"),             // compiled grammar table
            "com.synechron.codeanalyzer.tsql.rulehandlers",                         // rule handler package (fully qualified package)
            false);                                         						// trim reductions
       
        // Controls whether or not a parse tree is returned or the program executed.
        parser.setGenerateTree(true);
        
        // Parse the source statements to see if it is syntactically correct
        boolean parsedWithoutError = parser.parseSourceStatements(sourceCode);
        // Holds the parse tree if setGenerateTree(true) was called
        if(parsedWithoutError) {
        	String tree = parser.getParseTree();       
        	System.out.println(tree);
        	return parser.getCurrentReduction();
        } else {
        	throw new CodeAnalyzerException("Unable to parse code to generate Tree :: " + parser.getErrorMessage());
        }
        
	}	
	

	private String getFileContents(File file) throws CodeAnalyzerException {		
		StringBuilder buffer = new StringBuilder();
		try {
			String line=null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while((line=bufferedReader.readLine())!= null) {
				buffer.append(line);
				buffer.append(System.getProperty("line.separator"));					
			}
			bufferedReader.close();
			return buffer.toString();
		} catch(IOException ioe) {
			throw new CodeAnalyzerException("Exception getting fileContents", ioe);
		}
	}

	
	private List<Class<? extends CodeAnalysisRule>> getRuleList(String ruleSetId) throws Exception{		
		
		InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream("com/synechron/codeanalyzer/tsql/rules.xml");		
		List<Class<? extends CodeAnalysisRule>> ruleList = new ArrayList<>();
		RuleSets ruleSets = XMLUtils.unmarshall(fileStream, RuleSets.class,RuleSet.class);
		
		switch(ruleSetId){
				case "ALL" :
								for (RuleSet ruleSet : ruleSets.getRuleset()) {
									ruleList.addAll(getRuleListFromRuleSet(ruleSet.getId(),ruleSets));
								}
								break;
				case "DESIGN" :
								ruleList.addAll(getRuleListFromRuleSet(ruleSetId,ruleSets));
								break;
				case "NAMING" :
								ruleList.addAll(getRuleListFromRuleSet(ruleSetId,ruleSets));
								break;
				case "PERFORMANCE" :
								ruleList.addAll(getRuleListFromRuleSet(ruleSetId,ruleSets));
								break;
				default :
								throw new CodeAnalyzerException("Invalid RuleSet selected");
					
		}		
		return ruleList;
	}
	
	
	private List<Class<? extends CodeAnalysisRule>> getRuleListFromRuleSet(String ruleSetId,RuleSets ruleSets) throws Exception {
		List<Class<? extends CodeAnalysisRule>> ruleList = new ArrayList<>();
		for (RuleSet ruleSet : ruleSets.getRuleset()) {
			if(ruleSet.getId().equals(ruleSetId)) {
				for(String ruleClass : ruleSet.getRules())
				ruleList.add(getRuleClass(ruleClass));
			}			
		}		
		return ruleList;
	}
	
	
	private Class getRuleClass(String fqcn) throws Exception {
		return Class.forName(fqcn);
	}

}

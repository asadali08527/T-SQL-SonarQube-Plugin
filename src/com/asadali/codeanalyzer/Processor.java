package com.asadali.codeanalyzer;

import java.io.File;
import java.util.List;

import com.asadali.codeanalyzer.reporting.Report;

public interface Processor {

	Report analyzeDirectory(String dirPath) throws CodeAnalyzerException;
	Report analyzeFiles(File[] files) throws CodeAnalyzerException;	
	Report analyzeDirectory(String dirPath, String ruleSetId) throws CodeAnalyzerException;
	Report analyzeFiles(File[] files, String ruleSetId) throws CodeAnalyzerException;
	Report analyzeDirectory(String dirPath, List<Class<? extends CodeAnalysisRule>> ruleList) throws CodeAnalyzerException;
	Report analyzeFiles(File[] files, List<Class<? extends CodeAnalysisRule>> ruleList) throws CodeAnalyzerException;	
	
}

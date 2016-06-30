package com.asadali.codeanalyzer;

import com.asadali.codeanalyzer.rules.Type;


public interface CodeAnalysisRule<T> {
	
	void evaluate(T object);		
	boolean hasIssueBeenReported();
	String description();
	String getId();
	int getLineNumber();
	Type getType();

}

package com.asadali.codeanalyzer;

public class CodeAnalyzerException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CodeAnalyzerException() {
		super();
	}
	
	public CodeAnalyzerException(String message) {
		super(message);
	}
	
	public CodeAnalyzerException(String message, Throwable throwable) {
		super(message,throwable);
	}
	
	public CodeAnalyzerException(Throwable throwable) {
		super(throwable);
	}
	
}

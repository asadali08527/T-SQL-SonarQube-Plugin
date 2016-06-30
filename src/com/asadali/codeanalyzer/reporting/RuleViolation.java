package com.asadali.codeanalyzer.reporting;

public class RuleViolation {
	
	private String ruleId;
	private String description;
	private int lineNumber;
	
	public RuleViolation(String ruleId, String description) {
		super();
		this.ruleId = ruleId;
		this.description = description;
		this.lineNumber = 0;
	}

	public RuleViolation(String ruleId, String description, int lineNumber) {
		super();
		this.ruleId = ruleId;
		this.description = description;
		this.lineNumber = lineNumber;
	}
	
	public String getRuleId() {
		return ruleId;
	}


	public String getDescription() {
		return description;
	}

	public int getLineNumber() {
		return lineNumber;
	}

}

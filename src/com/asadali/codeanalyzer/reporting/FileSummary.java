package com.asadali.codeanalyzer.reporting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileSummary {

	private String fileName = null;

	private String filePath = null;
	private List<RuleViolation> violations = null;
	private long executionStart;
	private long executionEnd;
	private long linesOfCode;
	
	public FileSummary(String fileName, String filePath) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.violations = new ArrayList<RuleViolation>();
		this.linesOfCode = 0;
	}

	public long getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(long linesOfCode) {
		this.linesOfCode = linesOfCode;
	}

	public String getFileName() {
		return fileName;
	}

	public void addViolation(RuleViolation violation) {
		this.violations.add(violation);
	}

	public long getExecutionStart() {
		return executionStart;
	}

	public void setExecutionStart(long executionStart) {
		this.executionStart = executionStart;
	}

	public long getExecutionEnd() {
		return executionEnd;
	}

	public void setExecutionEnd(long executionEnd) {
		this.executionEnd = executionEnd;
	}

	public Iterator<RuleViolation> violations() {
		return this.violations.iterator();
	}
	
	public boolean areViolationsReported() {
		return !this.violations.isEmpty();
	}
	
	public String getFilePath() {
		return filePath;
	}

}

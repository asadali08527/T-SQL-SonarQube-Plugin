package com.asadali.codeanalyzer.reporting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Report{

	private List<FileSummary> fileList = null;
	private long executionStart;
	private long executionEnd;
	private long linesOfCodeAnalyzed;
	
	public Report() {
		this.fileList = new ArrayList<FileSummary>();
		this.linesOfCodeAnalyzed = 0;
	}
	
	public void addFileSummary(FileSummary summary) {
		this.linesOfCodeAnalyzed += summary.getLinesOfCode();
		fileList.add(summary);
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

	public Iterator<FileSummary> iterate() {
		return fileList.iterator();
	}

	public long getLinesOfCodeAnalyzed() {
		return linesOfCodeAnalyzed;
	}	
	
}

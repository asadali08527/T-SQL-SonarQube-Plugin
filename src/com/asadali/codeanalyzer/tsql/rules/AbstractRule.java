package com.asadali.codeanalyzer.tsql.rules;

import com.asadali.codeanalyzer.CodeAnalysisRule;
import com.asadali.codeanalyzer.rules.Type;

public abstract class AbstractRule<T> implements CodeAnalysisRule<T> {

	protected boolean issueNoticed;
	protected int lineNumber;
	protected Type type;
	
	public AbstractRule(Type type) {
		super();
		this.issueNoticed = false;
		this.lineNumber = 0;
		this.type = type;
	}
	

	public final void evaluate(T object) {
		reset(); // called to reset the state
		checkForVoilation(object);
	}

	
	protected void reportIssue() {
		this.issueNoticed = true;
	}

	
	public boolean hasIssueBeenReported() {
		return this.issueNoticed;
	}

	protected void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public int getLineNumber() {
		return this.lineNumber;
	}

	/**
	 * Resets the state
	 */
	protected void reset() {
		this.issueNoticed = false;
		this.lineNumber = 0;
	}
	
	protected abstract void checkForVoilation(T object); 
	
	@Override
	public Type getType() {
		return this.type;
	}
	
}

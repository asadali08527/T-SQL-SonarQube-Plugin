package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0051 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<ExecuteStatement>".equals(token.asReduction().getParent().getHead().toString())) {
						checkForRule(token.asReduction());					
					} else {
						searchReductionTree(token.asReduction());
					}
					break;
			}
		}
	}
	
	private void checkForRule(Reduction reduction) {
		
		// check if procedure name is provided as part of execute
		if(reduction.getParent().getHandle().size() > 0) {			
			if(reduction.getParent().getHandle().findByName("ProcedureNameQualified") != null) {
				processProcedureNameQualified(reduction);
			} else {
				 reportIssue();
				 Token token = reduction.get(0); // returns the token "EXECUTE"
				 setLineNumber(token.getPosition().getLine());
				 
			}
		}
		
	}
	
	
	private void processProcedureNameQualified(Reduction reduction) {
		
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ProcedureNameQualified>".equals(token.asReduction().getParent()
						.getHead().toString())) {
					processProcedureName(token.asReduction());				 
				} 
				break;
			default:
				break;
			}
		}
	}
	
	private void processProcedureName(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ProcedureName>".equals(token.asReduction().getParent()
						.getHead().toString())) {					
					processObjectName(token.asReduction());
				 
				} 
				break;
			default:
				break;
			}
		}
	}

	private void processObjectName(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ObjectName>".equals(token.asReduction().getParent().getHead().toString())) {					
					processName(token.asReduction());
				}
				break;
			default:
				break;
			}
		}	
		
	}


	private void processName(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:
						// do nothing ..we are intrested in the name of it
				 default:
					 String procedureName = token.asString();
					 procedureName = procedureName.toUpperCase();
					 if("SP_EXECUTESQL".equals(procedureName)){				 
						 reportIssue();
						 setLineNumber(token.getPosition().getLine());
					 }
				 break;
			}
		}	
		
	}

	
	
	@Override
	public String description() {
		return "Do not use Dynamic SQL";
	}

	@Override
	public String getId() {
		return "SR0051";
	}

}

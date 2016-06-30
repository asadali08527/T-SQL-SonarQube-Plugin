package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0054 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<UpdateStatement>".equals(token.asReduction().getParent().getHead().toString())) {
						checkForRule(token.asReduction());					
					} else {
						searchReductionTree(token.asReduction());
					}
					break;
			}
		}
	}

	private void checkForRule(Reduction reduction) {
		
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<WhereClause>".equals(token.asReduction().getParent().getHead().toString())) {
						if(token.asReduction().isEmpty())
						{
							reportIssue();
							// getting the line number from token "UPDATE" which would be found in passed in reduction.
							Token updateStringToken = reduction.get(1);
							setLineNumber(updateStringToken.getPosition().getLine());
						}
					} 
					break;
			}
		}
		
	}	
	
	@Override
	public String description() {		
		return "Update statement should have a \"Where\" clause";
	}

	@Override
	public String getId() {
		return "SR0054";
	}

}

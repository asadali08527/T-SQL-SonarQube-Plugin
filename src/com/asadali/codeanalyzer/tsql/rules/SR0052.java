package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0052 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}
	
	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<DeclareItem>".equals(token.asReduction().getParent().getHead().toString())) {
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
		if(reduction.getParent().getHandle().findByName("CURSOR") != null) {
			reportIssue();
			 Token token = reduction.get(1); // returns the token "CURSOR"
			 setLineNumber(token.getPosition().getLine());
		}
		
	}

	@Override
	public String description() {
		return "Avoid using cursors";
	}

	@Override
	public String getId() {
		return "SR0052";
	}

}

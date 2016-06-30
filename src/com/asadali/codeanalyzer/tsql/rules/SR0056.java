package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0056 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}
	
	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<GotoStatement>".equals(token.asReduction().getParent().getHead().toString())) {
						checkForRule(token.asReduction());					
					} else {
						searchReductionTree(token.asReduction());
					}
					break;
			}
		}
	}
	
	private void checkForRule(Reduction reduction) {
		
		// check if UNION is present and ALL
		if(reduction.getParent().getHandle().findByName("GOTO") != null) {
			Token token1 = reduction.get(0); // returns the token "GOTO"
			reportIssue();
			setLineNumber(token1.getPosition().getLine());				
		}
		
	}

	@Override
	public String description() {
		return "GOTO should be avoided";
	}

	@Override
	public String getId() {
		return "SR0056";
	}

}

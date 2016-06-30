package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0053 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}
	
	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<RowsetCombineClause>".equals(token.asReduction().getParent().getHead().toString())) {
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
		if(reduction.getParent().getHandle().findByName("UNION") != null) {
			Token token1 = reduction.get(0); // returns the token "UNION"
			Token token2 = reduction.get(1);
			if("ALL".equals(token2.toString())) {
				// correct usage of union hence let go..
			} else {
				reportIssue();
				setLineNumber(token1.getPosition().getLine());
			}	
		}
		
	}

	@Override
	public String description() {
		return "UNION should be replaced with UNION ALL";
	}

	@Override
	public String getId() {
		return "SR0053";
	}

}

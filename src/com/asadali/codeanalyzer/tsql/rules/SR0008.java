package com.asadali.codeanalyzer.tsql.rules;


import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0008 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				searchReductionTree(token.asReduction());
				break;
			default:
				if ("@@IDENTITY".equals(token.asString())) {
					reportIssue();
					setLineNumber(token.getPosition().getLine());
				}
				break;
			}
		}
	}

	@Override
	public String description() {		
		return "Consider using SCOPE_IDENTITY instead of @@IDENTITY";
	}
	
	public String getId() {		
		return "SR0008";
	}
}

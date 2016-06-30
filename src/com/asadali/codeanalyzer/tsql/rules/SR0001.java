package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0001 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {		
		searchReductionTree(root);
	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<SelectQuery>".equals(token.asReduction().getParent().getHead().toString())) {
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
				if ("<ColumnItemList>".equals(token.asReduction().getParent()
						.getHead().toString())) {
					checkForWildCardAsterix(token.asReduction());
				} else {
					searchReductionTree(token.asReduction());
				}
				break;
			}

		}

	}

	private void checkForWildCardAsterix(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				checkForWildCardAsterix(token.asReduction());
				break;
			default:
				if ("*".equals(token.asString())) {
					reportIssue();
					setLineNumber(token.getPosition().getLine());
				}
				break;
			}

		}

	}

	@Override
	public String description() {
		return "Avoid SELECT * in stored procedures, views, and table-valued functions";
	}

	
	public String getId() {		
		return "SR0001";
	}

	
}
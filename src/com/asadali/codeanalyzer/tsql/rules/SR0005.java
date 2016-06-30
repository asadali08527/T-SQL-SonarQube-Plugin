package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0005 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<PredicateCompare>".equals(token.asReduction().getParent().getHead().toString())) {

					if (token.asReduction().getParent().getHandle()
							.findByName("LIKE") != null) {
						checkForRule(token.asReduction());
					}

					// System.out.println("Ended SelectQuery Search");
				} else {
					searchReductionTree(token.asReduction());
				}
				break;
			default:
				break;
			}
		}
	}

	private void checkForRule(Reduction reduction) {
		// predicate is written as where [expression like expression]
		// considering expression as the 0th position of token arraylist,
		// like in 1st position
		// expression containing % in the 2nd position
		// hence searching for 2nd position token
		Token expression = reduction.get(2);
		//System.out.println(expression.asReduction().getParent());
		checkExpressionForRule(expression.asReduction());
	}

	private void checkExpressionForRule(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				checkExpressionForRule(token.asReduction());
				break;
			default:
				if (token.asString().startsWith("'%")) {
					reportIssue();	
					setLineNumber(token.getPosition().getLine());
				}
				break;
			}
		}

	}

	@Override
	public String description() {
		return "Avoid using patterns that start with �%� in LIKE predicates";
	}

	public String getId() {		
		return "SR0005";
	}
}

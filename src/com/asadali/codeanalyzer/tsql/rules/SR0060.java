package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0060 extends AbstractSyntaxTreeBasedRule{
	
	@Override
	protected void checkForVoilation(Reduction root) {
		int count = 0;
		searchReductionTree(root, count);
	}

	private int searchReductionTree(Reduction reduction, int count) {

		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<OrderClause>".equals(token.asReduction().getParent().getHead().toString()) || "<GroupClause>".equals(token.asReduction().getParent().getHead().toString())) {

					if (token.asReduction().getParent().getHandle().findByName("group") != null) {
						count++;
					} else if (token.asReduction().getParent().getHandle().findByName("order") != null) {
						count++;
					}

					if (count >= 3) {
						reportIssue();
						setLineNumber(token.asReduction().get(0).getPosition()
								.getLine());
					}

				} else {
					count = searchReductionTree(token.asReduction(), count);
				}
				break;

			}
		}

		return count;
	}

	@Override
	public String description() {
		return "Use of multiple Group BY and Order BY should be avoided";
	}

	@Override
	public String getId() {
		return "SR00059";
	}

}
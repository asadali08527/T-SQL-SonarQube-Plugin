package com.asadali.codeanalyzer.tsql.rules;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0059 extends AbstractSyntaxTreeBasedRule{

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:
	
					if ("<GroupClause>".equals(token.asReduction().getParent().getHead().toString())) {
						checkForRule(token.asReduction() , 0);
					} else if ("<OrderClause>".equals(token.asReduction().getParent().getHead().toString())) {				
						checkForRule(token.asReduction() , 0);
					} else {
						searchReductionTree(token.asReduction());
					}
					break;
				}
		}
	}

	private int checkForRule(Reduction reduction, int count) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:

				if ("Id".equals(token.asReduction().getParent().getHandle().toString())	|| "IntegerLiteral".equals(token.asReduction().getParent().getHandle().toString())) {
					count++;
					/*if (token.asReduction().getParent().getHandle().findByName("id") != null) {
						
					} else if (token.asReduction().getParent().getHandle().findByName("IntegerLiteral") != null) {
						numeralCount++;
						numeralCount = numeralCount + count;
					}*/

					if (count >= 3) {
						reportIssue();
						setLineNumber(token.asReduction().get(0).getPosition().getLine());
					}

				} else {
					count = checkForRule(token.asReduction(), count);
				}
				break;
			default:
				break;
				//searchReductionTree(reduction);

			}
		}
		return count;
	}

	@Override
	public String description() {
		return "excessive use of ORDER BY and GROUP BY on attribute should be avoided";
	}

	@Override
	public String getId() {

		return "SR00059";
	}
}
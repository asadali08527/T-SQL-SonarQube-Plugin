package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0057 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}
	
	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<IfStatement>".equals(token.asReduction().getParent().getHead().toString())) {
						checkForRule(token.asReduction(), 1);					
					} else {
						searchReductionTree(token.asReduction());
					}
					break;
			default:
				break;
			}
		}
	}
	
	private void checkForRule(Reduction reduction, int count) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<IfStatement>".equals(token.asReduction().getParent().getHead().toString())) {
						count = count+1;
						System.out.println("count :: " + count);
						if(count > 3) {
							reportIssue();							
							Token token1 = token.asReduction().get(0); // returns the token "IF"
							setLineNumber(token1.getPosition().getLine());															
						} else {
							checkForRule(token.asReduction(), count);
						}							
																	
					} else {
						checkForRule(token.asReduction(),count);
					}
					break;
			default:
				break;
			}
		}
	}
	
	@Override
	public String description() {
		return "Avoid more than 3 NESTED IF conditions";
	}

	@Override
	public String getId() {
		return "SR0057";
	}

}

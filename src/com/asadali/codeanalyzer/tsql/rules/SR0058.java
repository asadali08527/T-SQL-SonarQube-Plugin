package com.asadali.codeanalyzer.tsql.rules;
import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;
public class SR0058 extends AbstractSyntaxTreeBasedRule{
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
				if(reduction.getParent().getHandle().findByName("WHERE") != null)
				{			
					checkForRule(reduction);
				}
				else if(reduction.getParent().getHandle().findByName("HAVING") != null)
				{					
					checkForNotRule(reduction);
				}
				break;
			}
		}
	}
	private void checkForRule(Reduction reduction) {
		for(Token token:reduction){
			
			switch (token.getType()) {
			case NON_TERMINAL:
				if("<SelectQuery>".equals(token.asReduction().getParent().getHead().toString()))
				{
					
					searchReductionTree(token.asReduction());
				}
				else
					checkForNotRule(reduction);
			
				break;
			default:
				
				break;
			}
		}
	}
	private void checkForNotRule(Reduction asReduction) {
		for(Token token:asReduction){
			
			switch (token.getType()) {
			case NON_TERMINAL:
				checkForRule(token.asReduction());
				break;
			default:
				
				
				 if ("<>".equals(token.asString())||"!=".equals(token.asString())||"NOT".equals(token.asString())) {
					reportIssue();	
					setLineNumber(token.getPosition().getLine());
								}
								break;
			}
	}
	}
	@Override
	public String description() {
		return "Use of NOT,<> ,!= should be avoided";
	}
	@Override
	public String getId() {
		
		return "SR0058";
	}
}
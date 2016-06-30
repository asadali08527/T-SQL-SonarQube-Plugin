package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.SymbolList;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0010 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTreeForFromClause(root);

	}

	 private void searchReductionTreeForFromClause(Reduction reduction) {
	        for (Token token : reduction) {
	            switch (token.getType()) {
	                case NON_TERMINAL:	 

	                	if("<SynSourceChain>".equals(token.asReduction().getParent().getHead().toString())) {	                		
	                		SymbolList list = token.asReduction().getParent().getHandle();
	                		if(list.findByName(",") != null) {
	                			reportIssue();
	                			//setLineNumber(token.getPosition().getLine());
	                		}

	                	} else {
	                		searchReductionTreeForFromClause(token.asReduction());
	                	}
	                    break;         
	            }
	        }
	 }

	@Override
	public String description() {		
		return "Avoid using deprecated syntax when you join tables or views";
	}
	
	public String getId() {		
		return "SR0010";
	}
}

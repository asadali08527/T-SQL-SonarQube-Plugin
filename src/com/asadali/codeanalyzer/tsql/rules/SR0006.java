package com.asadali.codeanalyzer.tsql.rules;


import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;


public class SR0006 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		reset();
		searchReductionTree(root);
		
		
	}


	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<PredicateCompare>".equals(token.asReduction().getParent().getHead().toString()) && token.asReduction().getParent().getHandle().findByName("Expression") != null) {
						checkForRule(token.asReduction());
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
		// IN / LIKE / NOT IN etc in 1st position
		// expression again in the 2nd position
		// hence searching for 0th position token
		Token expression1 = reduction.get(0);
		Token expression2 = reduction.get(2);
		boolean returnValForExpression1 = checkExpressionForForcingColumnIndex(expression1.asReduction());
		boolean returnValForExpression2 = false;
		if (reduction.getParent().getHandle().findByName("IN") != null || reduction.getParent().getHandle().findByName("NOT IN") != null) {
			//System.out.println(expression1.asReduction().getParent());
			searchReductionTree(expression2.asReduction());
		} else {
			returnValForExpression2 = checkExpressionForForcingColumnIndex(expression2.asReduction());
		}
		if(returnValForExpression1 || returnValForExpression2 ) {
			reportIssue();	
			setLineNumber(expression1.getPosition().getLine());
		}
	}


	private boolean checkExpressionForForcingColumnIndex(Reduction reduction) {
		
		if(reduction != null && reduction.size() > 0) {
			//System.out.println("Level -> " + reduction.getParent());
			boolean isMultipleTermsAvailable = checkIfExpressionContainsMultipleTerms(reduction.get(0).asReduction());
			boolean isColumnNamePresent = checkIfColumnNamePartOfExpression(reduction);
			//System.out.println(" isColumnNamePresent -> " + isColumnNamePresent);
			//System.out.println(" isMultipleTermsAvailable -> " + isMultipleTermsAvailable);
			return (isColumnNamePresent && isMultipleTermsAvailable);
		} else {
			return false;
		}
		
	}

	private boolean checkIfColumnNamePartOfExpression(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ColumnName>".equals(token.asReduction().getParent().getHead().toString())){
					//System.out.println("returning true");
					return true;
				} else {
					return checkIfColumnNamePartOfExpression(token.asReduction());
				}				
			default:
				// do nothing
				break;
			}
		}	
		return false;
	}


	private boolean checkIfExpressionContainsMultipleTerms(Reduction reduction) {
		return (reduction.getParent().getHandle().size() != 1);			
	}


	@Override
	public String description() {
		return "Move a column reference to one side of a comparison operator to use a column index.";
	}
	
	public String getId() {		
		return "SR0006";
	}
}

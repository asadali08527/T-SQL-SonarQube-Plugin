package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0015 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		reset();
		searchReductionTree(root);
	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<PredicateCompare>".equals(token.asReduction().getParent().getHead().toString())) {
						checkForRule(token.asReduction());					
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
		if(reduction.getParent().getHandle().size() > 3) {
			// a predicate compare is broken down as expression <operator> expression		
			// we need to check if any of the expressions contain a function call with variable name
			Token expression1 = reduction.get(0);
			Token expression2 = reduction.get(2);		
			checkIfExpressionContainsDeterministicFunctions(expression1.asReduction());
			if (reduction.getParent().getHandle().findByName("IN") != null || reduction.getParent().getHandle().findByName("NOT IN") != null) {			
				searchReductionTree(expression2.asReduction());
			} else {
				checkIfExpressionContainsDeterministicFunctions(expression2.asReduction());
			}		
		} else {
			searchReductionTree(reduction);
		}
	}
	
	
	private void checkIfExpressionContainsDeterministicFunctions(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<NamedFunction>".equals(token.asReduction().getParent().getHead().toString())) {					
					checkIfFunctionCallHasVariableInParam(token.asReduction());
					
				} else {
					checkIfExpressionContainsDeterministicFunctions(token.asReduction());
				}
				break;
			default:
				break;
			}
		}		
	}
	
	private void checkIfFunctionCallHasVariableInParam(Reduction reduction) {
		// <NamedFunction> ::= <FunctionName> '(' ')'  | <FunctionName> '(' <ExpressionList> ')'
		// from above we need to check for the latter		
		if(reduction.size() == 4) {
			// check in expressionList if it contains tag for variableName
			Token expressionList = reduction.get(2);
			checkForVariableName(expressionList.asReduction());
		}
			
	}

	private void checkForVariableName(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:				
					if ("<VariableName>".equals(token.asReduction().getParent().getHead().toString())) {
						reportIssue();						
						setLineNumber(token.asReduction().get(0).getPosition().getLine());
					} else {
						checkForVariableName(token.asReduction());
					}
					break;
			default:
				break;
			}
		}
		
	}
	

	@Override
	public String description() {
		return "Extract deterministic function calls from WHERE predicates";
	}

	@Override
	public String getId() {
		return "SR0015";
	}

}

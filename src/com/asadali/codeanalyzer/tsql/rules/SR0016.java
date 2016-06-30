package com.asadali.codeanalyzer.tsql.rules;


import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0016 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);

	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:				
				if("<CreateOrAlterProcedureStatement>".equals(token.asReduction().getParent().getHead().toString())) {            		
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
	
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ProcedureNameQualified>".equals(token.asReduction().getParent()
						.getHead().toString())) {
					processProcedureName(token.asReduction());				 
				} 
				break;
			default:
				break;
			}
		}
	}
	
	private void processProcedureName(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ProcedureName>".equals(token.asReduction().getParent()
						.getHead().toString())) {					
					processObjectName(token.asReduction());
				 
				} 
				break;
			default:
				break;
			}
		}
	}

	private void processObjectName(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ObjectName>".equals(token.asReduction().getParent().getHead().toString())) {					
					processName(token.asReduction());
				}
				break;
			default:
				break;
			}
		}	
		
	}


	private void processName(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
				case NON_TERMINAL:
						// do nothing ..we are intrested in the name of it
				 default:
					 String columnNameValue = getColumnNameAsString(token.asString());
		
					 if(columnNameValue.startsWith("sp_")) {
						 reportIssue();
						 setLineNumber(token.getPosition().getLine());
					 }
					 break;
			}
		}	
		
	}


	private String getColumnNameAsString(String columnNameValue) {
		//check if the name starts []
		if(columnNameValue != null) {
			columnNameValue = columnNameValue.trim();
		}
		if('[' == columnNameValue.charAt(0)) {
			columnNameValue = columnNameValue.substring(1, (columnNameValue.length() - 2));
		}
		return columnNameValue;		
	}

	@Override
	public String description() {
		return "Avoid using sp_ as a prefix for stored procedures";
	}
	
	public String getId() {		
		return "SR0016";
	}
}

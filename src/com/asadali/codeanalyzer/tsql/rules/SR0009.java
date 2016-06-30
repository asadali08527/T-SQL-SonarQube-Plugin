package com.asadali.codeanalyzer.tsql.rules;



import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0009 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);

	}

	
	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:				
				if("<CreateTableStatement>".equals(token.asReduction().getParent().getHead().toString())) {
            		//System.out.println("Create Table Found");
            		checkForRule(token.asReduction());
            		//System.out.println("Ended SelectQuery Search");
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
				if ("<TableDefinitionGroup>".equals(token.asReduction().getParent().getHead().toString())) {					
					processTableDefinitionGroup(token.asReduction());
				} else {
					searchReductionTree(token.asReduction());
				}
				break;
			}

		}
	}

	
	
	private void processTableDefinitionGroup(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<TableDefinitionList>".equals(token.asReduction().getParent().getHead().toString())) {					
					processTableDefinitionList(token.asReduction());
				} else {
					searchReductionTree(token.asReduction());
				}
				break;
			}

		}
	}


	private void processTableDefinitionList(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
					if ("<TableDefinition>".equals(token.asReduction().getParent().getHead().toString())) {						
						processColumnName(token.asReduction());
					} else if ("<TableDefinitionList>".equals(token.asReduction().getParent().getHead().toString())) {
						processTableDefinitionList(token.asReduction());
					} else {
						// nothing for now
					}						
					break;
				default:
						// do nothing for now
						break;
			
			}
		}	
		
	}

	private void processColumnName(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ColumnName>".equals(token.asReduction().getParent().getHead().toString())) {					
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
						 
						 if(columnNameValue.length() <= 2) {
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
		return "Avoid using types of variable length that are size 1 or 2";
	}
	
	public String getId() {		
		return "SR0009";
	}
}

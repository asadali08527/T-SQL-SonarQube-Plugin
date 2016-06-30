package com.asadali.codeanalyzer.tsql.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;

public class SR0011 extends AbstractSyntaxTreeBasedRule {

	private Pattern specialCharacterPattern = null;
	
	public SR0011() {
		specialCharacterPattern = Pattern.compile(".*?[\"\\s'\\[\\]].*?$");
	}
	
	
	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);

	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:				
				if("<ObjectName>".equals(token.asReduction().getParent().getHead().toString())) {            		
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
						// do nothing ..we are interested in the name of it
						break;
					 default:
						 String columnNameValue = getColumnNameAsString(token.asString());
						 Matcher matcher = specialCharacterPattern.matcher(columnNameValue);
						 if(matcher.matches()) {
							 reportIssue();
							 setLineNumber(token.getPosition().getLine());
						 }
						 break;
			}
		}	
		
	}


	private String getColumnNameAsString(String columnNameValue) {
		//check if the name starts []		
		if('[' == columnNameValue.charAt(0)) {
			columnNameValue = columnNameValue.substring(1, (columnNameValue.length() - 2));
		}		
		return columnNameValue;
		
	}


	@Override
	public String description() {		
		return "Avoid using special characters in object names";
	}
	
	public String getId() {		
		return "SR0011";
	}

}

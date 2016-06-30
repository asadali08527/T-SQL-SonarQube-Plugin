package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.asadali.sonar.tsql.TSQLRulesCheckList;

@Rule(key = "SR0062", priority = Priority.MAJOR)
@BelongsToProfile(title = TSQLRulesCheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class SR0062 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);

	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<CreateIndexStatement>".equals(token.asReduction()
						.getParent().getHead().toString())) {
					processIndexName(token.asReduction());
				} else {
					searchReductionTree(token.asReduction());
				}
				break;
			default:
				break;

			}
		}

	}

	private void processIndexName(Reduction asReduction) {
		for (Token token : asReduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<IndexName>".equals(token.asReduction().getParent()
						.getHead().toString())) {
					checkForIndexName(token.asReduction());

				}
			}

		}

	}

	private void checkForIndexName(Reduction asReduction) {
		for (Token token : asReduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<ObjectName>".equals(token.asReduction().getParent()
						.getHead().toString())) {
					processName(token.asReduction());

				}
			}
		}
	}

	private void processName(Reduction asReduction) {
		for (Token token : asReduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				// Do nothing as we are interested to check index name
				break;
			default:
				if (token.asString().startsWith("IX_")) {
					return;
				} else {
					reportIssue();
					setLineNumber(token.getPosition().getLine());
				}
				break;

			}
		}

	}

	@Override
	public String description() {

		return "Indexed should be named as IX_<table name>_<field names> separated by underscores";
	}

	@Override
	public String getId() {

		return "SR0062";
	}

}

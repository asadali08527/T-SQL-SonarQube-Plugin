package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.asadali.sonar.tsql.TSQLRulesCheckList;

@Rule(key = "SR0063", priority = Priority.MAJOR)
@BelongsToProfile(title = TSQLRulesCheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class SR0063 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);
	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<SelectQuery>".equals(token.asReduction().getParent()
						.getHead().toString())) {

					checkForRule(token.asReduction());
				} else {
					searchReductionTree(token.asReduction());
				}
			}
		}

	}

	private void checkForRule(Reduction asReduction) {
		for (Token token : asReduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				if ("<IntoClause>".equals(token.asReduction().getParent()
						.getHead().toString())) {
					if (token.asReduction().getParent().getHandle()
							.findByName("INTO") != null) {
						reportIssue();
						setLineNumber(token.asReduction().get(0).getPosition()
								.getLine());
					}
				}

			}
		}

	}

	@Override
	public String description() {
		return "The SELECT… INTO syntax cannot be used";
	}

	@Override
	public String getId() {
		return "SR0063";
	}

}

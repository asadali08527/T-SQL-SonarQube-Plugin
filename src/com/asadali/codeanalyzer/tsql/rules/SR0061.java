package com.asadali.codeanalyzer.tsql.rules;

import com.creativewidgetworks.goldparser.engine.Reduction;
import com.creativewidgetworks.goldparser.engine.Token;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.asadali.sonar.tsql.TSQLRulesCheckList;

@Rule(key = "SR0061", priority = Priority.MAJOR)
@BelongsToProfile(title = TSQLRulesCheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class SR0061 extends AbstractSyntaxTreeBasedRule {

	@Override
	protected void checkForVoilation(Reduction root) {
		searchReductionTree(root);

	}

	private void searchReductionTree(Reduction reduction) {
		for (Token token : reduction) {
			switch (token.getType()) {
			case NON_TERMINAL:

				if ("<CreateTableStatement>".equals(token.asReduction()
						.getParent().getHead().toString())
						|| "<CreateOrAlterProcedureStatement>"
								.equals(token.asReduction().getParent()
										.getHead().toString())
						|| "<CreateOrAlterFunctionStatement>"
								.equals(token.asReduction().getParent()
										.getHead().toString())
						|| "<CreateTriggerStatement>"
								.equals(token.asReduction().getParent()
										.getHead().toString())
						|| "<CreateTypeStatement>".equals(token.asReduction()
								.getParent().getHead().toString())
						|| "<CreateOrAlterViewStatement>"
								.equals(token.asReduction().getParent()
										.getHead().toString())
						|| "<CreateXmlSchemaCollectionStatement>"
								.equals(token.asReduction().getParent()
										.getHead().toString())) {
					checkForRule(token.asReduction());
				} else

				{
					searchReductionTree(token.asReduction());
				}
				break;
			}
		}

	}

	private void checkForRule(Reduction asReduction) {
		for (Token token : asReduction) {
			switch (token.getType()) {
			case NON_TERMINAL:
				System.out.println(token.asReduction());
				if ("<TableName>".equals(token.asReduction().getParent()
						.getHandle().toString())
						|| "<ProcedureName>".equals(token.asReduction()
								.getParent().getHandle().toString())
						|| "<FunctionName>".equals(token.asReduction()
								.getParent().getHandle().toString())
						|| "<TriggerName>".equals(token.asReduction()
								.getParent().getHandle().toString())
						|| "<SimpleTypeName>".equals(token.asReduction()
								.getParent().getHandle().toString())
						|| "<ViewName>".equals(token.asReduction().getParent()
								.getHandle().toString())
						|| "<XmlSchemaCollectionName>".equals(token
								.asReduction().getParent().getHandle()
								.toString())) {
					reportIssue();
					// setLineNumber(token.asReduction().get(0).getPosition().getLine());
				}
				break;
			}

		}

	}

	@Override
	public String description() {

		return "CREATE statements name must be in this format <SchemaName>.<ObjectName>";
	}

	@Override
	public String getId() {

		return "SR0061";
	}

}

package com.asadali.sonar.tsql;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.Violation;

import com.asadali.codeanalyzer.reporting.RuleViolation;

public class TSQLViolationToRuleViolation implements BatchExtension {

	private final Project project;
	private final RuleFinder ruleFinder;

	public TSQLViolationToRuleViolation(Project project, RuleFinder ruleFinder) {
		this.project = project;
		this.ruleFinder = ruleFinder;
	}

	public Violation toViolation(String filePath, RuleViolation tsqlRuleViolation, SensorContext context) {
		Resource resource = findResourceFor(filePath);
		if (context.getResource(resource) == null) {			
			// Save violations only for existing resources
			return null;
		}

		Rule rule = findRuleFor(tsqlRuleViolation);
		if (rule == null) {			
			// Save violations only for enabled rules
			return null;
		}

		int lineId = tsqlRuleViolation.getLineNumber();
		String message = tsqlRuleViolation.getDescription();

		return Violation.create(rule, resource).setLineId(lineId).setMessage(message);
	}

	private Resource findResourceFor(String tsqlFilePath) {
		return File.fromIOFile(new java.io.File(tsqlFilePath), project);
	}

	private Rule findRuleFor(RuleViolation violation) {
		String ruleKey = violation.getRuleId();
		Rule rule = ruleFinder.findByKey(TSQLConstants.REPOSITORY_KEY, ruleKey);
		return rule;

	}

}

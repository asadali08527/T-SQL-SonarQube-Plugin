package com.asadali.sonar.tsql;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.XmlParserException;

import com.asadali.sonar.tsql.TSQL;
import com.asadali.sonar.tsql.TSQLExecutor;
import com.asadali.sonar.tsql.TSQLViolationToRuleViolation;
import com.asadali.codeanalyzer.reporting.FileSummary;
import com.asadali.codeanalyzer.reporting.Report;
import com.asadali.codeanalyzer.reporting.RuleViolation;

public class TSQLSensor implements Sensor {

	private static final Logger LOG = LoggerFactory.getLogger(TSQLSensor.class);
	private final RulesProfile profile;
	private final TSQLExecutor executor;
	private final TSQLViolationToRuleViolation tsqlViolationToRuleViolation;

	private Settings settings;

	/**
	 * Use of IoC to get Settings
	 */
	public TSQLSensor(RulesProfile profile, TSQLExecutor executor, TSQLViolationToRuleViolation tsqlViolationToRuleViolation) {
		this.profile = profile;
		this.executor = executor;
		this.tsqlViolationToRuleViolation = tsqlViolationToRuleViolation;
	}

	public boolean shouldExecuteOnProject(Project project) {
		// This sensor is executed on any type of projects
		return (!project.getFileSystem().mainFiles(TSQL.KEY).isEmpty());
	}

	public void analyse(Project project, SensorContext sensorContext) {
		try {
			Report report = executor.execute();
			reportViolations(report.iterate(), sensorContext);
		} catch (Exception e) {
			throw new XmlParserException(e);
		}

	}

	private void reportViolations(Iterator<FileSummary> iterator,
			SensorContext context) {

		while (iterator.hasNext()) {
			FileSummary fileSummary = iterator.next();
			if (fileSummary.areViolationsReported()) {
				String filePath = fileSummary.getFilePath();
				System.out.println("File Name -> " + filePath );
				for (Iterator<RuleViolation> ruleViolationsIterator = fileSummary.violations(); ruleViolationsIterator.hasNext();) {
					RuleViolation ruleViolation = ruleViolationsIterator.next();
					System.out.println("Found Violation");
					Violation violation = tsqlViolationToRuleViolation.toViolation(filePath, ruleViolation, context);
					if (null != violation) {
						System.out.println("Done conversion of Violation");
						context.saveViolation(violation);
					}
				}

			}
		}

	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}

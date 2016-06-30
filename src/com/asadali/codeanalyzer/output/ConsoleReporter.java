package com.asadali.codeanalyzer.output;

import java.util.Iterator;

import com.asadali.codeanalyzer.reporting.FileSummary;
import com.asadali.codeanalyzer.reporting.Report;
import com.asadali.codeanalyzer.reporting.RuleViolation;

public class ConsoleReporter implements Reporter {

	public void report(Report report) {
		System.out.println("***************************************");
		System.out.println("Total Lines of Code Scanned :: " + report.getLinesOfCodeAnalyzed());
		for (Iterator<FileSummary> iterator = report.iterate(); iterator.hasNext();) {
			FileSummary summary = iterator.next();
			System.out.println("File Name : " + summary.getFileName());
			System.out.println("Voilations found ?? " + summary.areViolationsReported());
			for (Iterator<RuleViolation> iterator2 = summary.violations(); iterator2.hasNext();) {
				RuleViolation violation = iterator2.next();
				System.out.println(violation.getRuleId() + " :: " + violation.getDescription() + " @ Line : " + violation.getLineNumber() );				
			}			
		}		
		System.out.println("***************************************");

	}

}

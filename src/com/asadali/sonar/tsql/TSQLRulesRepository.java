package com.asadali.sonar.tsql;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.platform.ServerFileSystem;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

public class TSQLRulesRepository extends RuleRepository  {

	private final ServerFileSystem fileSystem;
	private final XMLRuleParser xmlRuleParser;
	  
	public TSQLRulesRepository(ServerFileSystem fileSystem, XMLRuleParser xmlRuleParser) {
		super(TSQLConstants.REPOSITORY_KEY, TSQL.KEY);
		setName(TSQLConstants.REPOSITORY_NAME);
	    this.fileSystem = fileSystem;
	    this.xmlRuleParser = xmlRuleParser;
	}

	@Override
	public List<Rule> createRules() {
		List<Rule> rules = new ArrayList<Rule>();
	    rules.addAll(xmlRuleParser.parse(getClass().getResourceAsStream("/TSQL-CodeAnalyzer/resources/com/synechron/codeanalyzer/tsql/rules.xml")));
		System.out.println("Rules fetched " + rules.size());		
	    return rules;
	}

}

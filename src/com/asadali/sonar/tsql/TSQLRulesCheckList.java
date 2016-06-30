package com.asadali.sonar.tsql;

import java.util.ArrayList;
import java.util.List;

import com.asadali.codeanalyzer.tsql.rules.SR0001;
import com.asadali.codeanalyzer.tsql.rules.SR0005;
import com.asadali.codeanalyzer.tsql.rules.SR0006;
import com.asadali.codeanalyzer.tsql.rules.SR0008;
import com.asadali.codeanalyzer.tsql.rules.SR0009;
import com.asadali.codeanalyzer.tsql.rules.SR0010;
import com.asadali.codeanalyzer.tsql.rules.SR0011;
import com.asadali.codeanalyzer.tsql.rules.SR0016;
import com.asadali.codeanalyzer.tsql.rules.SR0051;
import com.asadali.codeanalyzer.tsql.rules.SR0052;
import com.asadali.codeanalyzer.tsql.rules.SR0053;
import com.asadali.codeanalyzer.tsql.rules.SR0054;
import com.asadali.codeanalyzer.tsql.rules.SR0055;
import com.asadali.codeanalyzer.tsql.rules.SR0056;
import com.asadali.codeanalyzer.tsql.rules.SR0057;
import com.asadali.codeanalyzer.tsql.rules.SR0058;
import com.asadali.codeanalyzer.tsql.rules.SR0059;
import com.asadali.codeanalyzer.tsql.rules.SR0060;
import com.asadali.codeanalyzer.tsql.rules.SR0061;
import com.asadali.codeanalyzer.tsql.rules.SR0062;
import com.asadali.codeanalyzer.tsql.rules.SR0063;

public class TSQLRulesCheckList {

	public static final String REPOSITORY_KEY = "tsql";

	public static final String REPOSITORY_NAME = "SonarQube";

	public static final String SONAR_WAY_PROFILE = "Sonar way";

	private TSQLRulesCheckList() {
		 super();
	}

	public static List<Class> getRules() {
		List<Class> rulesList = new ArrayList<Class>(21);
		
		rulesList.add(SR0001.class);
		rulesList.add(SR0005.class);
		rulesList.add(SR0006.class);
		rulesList.add(SR0008.class);
		rulesList.add(SR0009.class);
		rulesList.add(SR0010.class);
		rulesList.add(SR0011.class);
		rulesList.add(SR0016.class);
		rulesList.add(SR0051.class);
		rulesList.add(SR0052.class);
		rulesList.add(SR0053.class);
		rulesList.add(SR0054.class);
		rulesList.add(SR0055.class);
		rulesList.add(SR0056.class);
		rulesList.add(SR0057.class);
		rulesList.add(SR0058.class);
		rulesList.add(SR0059.class);
		rulesList.add(SR0060.class);
		rulesList.add(SR0061.class);
		rulesList.add(SR0062.class);
		rulesList.add(SR0063.class);
		return rulesList;
	}
	 
}

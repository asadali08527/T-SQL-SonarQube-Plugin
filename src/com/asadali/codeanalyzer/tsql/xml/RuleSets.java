package com.asadali.codeanalyzer.tsql.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rulesets")
public class RuleSets {
	
	private List<RuleSet> ruleset;
    
    @XmlElement(name = "ruleset")
	public List<RuleSet> getRuleset() {
		return ruleset;
	}
   
	public void setRuleset(List<RuleSet> ruleset) {
		this.ruleset = ruleset;
	}
	

}

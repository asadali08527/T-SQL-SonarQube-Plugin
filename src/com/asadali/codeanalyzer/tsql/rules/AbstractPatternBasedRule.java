package com.asadali.codeanalyzer.tsql.rules;

import com.asadali.codeanalyzer.rules.Type;
import com.creativewidgetworks.goldparser.engine.Reduction;

public abstract class AbstractPatternBasedRule extends AbstractRule<Reduction> {

	public AbstractPatternBasedRule() {
		super(Type.PATTERN_BASED);
	}
	
	@Override
	protected abstract void checkForVoilation(Reduction root); 

	@Override
	public abstract String description();

	@Override
	public abstract String getId(); 

}

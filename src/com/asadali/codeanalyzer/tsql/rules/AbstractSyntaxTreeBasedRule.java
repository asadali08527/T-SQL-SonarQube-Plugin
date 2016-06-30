package com.asadali.codeanalyzer.tsql.rules;

import com.asadali.codeanalyzer.rules.Type;
import com.creativewidgetworks.goldparser.engine.Reduction;

public abstract class AbstractSyntaxTreeBasedRule extends AbstractRule<Reduction> {

	public AbstractSyntaxTreeBasedRule() {
		super(Type.AST_BASED);
	}
	
	@Override
	protected abstract void checkForVoilation(Reduction root); 

	@Override
	public abstract String description();

	@Override
	public abstract String getId(); 

}

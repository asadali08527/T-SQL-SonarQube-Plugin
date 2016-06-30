package com.asadali.sonar.tsql;

import org.sonar.api.profiles.AnnotationProfileParser;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;

public class TSQLProfile extends ProfileDefinition {
	
	private final AnnotationProfileParser annotationProfileParser;

	  public TSQLProfile(AnnotationProfileParser annotationProfileParser) {
	    this.annotationProfileParser = annotationProfileParser;
	  }

	  @Override
	  public RulesProfile createProfile(ValidationMessages validation) {
	    return annotationProfileParser.parse(TSQLRulesCheckList.REPOSITORY_KEY, TSQLRulesCheckList.SONAR_WAY_PROFILE, TSQL.KEY, TSQLRulesCheckList.getRules(), validation);
	  }


}

package com.asadali.sonar.tsql;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;

/**
 * This class is the entry point for all extensions
 */

public final class TSQLPlugin extends SonarPlugin {



  // This is where you're going to declare all your Sonar extensions
  public List getExtensions() {
    return Arrays.asList(
    	// language	
        TSQL.class,
        // Batch
        TSQLSensor.class,
        TSQLExecutor.class,
        TSQLRulesRepository.class,
        TSQLProfile.class,
        TSQLViolationToRuleViolation.class);
  }
}

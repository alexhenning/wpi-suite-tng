/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * @author jpalnick
 *
 */
public class RequirementExclusionStrategy implements ExclusionStrategy {
//  private final Class<?> fieldsToSkip;

  private RequirementExclusionStrategy() {
//    this.typeToSkip = typeToSkip;
  }

  public boolean shouldSkipClass(Class<?> clazz) {
//    return (clazz == typeToSkip);
  	return false;
  }

  public boolean shouldSkipField(FieldAttributes f) {
    return f.getAnnotation(ExcludeField.class) != null;
  }
}
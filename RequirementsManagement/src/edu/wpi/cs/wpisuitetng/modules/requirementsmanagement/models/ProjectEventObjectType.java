package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

public enum ProjectEventObjectType {
	REQUIREMENT ("requirementmodel", "id");
//	REQUIREMENT ("requirementmodel", RequirementModel.class);
//
	private final String modelName;
    private final String idFieldName;
    ProjectEventObjectType(String modelName, String idFieldName) {
        this.idFieldName = idFieldName;
        this.modelName = modelName;
    }
    private String modelName() { return modelName; }
    private String idFieldName() { return idFieldName; }

//	REQUIREMENT ("requirementmodel", RequirementModel.class);
//
//	private final String modelName;
//    private final Class<?> modelClass;
//    ProjectEventObjectType(String modelName, Class<?> modelClass) {
//        this.modelClass = modelClass;
//        this.modelName = modelName;
//    }
//    private String modelName() { return modelName; }
//    private Class<?> modelClass() { return modelClass; }
}
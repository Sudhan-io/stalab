package com.sta.defect;

public class DefectModel {
    private final String defectId;
    private final String module;
    private final String title;
    private final String severity; // Critical, Major, Moderate, Minor
    private final String priority; // P1 - High, P2 - Medium, P3 - Low
    private final String stepsToReproduce;
    private final String expectedResult;
    private final String actualResult;
    private final String status;

    public DefectModel(String defectId, String module, String title, String severity,
                       String priority, String stepsToReproduce, String expectedResult,
                       String actualResult, String status) {
        this.defectId = defectId;
        this.module = module;
        this.title = title;
        this.severity = severity;
        this.priority = priority;
        this.stepsToReproduce = stepsToReproduce;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
        this.status = status;
    }

    public String getDefectId() { return defectId; }
    public String getModule() { return module; }
    public String getTitle() { return title; }
    public String getSeverity() { return severity; }
    public String getPriority() { return priority; }
    public String getStepsToReproduce() { return stepsToReproduce; }
    public String getExpectedResult() { return expectedResult; }
    public String getActualResult() { return actualResult; }
    public String getStatus() { return status; }
}

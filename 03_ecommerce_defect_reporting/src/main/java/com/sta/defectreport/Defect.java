package com.sta.defectreport;

public class Defect {
    private String defectId;
    private String description;
    private String stepsToReproduce;
    private String severity; // Critical, Major, Moderate, Minor
    private String priority; // P1, P2, P3, P4
    private String expectedResult;
    private String actualResult;
    private String status;   // OPEN, IN_PROGRESS, RESOLVED, CLOSED

    public Defect(String defectId, String description, String stepsToReproduce,
                  String severity, String priority, String expectedResult,
                  String actualResult, String status) {
        this.defectId = defectId;
        this.description = description;
        this.stepsToReproduce = stepsToReproduce;
        this.severity = severity;
        this.priority = priority;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
        this.status = status;
    }

    public String getDefectId() { return defectId; }
    public String getDescription() { return description; }
    public String getStepsToReproduce() { return stepsToReproduce; }
    public String getSeverity() { return severity; }
    public String getPriority() { return priority; }
    public String getExpectedResult() { return expectedResult; }
    public String getActualResult() { return actualResult; }
    public String getStatus() { return status; }
}


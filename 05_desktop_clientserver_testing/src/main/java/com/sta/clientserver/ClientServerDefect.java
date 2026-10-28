package com.sta.clientserver;

public class ClientServerDefect {
    private String defectId;
    private String category; // FUNCTIONAL or USABILITY
    private String title;
    private String stepsToReproduce;
    private String severity; // Critical, Major, Moderate, Minor
    private String priority; // P1, P2, P3, P4
    private String expectedResult;
    private String actualResult;

    public ClientServerDefect(String defectId, String category, String title,
                              String stepsToReproduce, String severity, String priority,
                              String expectedResult, String actualResult) {
        this.defectId = defectId;
        this.category = category;
        this.title = title;
        this.stepsToReproduce = stepsToReproduce;
        this.severity = severity;
        this.priority = priority;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
    }

    public String getDefectId() { return defectId; }
    public String getCategory() { return category; }
    public String getTitle() { return title; }
    public String getStepsToReproduce() { return stepsToReproduce; }
    public String getSeverity() { return severity; }
    public String getPriority() { return priority; }
    public String getExpectedResult() { return expectedResult; }
    public String getActualResult() { return actualResult; }
}


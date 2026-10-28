package com.sta.testcases;

public class TestCase {
    private String id;
    private String module;
    private String scenario;
    private String precondition;
    private String testSteps;
    private String testData;
    private String expectedResult;
    private String actualResult;
    private String status; // PASS, FAIL, BLOCKED

    public TestCase(String id, String module, String scenario, String precondition,
                    String testSteps, String testData, String expectedResult) {
        this.id = id;
        this.module = module;
        this.scenario = scenario;
        this.precondition = precondition;
        this.testSteps = testSteps;
        this.testData = testData;
        this.expectedResult = expectedResult;
        this.status = "NOT_RUN";
    }

    public String getId() { return id; }
    public String getModule() { return module; }
    public String getScenario() { return scenario; }
    public String getPrecondition() { return precondition; }
    public String getTestSteps() { return testSteps; }
    public String getTestData() { return testData; }
    public String getExpectedResult() { return expectedResult; }
    public String getActualResult() { return actualResult; }
    public void setActualResult(String actualResult) { this.actualResult = actualResult; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}


package com.sta.testng;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomReportListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("==========================================================================================");
        System.out.println("                     TESTNG AUTOMATED TEST EXECUTION REPORT START                         ");
        System.out.println("Suite Name: " + context.getSuite().getName());
        System.out.println("Test Run  : " + context.getName());
        System.out.println("==========================================================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.printf("  [RUNNING] %-35s (Groups: %s)\n", result.getMethod().getMethodName(), String.join(", ", result.getMethod().getGroups()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.printf("  [PASS]    %-35s (Time: %d ms)\n", result.getMethod().getMethodName(), duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.printf("  [FAIL]    %-35s (Cause: %s)\n", result.getMethod().getMethodName(), result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.printf("  [SKIPPED] %-35s\n", result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("                         TESTNG AUTOMATION SUMMARY METRICS                                ");
        System.out.println("Total Tests Planned  : " + context.getAllTestMethods().length);
        System.out.println("Tests Passed         : " + context.getPassedTests().size());
        System.out.println("Tests Failed         : " + context.getFailedTests().size());
        System.out.println("Tests Skipped        : " + context.getSkippedTests().size());
        System.out.println("==========================================================================================");
    }
}

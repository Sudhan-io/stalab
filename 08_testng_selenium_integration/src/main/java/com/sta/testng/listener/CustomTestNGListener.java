package com.sta.testng.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomTestNGListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("===============================================================================");
        System.out.println(" [TESTNG LISTENER] STARTING TEST SUITE: " + context.getName());
        System.out.println("===============================================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.printf("  --> [STARTED] %s#%s() | Priority: %d | Groups: %s%n",
            result.getTestClass().getRealClass().getSimpleName(),
            result.getMethod().getMethodName(),
            result.getMethod().getPriority(),
            java.util.Arrays.toString(result.getMethod().getGroups()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.printf("  <-- [PASSED ] %s#%s() in %d ms%n",
            result.getTestClass().getRealClass().getSimpleName(),
            result.getMethod().getMethodName(),
            duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.err.printf("  <-- [FAILED ] %s#%s() - Reason: %s%n",
            result.getTestClass().getRealClass().getSimpleName(),
            result.getMethod().getMethodName(),
            result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.printf("  <-- [SKIPPED] %s#%s()%n",
            result.getTestClass().getRealClass().getSimpleName(),
            result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("===============================================================================");
        System.out.printf(" [TESTNG LISTENER] SUITE FINISHED: Passed: %d | Failed: %d | Skipped: %d%n",
            context.getPassedTests().size(),
            context.getFailedTests().size(),
            context.getSkippedTests().size());
        System.out.println("===============================================================================");
    }
}


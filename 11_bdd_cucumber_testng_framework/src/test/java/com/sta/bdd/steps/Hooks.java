package com.sta.bdd.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("===============================================================================");
        System.out.println(" [CUCUMBER SCENARIO START] " + scenario.getName());
        System.out.println("===============================================================================");
        // Ensure driver is created
        DriverManager.getDriver();
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
            } catch (Exception ignored) {}
        }
        System.out.println(" [CUCUMBER SCENARIO END] Status: " + scenario.getStatus());
        DriverManager.quitDriver();
    }
}


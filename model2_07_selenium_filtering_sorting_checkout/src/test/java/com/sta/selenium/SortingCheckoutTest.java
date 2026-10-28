package com.sta.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortingCheckoutTest {

    @Test
    public void testFilteringSortingAndCheckoutEndToEnd() {
        WebDriver driver = SortingCheckoutAutomation.createDriver();
        boolean passed = SortingCheckoutAutomation.runAutomation(driver);
        assertTrue(passed, "Selenium end-to-end sorting and checkout automation should pass");
    }
}

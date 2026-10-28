package com.sta.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
    }

    public String getConfirmationMessage() {
        return getText(completeHeader);
    }
}

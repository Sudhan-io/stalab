package com.sta.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {

    // Step 1 locators
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");

    // Step 2 locators
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void enterCustomerInfo(String first, String last, String zip) {
        wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
        type(firstNameField, first);
        type(lastNameField, last);
        type(postalCodeField, zip);
        click(continueButton);
    }

    public String getSummaryTotal() {
        wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
        return getText(totalLabel);
    }

    public void clickFinish() {
        click(finishButton);
    }
}


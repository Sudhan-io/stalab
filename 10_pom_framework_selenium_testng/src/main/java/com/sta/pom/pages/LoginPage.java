package com.sta.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Locators encapsulated as private constants
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public void clickLogin() {
        click(loginButton);
    }

    /**
     * Fluent action: Login and return the resulting ProductsPage.
     */
    public ProductsPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new ProductsPage(driver);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isLoginButtonDisplayed() {
        return isElementVisible(loginButton);
    }
}


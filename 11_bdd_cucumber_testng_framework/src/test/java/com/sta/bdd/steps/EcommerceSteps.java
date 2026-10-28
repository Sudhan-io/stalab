package com.sta.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class EcommerceSteps {

    private WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    }

    private static final String APP_URL = "https://www.saucedemo.com";

    @Given("the user navigates to the e-commerce storefront")
    public void userNavigatesToStorefront() {
        getDriver().get(APP_URL);
    }

    @When("the user enters username {string} and password {string}")
    public void userEntersCredentials(String username, String password) {
        WebElement userField = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement passField = getDriver().findElement(By.id("password"));

        userField.clear();
        if (!username.isEmpty()) {
            userField.sendKeys(username);
        }

        passField.clear();
        if (!password.isEmpty()) {
            passField.sendKeys(password);
        }
    }

    @And("clicks on the login button")
    public void clicksLoginButton() {
        WebElement loginBtn = getDriver().findElement(By.id("login-button"));
        loginBtn.click();
    }

    @Then("the user should be redirected to the product catalog page")
    public void verifyCatalogRedirect() {
        getWait().until(ExpectedConditions.urlContains("inventory"));
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"), "URL should contain 'inventory'");
    }

    @And("the catalog should display available products")
    public void verifyCatalogItems() {
        List<WebElement> items = getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item")));
        Assert.assertFalse(items.isEmpty(), "Catalog should contain products");
    }

    @Then("an error message containing {string} should be displayed")
    public void verifyErrorMessage(String expectedMessage) {
        WebElement errorBanner = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        String actualText = errorBanner.getText();
        Assert.assertTrue(actualText.toLowerCase().contains(expectedMessage.toLowerCase()),
            "Expected error to contain '" + expectedMessage + "' but got: '" + actualText + "'");
    }

    @Given("the user is logged in with username {string} and password {string}")
    public void userIsLoggedIn(String username, String password) {
        userNavigatesToStorefront();
        userEntersCredentials(username, password);
        clicksLoginButton();
        verifyCatalogRedirect();
    }

    @When("the user selects the product {string}")
    public void selectProduct(String productName) {
        WebElement productLink = getWait().until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[text()='" + productName + "']")));
        productLink.click();
    }

    @And("clicks the add to cart button")
    public void clickAddToCart() {
        WebElement addBtn = getWait().until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@id,'add-to-cart') or text()='Add to cart']")));
        addBtn.click();
    }

    @Then("the shopping cart badge count should be {string}")
    public void verifyCartBadge(String expectedCount) {
        WebElement badge = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertEquals(badge.getText().trim(), expectedCount);
    }

    @When("the user opens the shopping cart")
    public void openShoppingCart() {
        WebElement cartIcon = getDriver().findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        getWait().until(ExpectedConditions.urlContains("cart"));
    }

    @Then("the cart should contain the item {string}")
    public void verifyCartItem(String expectedItem) {
        WebElement cartItem = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
        Assert.assertEquals(cartItem.getText().trim(), expectedItem);
    }

    @When("the user removes {string} from the cart")
    public void removeItemFromCart(String itemName) {
        WebElement removeBtn = getWait().until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='cart_item']//button")));
        removeBtn.click();
    }

    @Then("the item should no longer be present in the cart")
    public void verifyItemRemoved() {
        List<WebElement> items = getDriver().findElements(By.className("cart_item"));
        Assert.assertTrue(items.isEmpty(), "Cart should be empty");
    }
}


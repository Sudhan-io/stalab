package com.sta.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    private final By cartItems = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.urlContains("/cart.html"));
    }

    public List<String> getItemNames() {
        List<WebElement> elements = driver.findElements(cartItems);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText());
        }
        return names;
    }

    public void clickCheckout() {
        click(checkoutButton);
    }
}


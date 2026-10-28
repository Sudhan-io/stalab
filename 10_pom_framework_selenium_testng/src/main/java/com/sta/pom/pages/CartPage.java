package com.sta.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getCartItemTitles() {
        return driver.findElements(cartItemNames).stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());
    }

    public CartPage removeItem(String productName) {
        By removeBtn = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//button");
        click(removeBtn);
        return this;
    }

    public boolean isItemInCart(String productName) {
        return getCartItemTitles().contains(productName);
    }
}


package com.sta.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {

    // Encapsulated Locators
    private final By pageTitle = By.className("title");
    private final By productCards = By.className("inventory_item");
    private final By productNames = By.className("inventory_item_name");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public List<String> getAllProductTitles() {
        return driver.findElements(productNames).stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());
    }

    public ProductDetailPage selectProduct(String productName) {
        By itemLocator = By.xpath("//div[contains(@class,'inventory_item_name') and text()='" + productName + "']");
        click(itemLocator);
        return new ProductDetailPage(driver);
    }

    public ProductsPage addProductToCart(String productName) {
        String buttonId = "add-to-cart-" + productName.toLowerCase().replaceAll("[^a-z0-9]", "-");
        // Fallback xpath if dynamic ID varies
        By addBtn = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button");
        click(addBtn);
        return this;
    }

    public int getCartBadgeCount() {
        if (!isElementVisible(cartBadge)) return 0;
        String text = getText(cartBadge);
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }
}


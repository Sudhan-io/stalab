package com.sta.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage extends BasePage {

    private final By sortContainer = By.className("product_sort_container");
    private final By itemNames = By.className("inventory_item_name");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.urlContains("/inventory.html"));
    }

    public List<String> searchOrFilterProducts(String keyword) {
        List<WebElement> elements = driver.findElements(itemNames);
        List<String> matched = new ArrayList<>();
        for (WebElement el : elements) {
            String name = el.getText();
            if (name.toLowerCase().contains(keyword.toLowerCase())) {
                matched.add(name);
            }
        }
        return matched;
    }

    public void sortByOption(String value) {
        WebElement el = waitAndFind(sortContainer);
        Select select = new Select(el);
        select.selectByValue(value);
    }

    public void addItemToCart(String itemSlug) {
        By buttonLocator = By.id("add-to-cart-" + itemSlug);
        click(buttonLocator);
    }

    public int getCartBadgeCount() {
        try {
            String count = getText(cartBadge);
            return Integer.parseInt(count);
        } catch (Exception e) {
            return 0;
        }
    }

    public void navigateToCart() {
        click(cartLink);
    }
}

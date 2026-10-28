package com.sta.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {

    private final By detailTitle = By.className("inventory_details_name");
    private final By detailPrice = By.className("inventory_details_price");
    private final By addToCartButton = By.xpath("//button[contains(@id,'add-to-cart')]");
    private final By backButton = By.id("back-to-products");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getProductTitle() {
        return getText(detailTitle);
    }

    public String getProductPrice() {
        return getText(detailPrice);
    }

    public ProductDetailPage addToCart() {
        click(addToCartButton);
        return this;
    }

    public ProductsPage backToProducts() {
        click(backButton);
        return new ProductsPage(driver);
    }
}


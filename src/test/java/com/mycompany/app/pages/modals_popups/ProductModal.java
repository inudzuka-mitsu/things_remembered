package com.mycompany.app.pages.modals_popups;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.mycompany.app.pages.BasePage;

public class ProductModal extends BasePage{

    public ProductModal(Page page) {
        super(page);
    }

    String productItem = "div.ea-sug-product-name";
    String productSuggestions = "div[class='ea-sug-section'] ul li";


    public void validateAllProductsContainProductName(String product) {
        Locator items = page.locator(productItem);
    
        for (int i = 0; i < items.count(); i++) {
           String actualText = items.nth(i).innerText().toLowerCase();
           assertTrue(actualText.contains(product.toLowerCase()), 
            "Expected product item at index " + i + " to contain '" + product + "', but got: " + actualText);
        }
    }

    public void validateAllProductSuggestionsContainProductName(String product) {
        Locator items = page.locator(productSuggestions);
    
        for (int i = 0; i < items.count(); i++) {
           String actualText = items.nth(i).innerText().toLowerCase();
           assertTrue(actualText.contains(product.toLowerCase()), 
            "Expected product item at index " + i + " to contain '" + product + "', but got: " + actualText);
        }
    }
}

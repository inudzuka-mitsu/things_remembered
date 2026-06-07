package com.mycompany.app.pages.modals_popups;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.mycompany.app.pages.BasePage;

public class ProductModal extends BasePage{

    public ProductModal(Page page) {
        super(page);
    }

    String categoryName = "div.ea-sug-category-name";
    String productItem = "div.ea-sug-product-name";
    String productSuggestions = "div[class='ea-sug-section'] ul li";

    public void validateCategoryName(String expected) {
        String actualText = page.locator(categoryName).innerText();
        assertEquals("Shop " + expected + " Category", actualText);
    }

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

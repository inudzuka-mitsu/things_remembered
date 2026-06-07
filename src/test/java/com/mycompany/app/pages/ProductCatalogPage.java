package com.mycompany.app.pages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProductCatalogPage extends BasePage {

    private final boolean isMobile;

    public ProductCatalogPage(Page page, boolean isMobile) {
       super(page);
       this.isMobile = isMobile;
    }

    String itemDescription = "";
    String itemImage = "";
    String currentSelectionItem = "";
    String productLink = "div.search-item .thumbProduct a";

    // MOBILE APP LOCATORS 

    String mobileItemDescription = "";
    String mobileItemImage = "";


    public void validateCurrentSelection(String searchQuery) {
        assertEquals(searchQuery.toLowerCase(), page.locator(currentSelectionItem).innerText().toLowerCase());
    }

    public void validateItemSearchResults(String searchQuery) {
    
        String activeDescLocator = isMobile ? mobileItemDescription : itemDescription;
        String activeImgLocator = isMobile ? mobileItemImage : itemImage;

        Locator items = page.locator(activeDescLocator);
        Locator itemImages = page.locator(activeImgLocator);

        assertEquals(items.count(), itemImages.count(), "Mismatch between count of descriptions and images.");

        String rawQuery = searchQuery.toLowerCase();
        String validatedQuery = rawQuery.endsWith("s") ? 
                            rawQuery.substring(0, rawQuery.length() - 1) : 
                            rawQuery;

        for (int i = 0; i < items.count(); i++) {
            String actualDescription = items.nth(i).innerText().toLowerCase();
            
            String imageAltText = itemImages.nth(i).getAttribute("alt");
            String imageTitleText = itemImages.nth(i).getAttribute("title");
            
            imageAltText = (imageAltText != null) ? imageAltText.toLowerCase() : "";
            imageTitleText = (imageTitleText != null) ? imageTitleText.toLowerCase() : "";
            
            String imageMetadata = imageAltText + " " + imageTitleText;

            assertTrue(actualDescription.contains(validatedQuery), 
                String.format("Expected item description at index %d to contain '%s', but found: '%s'", 
                i, validatedQuery, actualDescription));

            assertTrue(imageMetadata.contains(validatedQuery), 
                String.format("Expected image Alt/Title at index %d to contain '%s'. \nFound Alt: '%s' \nFound Title: '%s'", 
                i, validatedQuery, imageAltText, imageTitleText));
       }
    }

    public void clickFirstProduct() {
        String activeProductLink = isMobile ? mobileItemImage : productLink;
        
        Locator firstProduct = page.locator(activeProductLink).first();
        
        if (!isMobile) {
            firstProduct.hover();
            page.waitForTimeout(500);
        }
       
        firstProduct.click();
    }
}
package com.mycompany.app.pages;

import com.microsoft.playwright.Page;

public class ForHerPage extends BasePage {

    private final boolean isMobile;

    public ForHerPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // DESKTOP APP LOCATORS

    private final String productLinks = ".thumbProduct a.prod_url.carousel_new";

    // MOBILE APP LOCATORS


    public void clickCategory(String categoryName) {
        String dynamicLocator = String.format("nav a.main-banner_link:text-is(\"%s\")", categoryName);
        page.locator(dynamicLocator).click();
    }

    public void clickFirstProduct() {
        page.locator(productLinks).first().click();
    }
}
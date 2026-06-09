package com.mycompany.app.pages;

import com.microsoft.playwright.Page;

public class ForHerPage extends BasePage {

    private final boolean isMobile;

    public ForHerPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // DESKTOP & MOBILE APP LOCATORS

    private final String productLinks = ".thumbProduct a.prod_url.carousel_new, div.prod-thumb-img";


    public void clickCategory(String categoryName) {
        String dynamicLocator = String.format("nav a.main-banner_link:text-is(\"%s\")", categoryName);
        page.locator(dynamicLocator).click();
    }

    public void clickFirstProduct() {
        page.locator(productLinks).first().click();
    }
}
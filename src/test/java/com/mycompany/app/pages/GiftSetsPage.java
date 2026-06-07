package com.mycompany.app.pages;

import com.microsoft.playwright.Page;

public class GiftSetsPage extends BasePage {

    public GiftSetsPage(Page page) {
        super(page);
    }

    private final String productLink = ".product-list .cell__photo a.prod_url";

    public void clickFirstGiftSet() {
        page.locator(productLink).first().click();
    }
}
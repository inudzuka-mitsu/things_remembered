package com.mycompany.app.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class EditorPage extends BasePage {

    public EditorPage(Page page) {
        super(page);
    }

    private final String iframeSelector = "#app_iframe";
    private final String addToCartBtn = "div[data-sid='advEditorOrderButton'], .OrderButton, div[role='button']:has-text('Add to Cart')";

    public void clickAddToCart() {
        page.locator(iframeSelector).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        FrameLocator frame = page.frameLocator(iframeSelector);
        frame.locator(addToCartBtn).first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        frame.locator(addToCartBtn).first().click();
    }
}
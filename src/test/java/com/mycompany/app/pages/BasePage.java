package com.mycompany.app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

public class BasePage {
    
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void click(String selector) {
        page.click(selector);
    }

    public void type(String selector, String text) {
        page.fill(selector, text);
    }
    
    public String getTitle() {
        return page.title();
    }

    public void closePopUp() {
        try {
            page.frameLocator("#pmall_dialog_iframe-2")
                .locator("a.dismissal-link")
                .click(new Locator.ClickOptions().setTimeout(10000));
            
        } catch (TimeoutError e) {
            System.out.println("No popup appeared within 10 seconds. Moving on...");
        }
    }
}
package com.mycompany.app.pages.modals_popups;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mycompany.app.pages.BasePage;

public class Footer extends BasePage {

    private final boolean isMobile;

    public Footer(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // DESKTOP APP LOCATORS

    private final String celebrationsPassportLink = "a:has-text('Celebrations Passport')";

    // MOBILE APP LOCATORS

    private final String mobileServicesLink = "#id_footer_nav li:has-text('Services')";
    private final String mobileCelebrationsPassportLink = "#id_footer_content2 a:text-is('Celebrations Passport')";


    public void clickCelebrationsPassport() {
        if (isMobile) {
            Locator servicesMenu = page.locator(mobileServicesLink);
            servicesMenu.scrollIntoViewIfNeeded();
            servicesMenu.click();

            Locator link = page.locator(mobileCelebrationsPassportLink);
            link.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            link.scrollIntoViewIfNeeded();
            link.click();
            
        } else {
            Locator link = page.locator(celebrationsPassportLink).first();
            link.scrollIntoViewIfNeeded();
            link.click();
        }
    }
}
package com.mycompany.app.pages;

import com.microsoft.playwright.Page;

public class AccountPage extends BasePage {

    private final boolean isMobile;

    public AccountPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // DESKTOP APP LOCATORS

    private final String manageShippingAddressLink = "a:has-text('Manage my shipping address')";
    private final String orderHistoryLink = "a:has-text('Order History')";

    // MOBILE APP LOCATORS

    private final String mobileManageShippingAddressesBtn = ".flex__menu-blocks li:has-text('Manage My Shipping Addresses')";
    private final String mobileOrderHistoryLink = ".flex__menu-blocks li:has-text('Order History')";

    public void clickManageShippingAddress() {
        String locator = isMobile ? mobileManageShippingAddressesBtn : manageShippingAddressLink;
        page.locator(locator).first().click();
    }

    public void clickOrderHistory() {
        String locator = isMobile ? mobileOrderHistoryLink : orderHistoryLink;
        page.locator(locator).first().click();
    }
}
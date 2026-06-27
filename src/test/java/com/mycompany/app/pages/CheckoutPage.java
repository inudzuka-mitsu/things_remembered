package com.mycompany.app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CheckoutPage extends BasePage {

    private final boolean isMobile;

    public CheckoutPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // --- COMBINED DESKTOP & MOBILE LOCATORS ---
    
    private final String mobileCouponToggle = "a#couponLink";
    private final String couponInput = "#txtCouponCode:visible, #CouponCode:visible";
    private final String applyCouponBtn = "#ctl00_belowHeader_cmdReviseDiscout:visible, #submitbutton[value='coupon']:visible";
    private final String creditCardRadio = "#ctl00_belowHeader_payCC, #checkout_payment_gateway_ccard";
    private final String cardTypeDropdown = "#ctl00_belowHeader_cardType, #CardType";
    private final String nameOnCardInput = "#ctl00_belowHeader_nameOnCard, #CardNameOn";
    private final String cardNumberInput = "#ctl00_belowHeader_cardNumber, #CardNumber";
    private final String cvvInput = "#ctl00_belowHeader_validationNumber, #ValidationNumber";
    private final String expMonthDropdown = "#ctl00_belowHeader_expMonth, #CardExpMonth";
    private final String expYearDropdown = "#ctl00_belowHeader_expYear, #CardExpYear";
    
    private final String placeOrderBtn = "#cmdPlaceOrder, input[name='submitButton'][value='Place Your Order']"; 
    
    private final String changeShippingAddressLink = "#ctl00_belowHeader_aChangeShipAddress, h4:has-text('Shipping Information') a:has-text('Change')";
    
    private final String shippingAddressText = "#ctl00_belowHeader_txtShippingAddress, .block__address-info p";
    
    private final String payPalRadio = "#ctl00_belowHeader_payPalRadioButton, #checkout_payment_gateway_ebay";
    private final String payPalPlaceOrderBtn = "#cmdPayPal2Order, #paypalsubmitbutton"; 

    // --- ACTIONS ---

    public void applyCoupon(String code) {
        if (isMobile) {
            System.out.println(">>> Opening mobile coupon accordion...");
            Locator toggle = page.locator(mobileCouponToggle).first();
            toggle.scrollIntoViewIfNeeded();
            toggle.click();
            
            page.waitForTimeout(500); 
        }

        Locator input = page.locator(couponInput).first();
        input.scrollIntoViewIfNeeded();
        input.fill(code);
        
        Locator btn = page.locator(applyCouponBtn).first();
        btn.click(new Locator.ClickOptions().setForce(true));
    }

    public void enterPaymentInformation(String cardType, String name, String number, String cvv, String month, String year) {
        page.locator(creditCardRadio).first().check(new Locator.CheckOptions().setForce(true));
        
        page.locator(cardTypeDropdown).first().selectOption(cardType);
        page.locator(nameOnCardInput).first().fill(name);
        page.locator(cardNumberInput).first().fill(number);
        page.locator(cvvInput).first().fill(cvv);
        page.locator(expMonthDropdown).first().selectOption(month);
        page.locator(expYearDropdown).first().selectOption(year);
    }

    public void placeOrderWithPayPal() {
        System.out.println("Selecting PayPal payment method...");
        page.locator(payPalRadio).first().click(new Locator.ClickOptions().setForce(true));
        
        Locator ppButton = page.locator(payPalPlaceOrderBtn).first();
        ppButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        System.out.println("Clicking 'Place Order' (PayPal version)...");
        ppButton.click(new Locator.ClickOptions().setForce(true));
    }

    public void placeOrder() {
        System.out.println(">>> Preparing to click Place Order...");
        
        Locator orderButton = page.locator(placeOrderBtn).first();
        
        orderButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        orderButton.scrollIntoViewIfNeeded();
        
        orderButton.click(new Locator.ClickOptions().setForce(true));
    }

    public void clickChangeShippingAddress() {
        Locator changeLink = page.locator(changeShippingAddressLink).first();
        changeLink.scrollIntoViewIfNeeded();
        changeLink.click(new Locator.ClickOptions().setForce(true));
    }

    public void validateShippingAddress(String expectedAddress) {
        System.out.println("Validating Shipping Address...");
        
        Locator addressElement = page.locator(shippingAddressText).first();
        addressElement.scrollIntoViewIfNeeded();
        
        String actualAddress = addressElement.innerText();

        String normalizedActual = actualAddress.replaceAll("\\s+", " ").trim();
        String normalizedExpected = expectedAddress.replaceAll("\\s+", " ").trim();

        boolean matchFound = normalizedActual.contains(normalizedExpected) || 
                             normalizedExpected.contains(normalizedActual);

        if (!matchFound) {
            System.out.println("FAILURE: Address Mismatch");
            System.out.println("Expected (Normalized): " + normalizedExpected);
            System.out.println("Actual (Normalized):   " + normalizedActual);
        }

        if (!matchFound) {
            throw new AssertionError("Shipping address did not match!\nExpected part of: " + expectedAddress + "\nFound: " + actualAddress);
        }
        
        System.out.println("Address validation passed.");
    }
}
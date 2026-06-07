package com.mycompany.app.pages;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CelebrationsPassportPage extends BasePage {

    public CelebrationsPassportPage(Page page) {
        super(page);
    }

    private final String signUpButton = "button#btn_add-free-ship";

    public void clickSignUp() {
        page.locator(signUpButton).click();
    }

    public void verifyPassportPageLoaded() {
        assertThat(page.locator(signUpButton)).isVisible();
        System.out.println("Passport Landing Page loaded successfully.");
    }

    public void validatePriceOnButton(String expectedPrice) {
        assertThat(page.locator(signUpButton)).containsText(expectedPrice);
    }
}

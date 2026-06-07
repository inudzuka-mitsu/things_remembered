package com.mycompany.app.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.options.LoadState;
import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.CelebrationsPassportPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.Footer;

// This test is configured for desktop app, iPhone 13 Pro Max, Samsung Galaxy A52

public class PassportTests extends TestBase {

    private CartPage cartPage;
    private Footer footer;
    private StagingLoginPage stagingLoginPage;
    private CelebrationsPassportPage passportPage;

    @Test
    @Tag("smoke")
    void celebrationsPassport() throws InterruptedException {

        footer = new Footer(page, isMobile());
        cartPage = new CartPage(page, isMobile());
        stagingLoginPage = new StagingLoginPage(page);
        passportPage = new CelebrationsPassportPage(page);

        String ITEM_PRICE = "$19.99";

        String env = System.getProperty("env", "stg");
        
        if ("prod".equalsIgnoreCase(env)) {
            page.navigate(getProperty("baseUrl"));
        } else {
            page.navigate(getProperty("stagingBaseUrl"));
        }
        stagingLoginPage.closePopUp();

        footer.clickCelebrationsPassport();
        page.waitForLoadState(LoadState.LOAD);
        passportPage.validatePriceOnButton(ITEM_PRICE);

        page.waitForLoadState(LoadState.LOAD);

        page.waitForResponse(
           response -> response.url().contains("cart") && response.status() == 200, 
           () -> {
        passportPage.clickSignUp();
         }
        );

        cartPage.validateProductInCart("Passport Membership");
    }
}

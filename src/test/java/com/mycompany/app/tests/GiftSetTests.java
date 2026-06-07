package com.mycompany.app.tests;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.ProductPage;
import com.mycompany.app.pages.login.SignInPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.Header;
import com.mycompany.app.pages.modals_popups.PersonalizeItemModal;

// This test is configured for desktop app, iPhone 13 Pro Max, Samsung Galaxy A52

public class GiftSetTests extends TestBase {

    private StagingLoginPage stagingLoginPage;
    private ProductPage productPage;
    private PersonalizeItemModal personalizeModal;
    private HomePage homePage;
    private CartPage cartPage;
    private SignInPage signInPage;
    private Header header;

    private final String PRODUCT_SLUG = "/Whiskey-Glass-Decanter-Personalized-Gift-Set-Lavish-Last-Name-p55731.prod?sdest=store-one&sdestid=75";
    private final String PRODUCT_NAME = "Lavish Last Name Personalized Whiskey Glass";
    private final String PERSONALIZATION_MSG = "Happy Birthday!";

    @BeforeEach
    @Override
    public void setup() throws IOException {
        super.setup();
        stagingLoginPage = new StagingLoginPage(page);
        productPage = new ProductPage(page, isMobile());
        personalizeModal = new PersonalizeItemModal(page);
        homePage = new HomePage(page, isMobile());
        cartPage = new CartPage(page, isMobile());
        signInPage = new SignInPage(page, isMobile());
        header = new Header(page, isMobile());
    }

    @Test
    @DisabledIfSystemProperty(named = "env", matches = "prod")
    @DisplayName("Verify user can personalize a gift set, save it for later and move it back to cart")
    void giftSetPersonalizationFlow() {
        String testEmail = getProperty("test_email_2");
        String testPassword = getProperty("test_password_2");

        page.navigate(getProperty("stagingBaseUrl"));
        stagingLoginPage.closePopUp();

        page.navigate(getProperty("baseUrl") + PRODUCT_SLUG);
        productPage.clickPersonalizeBtn();
        try {
            personalizeModal.fillGiftSetPersonalizationAndAddToCart("T", "name1"); 
            personalizeModal.fillGiftSetPersonalizationAndAddToCart("M", "name2"); 
            personalizeModal.fillGiftSetPersonalizationAndAddToCart("K", "name3"); 
            personalizeModal.fillGiftSetPersonalizationAndAddToCart("K", "name4"); 
            personalizeModal.fillGiftSetPersonalizationAndAddToCart("L", "name5"); 
            
            personalizeModal.selectColor("Blue");
            personalizeModal.clickContinue();
            
            personalizeModal.enterMessage(PERSONALIZATION_MSG);
            personalizeModal.clickContinue();
            
            personalizeModal.checkPersonalizationCorrect();
            personalizeModal.clickAddToCart();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

        homePage.clickViewCart();

        cartPage.clickSaveForLaterSpecProd(PRODUCT_NAME);

        signInPage.signIn(testEmail, testPassword);

        page.waitForTimeout(7000);

        if (isMobile()) {
            header.clickCartIcon();
        }

        cartPage.clickSaveForLaterSpecProd(PRODUCT_NAME);

        if (!isMobile()) { cartPage.validateEmptyCartAndSavedMessage();}
        cartPage.validateProductInSavedForLaterSpecProd(PRODUCT_NAME);

        cartPage.clickMoveToCartSpecProd(PRODUCT_NAME);
        page.waitForTimeout(7000);
        cartPage.validateProductAddedToCart(PRODUCT_NAME);
    }
}
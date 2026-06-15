package com.mycompany.app.tests;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.ProductPage;
import com.mycompany.app.pages.login.SignInPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.Header;
import com.mycompany.app.pages.modals_popups.PersonalizeItemModal;

// This test is configured for TR desktop (stg and prod)

public class GiftSetTests extends TestBase {

    private StagingLoginPage stagingLoginPage;
    private ProductPage productPage;
    private PersonalizeItemModal personalizeModal;
    private HomePage homePage;
    private CartPage cartPage;
    private SignInPage signInPage;
    private Header header;

    private final String PRODUCT_SLUG = "/Personalized-Whiskey-Glass-Decanter-Gift-Set-Classic-Celebrations-p52448.prod?sdest=store-one&sdestid=105";
    private final String PRODUCT_NAME = "Classic Celebrations Personalized Whiskey Glass & Decanter Gift Set";
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
    @DisplayName("Verify user can personalize a gift set, save it for later and move it back to cart")
    void giftSetPersonalizationFlow() {
        String testEmail = getProperty("test_email_2");
        String testPassword = getProperty("test_password_2");

        page.navigate(getProperty("baseUrl"));
        stagingLoginPage.closePopUp();

        page.navigate(getProperty("baseUrl") + PRODUCT_SLUG);
        productPage.clickPersonalizeBtn();
    
        personalizeModal.fillInitialAndContinue("T", "Block"); 
        personalizeModal.fillInitialAndContinue("M", "Block"); 
        personalizeModal.fillInitialAndContinue("K", "Script"); 
        personalizeModal.fillInitialAndContinue("K", "Block"); 
        personalizeModal.fillInitialAndContinue("L", "Script"); 

        personalizeModal.selectCustomDropdown("Choose Pattern", "Stripe");
        personalizeModal.selectCustomDropdown("Choose Pattern Color", "Linen");
        personalizeModal.selectCustomDropdown("Choose Background Color", "Ivory");
        personalizeModal.selectCustomDropdown("Choose Text Color", "Black");
        personalizeModal.selectCustomDropdown("Choose Text Box Color", "Brown");

        personalizeModal.fillMultiLinePersonalization(PERSONALIZATION_MSG);

        personalizeModal.clickContinue();

        personalizeModal.enterPersonalizationMessage(PERSONALIZATION_MSG);
        personalizeModal.clickContinue();
                    
        personalizeModal.checkPersonalizationCorrect();
        personalizeModal.clickAddToCart();
    
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
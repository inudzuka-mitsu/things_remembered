package com.mycompany.app.tests;

import org.junit.jupiter.api.Test;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.CheckoutPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.OrderConfirmationPage;
import com.mycompany.app.pages.ProductPage;
import com.mycompany.app.pages.login.SignInPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.AddressModal;
import com.mycompany.app.pages.modals_popups.CustomizeGiftModal;
import com.mycompany.app.pages.modals_popups.PersonalizeItemModal;


// These tests are configured for desktop app, iPhone 13 Pro Max, Samsung Galaxy A52

public class ProductPersonalizationTests extends TestBase {

    private StagingLoginPage stagingLoginPage;
    private SignInPage signInPage;
    private ProductPage productPage;
    private PersonalizeItemModal personalizeModal;
    private CustomizeGiftModal giftBoxModal;
    private HomePage homePage;
    private CartPage cartPage;
    private AddressModal shippingPage;
    private CheckoutPage checkoutPage;
    private OrderConfirmationPage confirmationPage;

    private final String THREAD_COLOR = "Lilac";
    private final String FONT_STYLE = "Frunch";
    private final String PERSONALIZATION_TEXT = "QATest";
    private final int QUANTITY = 3;

    private final String ORIGINAL_THREAD_COLOR = "Burgundy";
    private final String PERSONALIZATION_TEXT_2 = "QATest_2";
    private final String UPDATED_THREAD_COLOR = "White";

   @Test
    void personalizeItem() {
        stagingLoginPage = new StagingLoginPage(page);
        signInPage = new SignInPage(page, isMobile());
        productPage = new ProductPage(page, isMobile());
        personalizeModal = new PersonalizeItemModal(page);
        giftBoxModal = new CustomizeGiftModal(page, isMobile());
        homePage = new HomePage(page, isMobile());
        cartPage = new CartPage(page, isMobile());
        shippingPage = new AddressModal(page, isMobile());
        checkoutPage = new CheckoutPage(page);
        confirmationPage = new OrderConfirmationPage(page);

        String testEmail = getProperty("test_email_2");
        String testPassword = getProperty("test_password_2");
        String couponCode = getProperty("sale_item_coupon_code");
        String cardType = getProperty("card_type");
        String cardName = getProperty("name");
        String cardNumber = getProperty("card_number");
        String securityCode = getProperty("card_security_code");
        String cardExpMonth = getProperty("card_exp_month");
        String cardExpYear = getProperty("card_exp_year");

        String PRODUCT_URL = getProperty("baseUrl") + "/Winter-Wonderland-Personalized-Christmas-Stockings-p30508.prod?sdest=Search&sdestid=181585763";

        page.navigate(PRODUCT_URL);
        stagingLoginPage.closePopUp();

        page.navigate(PRODUCT_URL);
        productPage.clickPersonalizeBtn();

        personalizeModal.fillPersonalizationAndAddToCart(THREAD_COLOR, FONT_STYLE, PERSONALIZATION_TEXT);
        giftBoxModal.clickContinue();

        homePage.validateAddedToCartVisible();
        homePage.validatePersonalization(THREAD_COLOR, FONT_STYLE, PERSONALIZATION_TEXT);
        homePage.clickViewCart();
        cartPage.updateQuantityAndVerifyTotal(QUANTITY);
        cartPage.clickProceedToCheckout();

        signInPage.signIn(testEmail, testPassword);
        signInPage.hoverCartAndCheckout();

        shippingPage.selectFirstAddressAndShip();
        shippingPage.clickSaveAndContinue();

        checkoutPage.applyCoupon(couponCode);
        checkoutPage.enterPaymentInformation(cardType, cardName, cardNumber, securityCode, cardExpMonth, cardExpYear);
        checkoutPage.placeOrder();

        confirmationPage.verifyOrderSuccessMessage();
    }

    // @Test
    // void editItem() {
    //     stagingLoginPage = new StagingLoginPage(page);
    //     signInPage = new SignInPage(page, isMobile());
    //     productPage = new ProductPage(page, isMobile());
    //     personalizeModal = new PersonalizeItemModal(page);
    //     giftBoxModal = new CustomizeGiftModal(page, isMobile());
    //     homePage = new HomePage(page, isMobile());
    //     cartPage = new CartPage(page, isMobile());
    //     shippingPage = new AddressModal(page, isMobile());
    //     checkoutPage = new CheckoutPage(page);
    //     confirmationPage = new OrderConfirmationPage(page);

    //     String PRODUCT_URL = getProperty("baseUrl") + "/Crossed-Clubs-Embroidered-Golf-Towel-p28855.prod?sdest=search-op&sdestid=117478940";

    //     String env = System.getProperty("env", "stg");
        
    //     if ("prod".equalsIgnoreCase(env)) {
    //         page.navigate(getProperty("baseUrl"));
    //     } else {
    //         page.navigate(getProperty("stagingBaseUrl"));
    //     }
    //     stagingLoginPage.closePopUp();

    //     page.navigate(PRODUCT_URL);
    //     productPage.clickPersonalizeBtn();

    //     personalizeModal.enterName(PERSONALIZATION_TEXT_2);
    //     personalizeModal.selectColor(ORIGINAL_THREAD_COLOR);
    //     personalizeModal.verifyPreviewImagePersonalization(ORIGINAL_THREAD_COLOR, null, PERSONALIZATION_TEXT_2);
    //     personalizeModal.checkPersonalizationCorrect();
    //     personalizeModal.clickAddToCart();
    //      personalizeModal.clickContinueBtn();

    //     homePage.validatePersonalization(ORIGINAL_THREAD_COLOR, null, PERSONALIZATION_TEXT_2);

    //     homePage.clickViewCart();

    //     cartPage.clickEdit();

    //     personalizeModal.selectColor(UPDATED_THREAD_COLOR);
    //     personalizeModal.verifyPreviewImagePersonalization(UPDATED_THREAD_COLOR, null, PERSONALIZATION_TEXT_2);
    //     personalizeModal.checkPersonalizationCorrect();
    //     personalizeModal.clickAddToCart();

    //     homePage.validatePersonalization(UPDATED_THREAD_COLOR, null, PERSONALIZATION_TEXT_2);
    // }
 }
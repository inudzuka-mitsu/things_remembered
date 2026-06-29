package com.mycompany.app.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.CheckoutPage;
import com.mycompany.app.pages.DesignMugPage;
import com.mycompany.app.pages.DesignToolPage;
import com.mycompany.app.pages.EditorPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.OrderConfirmationPage;
import com.mycompany.app.pages.ProductPage;
import com.mycompany.app.pages.login.SignInPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.AddressModal;
import com.mycompany.app.pages.modals_popups.DesignPopup;
import com.mycompany.app.pages.modals_popups.Header;
import com.mycompany.app.pages.modals_popups.PersonalizeItemModal;

// These tests are configured for TR desktop (stg and prod), BUG ON MOBILE, CAN'T PROCEED NOW

public class ProductDesignTests extends TestBase {

    private StagingLoginPage stagingLoginPage;
    private ProductPage productPage;
    private DesignToolPage designToolPage;
    private EditorPage editorPage;
    private DesignPopup popup;
    private HomePage homePage;
    private SignInPage signInPage;
    private AddressModal shippingPage;
    private CheckoutPage checkoutPage;
    private OrderConfirmationPage confirmationPage;
    private DesignMugPage designMugPage;
    private PersonalizeItemModal personalizeModal;
    private CartPage cartPage;
    private Header header;

    @Test
    @DisplayName("Verify user can design a wedding photo book and complete checkout")
    void designItem() {
        String PRODUCT_URL = getProperty("baseUrl") + "/Our-Wedding-Chronicle-Personalized-Photo-Book-4x6-Soft-Glossy-i165541.item?productid=59192&sdest=Search&sdestid=181713431";
        String testEmail = getProperty("test_email_2");
        String testPassword = getProperty("test_password_2");
        String cardType = getProperty("card_type");
        String cardName = getProperty("name");
        String cardNumber = getProperty("card_number");
        String securityCode = getProperty("card_security_code");
        String cardExpMonth = getProperty("card_exp_month");
        String cardExpYear = getProperty("card_exp_year");
        
        stagingLoginPage = new StagingLoginPage(page);
        productPage = new ProductPage(page, isMobile());
        designToolPage = new DesignToolPage(page);
        editorPage = new EditorPage(page);
        popup = new DesignPopup(page, isMobile());
        homePage = new HomePage(page, isMobile());
        signInPage = new SignInPage(page, isMobile());
        shippingPage = new AddressModal(page, isMobile());
        checkoutPage = new CheckoutPage(page, isMobile());
        confirmationPage = new OrderConfirmationPage(page);
        header = new Header(page, isMobile());

        page.navigate(PRODUCT_URL);
        stagingLoginPage.closePopUp();

        productPage.clickPersonalizeBtn();

        designToolPage.clickGoStraightToEditor();
        editorPage.clickAddToCart();

        popup.handleValidationPopup();
        
        if (isMobile()) {
            header.clickCartIcon();
        }

        homePage.clickCheckout();

        signInPage.signIn(testEmail, testPassword);

        if (!isMobile()) {
           signInPage.hoverCartAndCheckout();
        }

        shippingPage.selectFirstAddressAndShip();
        shippingPage.clickSaveAndContinue();

        checkoutPage.enterPaymentInformation(cardType, cardName, cardNumber, securityCode, cardExpMonth, cardExpYear);
        checkoutPage.placeOrder();

        confirmationPage.verifyOrderSuccessMessage();
    }

    @Test
    @DisplayName("Verify user can change shipping address during checkout")
    void changeShippingAddress() {
        String PRODUCT_URL = getProperty("baseUrl") + "/Our-Wedding-Chronicle-Personalized-Photo-Book-4x6-Soft-Glossy-i165541.item?productid=59192&sdest=Search&sdestid=181713431";
        String testEmail = getProperty("test_email_2");
        String testPassword = getProperty("test_password_2");
        
        stagingLoginPage = new StagingLoginPage(page);
        productPage = new ProductPage(page, isMobile());
        designToolPage = new DesignToolPage(page);
        editorPage = new EditorPage(page);
        popup = new DesignPopup(page);
        homePage = new HomePage(page, isMobile());
        signInPage = new SignInPage(page, isMobile());
        shippingPage = new AddressModal(page, isMobile());
        checkoutPage = new CheckoutPage(page, isMobile());
        confirmationPage = new OrderConfirmationPage(page);
        header = new Header(page, isMobile());

        page.navigate(PRODUCT_URL);
        stagingLoginPage.closePopUp();
        productPage.clickPersonalizeBtn();

        designToolPage.clickGoStraightToEditor();
        editorPage.clickAddToCart();

        popup.handleValidationPopup();

        if (isMobile()) {
            header.clickCartIcon();
        }

        homePage.clickCheckout();

        signInPage.signIn(testEmail, testPassword);

        if (!isMobile()) {
          signInPage.hoverCartAndCheckout();
        }

        String firstSelectedAddress = shippingPage.selectFirstAddressAndReturnText();
        System.out.println(firstSelectedAddress);
        shippingPage.clickSaveAndContinue();
        checkoutPage.validateShippingAddress(firstSelectedAddress);

        checkoutPage.clickChangeShippingAddress();

        String secondAddress = shippingPage.selectSecondAddressAndReturnText();
        shippingPage.clickSaveAndContinue();
        checkoutPage.validateShippingAddress(secondAddress);
    }
}

// package com.mycompany.app.tests;

// import com.mycompany.app.base.TestBase;
// import com.mycompany.app.pages.CheckoutPage;
// import com.mycompany.app.pages.HomePage;
// import com.mycompany.app.pages.login.SignInPage;
// import com.mycompany.app.pages.login.StagingLoginPage;
// import com.mycompany.app.pages.modals_popups.AddressModal;

/*
       This test is commented out, because Paypal implements checks on whether a human uses the app or a robot. Such checks should not be automated. 
*/

//public class PaypalTests extends TestBase {

    // private StagingLoginPage stagingLoginPage;
    // private HomePage homePage;
    // private SignInPage signInPage;
    // private AddressModal shippingPage;
    // private CheckoutPage checkoutPage;

    // @Test
    // @DisplayName("Validate user can sucecssfully pay for a product with Paypal")
    // void validatePaypalPayment() {

    //     stagingLoginPage = new StagingLoginPage(page);
    //     homePage = new HomePage(page);
    //     signInPage = new SignInPage(page);
    //     shippingPage = new AddressModal(page);
    //     checkoutPage = new CheckoutPage(page);

    //     String PRODUCT_URL = getProperty("staging_paypal_product");
    //     String testEmail = getProperty("test_email_2");
    //     String testPassword = getProperty("test_password_2");

    //     page.navigate(getProperty("stagingBaseUrl"));
    //     stagingLoginPage.closePopUp();

    //     page.navigate(PRODUCT_URL);

    //     page.locator("[name='ctl00$mainContent$addToCart$addToCartButton']").click();

    //     homePage.clickCheckout();

    //     signInPage.signIn(testEmail, testPassword);

    //     shippingPage.selectFirstAddressAndShip();
    //     shippingPage.clickSaveAndContinue();

    //     checkoutPage.placeOrderWithPayPal();

    //     page.pause();
    // }
//}

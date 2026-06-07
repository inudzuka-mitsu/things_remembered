package com.mycompany.app.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.AddAddressPage;
import com.mycompany.app.pages.CheckoutPage;
import com.mycompany.app.pages.CreateProfilePage;
import com.mycompany.app.pages.login.SignInPage;
import com.mycompany.app.pages.modals_popups.AddressModal;
import com.mycompany.app.pages.modals_popups.Header;

// These tests are configured for desktop app, iPhone 13 Pro Max, Samsung Galaxy A52

public class CreateAccountsTests extends TestBase {

    private SignInPage signInPage;
    private CreateProfilePage createProfilePage; 
    private AddAddressPage addAddressPage;
    private AddressModal addressModal;
    private CheckoutPage checkoutPage;
    private Header header;

    @Test
    //@DisabledIfSystemProperty(named = "env", matches = "prod")
    @DisplayName("Verify user can checkout as a guest")
    void createGuestAccount() {

        signInPage = new SignInPage(page, isMobile());
        createProfilePage = new CreateProfilePage(page, isMobile());
        addAddressPage = new AddAddressPage(page, isMobile());
        addressModal = new AddressModal(page, isMobile());
        checkoutPage = new CheckoutPage(page);
        header = new Header(page, isMobile());

        String email = "test_aqa_" + + System.currentTimeMillis() + "@yahoo.com";
        String firstName = "Jane";
        String lastName = "Doe";
        String phoneNumber = "3125550199";
        String streetAddress = "123 W Madison St";
        String city = "Chicago";
        String state = "IL";
        String zipCode = "60602";
        String apt = "5";

        String expectedAddress = firstName + " " + lastName + " " + 
                         streetAddress + " " + 
                         apt + " " + 
                         city + ", " + state + " " + zipCode + " " + 
                         phoneNumber;

        String env = System.getProperty("env", "stg");
        
        if ("prod".equalsIgnoreCase(env)) {
            page.navigate(getProperty("baseUrl"));
        } else {
            page.navigate(getProperty("stagingBaseUrl"));
        }
    
        page.navigate(getProperty("baseUrl") + "/Register.aspx?");

        signInPage.clickGuestNewAcc();

        createProfilePage.fillContactInformation(email, email, true);
        page.waitForTimeout(3000);
        createProfilePage.fillBillingAddress(firstName, lastName, streetAddress, apt, city, state, zipCode, phoneNumber);
        createProfilePage.validateShippingSameAsBillingIsChecked();
        createProfilePage.clickContinueCheckout();

        addAddressPage.confirmVerifiedAddress();

        if (!isMobile()) {
            addressModal.clickSaveAndContinue();
            checkoutPage.validateShippingAddress(expectedAddress);
        } else {
            header.validateSignedInName(firstName);
        }
    }

    @Test
    @DisabledIfSystemProperty(named = "env", matches = "prod")
    @DisplayName("Verify user can create a new account")
    void createNewAccount() {

        signInPage = new SignInPage(page, isMobile());
        createProfilePage = new CreateProfilePage(page, isMobile());
        addAddressPage = new AddAddressPage(page, isMobile());
        addressModal = new AddressModal(page, isMobile());
        checkoutPage = new CheckoutPage(page);
        header = new Header(page, isMobile());

        String email = "test_aqa2_" + + System.currentTimeMillis() + "@yahoo.com";
        String password = "Pass123!$";
        String firstName = "John";
        String lastName = "Doe";
        String phoneNumber = "3125550155";
        String streetAddress = "123 W Madison St";
        String city = "Chicago";
        String state = "IL";
        String zipCode = "60602";
        String apt = "67";

        String expectedAddress = firstName + " " + lastName + " " + 
                         streetAddress + " " + 
                         apt + " " + 
                         city + ", " + state + " " + zipCode + " " + 
                         phoneNumber;

        page.navigate(getProperty("stagingBaseUrl"), 
            new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    
        page.navigate(getProperty("baseUrl") + "/Register.aspx?");

        signInPage.clickGuestNewAcc();

        createProfilePage.fillContactInformation(email, password, false);
        createProfilePage.fillBillingAddress(firstName, lastName, streetAddress, apt, city, state, zipCode, phoneNumber);
        createProfilePage.validateShippingSameAsBillingIsChecked();
        createProfilePage.clickContinueCheckout();

        addAddressPage.confirmVerifiedAddress();

        if (!isMobile()) {
            addressModal.clickSaveAndContinue();
            checkoutPage.validateShippingAddress(expectedAddress);
        } else {
            header.validateSignedInName(firstName);
        }
    }
}

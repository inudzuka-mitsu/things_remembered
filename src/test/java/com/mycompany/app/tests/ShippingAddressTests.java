package com.mycompany.app.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.AccountPage;
import com.mycompany.app.pages.AddAddressPage;
import com.mycompany.app.pages.login.SignInPage;
import com.mycompany.app.pages.modals_popups.AddressModal;

// These tests are configured for TR desktop app (stg and prod)

public class ShippingAddressTests extends TestBase {
    
    private AddAddressPage addAddressPage;
    private SignInPage signInPage;
    private AccountPage accountPage;
    private AddressModal shippingPage;

    @Test
    @DisplayName("Verify user can add a new shipping address")
    void addShippingAddress() {
        signInPage = new SignInPage(page, isMobile());
        addAddressPage = new AddAddressPage(page, isMobile());
        shippingPage = new AddressModal(page, isMobile());
        accountPage = new AccountPage(page, isMobile());

        String testEmail = getProperty("test_email_2");
        String testPassword = getProperty("test_password_2");

        String addressNickname = "QA Test " + System.currentTimeMillis();
        String firstName = "Zhibek";
        String lastName = "Amankulova";
        String phoneNumber = "3125550199";
        String streetAddress = "123 W Madison St";
        String city = "Chicago";
        String state = "IL";
        String zipCode = "60602";

        page.navigate(getProperty("baseUrl") + "/Register.aspx?");
        signInPage.signIn(testEmail, testPassword);

        accountPage.clickManageShippingAddress();

        shippingPage.clickAddNewAddress();
        
        addAddressPage.fillNewAddressFormAndSubmit(
            addressNickname,
            firstName,
            lastName,
            phoneNumber,
            streetAddress,
            city,
            state,
            zipCode
        );
        addAddressPage.clickAddAddress();

        page.waitForTimeout(5000);

        page.navigate(getProperty("baseUrl") + "/AddressBook.aspx");
        shippingPage.validateLastAddress(addressNickname,
            firstName,
            lastName,
            phoneNumber,
            streetAddress,
            city,
            state,
            zipCode);
    }

    @Test
    @DisplayName("Verify user can edit a shipping address")
    void editShippingAddress() throws InterruptedException {
        signInPage = new SignInPage(page, isMobile());
        addAddressPage = new AddAddressPage(page, isMobile());
        shippingPage = new AddressModal(page, isMobile());
        accountPage = new AccountPage(page, isMobile());

        String testEmail = getProperty("test_email_2");
        String testPassword = getProperty("test_password_2");

        String addressNickname = "QA Test Updated" + System.currentTimeMillis();
        String firstName = "New";
        String lastName = "Name";
        String phoneNumber = "3125550199";
        String streetAddress = "123 W Madison St";
        String city = "Chicago";
        String state = "IL";
        String zipCode = "60602";

        int addressToEditIndex = 2;
    
        page.navigate(getProperty("baseUrl") + "/Register.aspx?");
    
        signInPage.signIn(testEmail, testPassword);

        accountPage.clickManageShippingAddress();
        shippingPage.editAddress(addressToEditIndex);
        
        addAddressPage.fillNewAddressFormAndSubmit(
            addressNickname,
            firstName,
            lastName,
            phoneNumber,
            streetAddress,
            city,
            state,
            zipCode
        );
        addAddressPage.clickSaveAddress();

        page.waitForTimeout(5000);

        page.navigate(getProperty("baseUrl") + "/AddressBook.aspx");

        shippingPage.validateAddress(addressToEditIndex, addressNickname,
            firstName,
            lastName,
            phoneNumber,
            streetAddress,
            city,
            state,
            zipCode);
    }
 }

package com.mycompany.app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AddAddressPage extends BasePage {

    private final boolean isMobile;

    public AddAddressPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // --- DESKTOP LOCATORS ---
    // Updated to reflect the 'mainContent' naming convention found in the DOM
    private final String nicknameInput = "#ctl00_mainContent_viewAddressBookControl_txtName";
    private final String firstNameInput = "#ctl00_mainContent_viewAddressBookControl_txtFirstName";
    private final String lastNameInput = "#ctl00_mainContent_viewAddressBookControl_txtLastName";
    private final String phoneInput = "#ctl00_mainContent_viewAddressBookControl_txtPhone";
    private final String addressInput = "#ctl00_mainContent_viewAddressBookControl_txtAddress1";
    private final String cityInput = "#ctl00_mainContent_viewAddressBookControl_txtCity";
    private final String stateDropdown = "#ctl00_mainContent_viewAddressBookControl_txtState";
    private final String zipInput = "#ctl00_mainContent_viewAddressBookControl_txtZip";
    private final String addAddressBtn = "#cmdAddAddress";
    
    private final String saveAddress = "#cmdSaveAddress";
    private final String confirmCheckbox = "input#checkConfirm";
    private final String useThisAddressBtn = "input#cmdUseThisAddress";
    
    // --- MOBILE LOCATORS  ---
    private final String mobileIframe = "iframe.dialog__iframe";
    
    private final String mNicknameInput = "#CustomerTempAddress_Nickname"; 
    private final String mFirstNameInput = "#CustomerTempAddress_CustomerFirstName";
    private final String mLastNameInput = "#CustomerTempAddress_CustomerLastName";
    private final String mPhoneInput = "#CustomerTempAddress_CustomerPhone";
    private final String mAddressInput = "#CustomerTempAddress_CustomerAddress1";
    private final String mCityInput = "#CustomerTempAddress_CustomerCity";
    private final String mStateDropdown = "#CustomerTempAddress_CustomerState";
    private final String mZipInput = "#CustomerTempAddress_CustomerZip";
    private final String mSaveAddress = ".wrapper__btn-bottom input[value*='Save'], input[value='Add New Shipping Address']";
    private final String mConfirmCheckbox = "#ConfirmAddress";
    private final String mUseThisAddressBtn = "#confirmAddrSubmit";

    private Locator getDynamicLocator(String desktopSelector, String mobileSelector) {
        if (isMobile) {
            return page.frameLocator(mobileIframe).locator(mobileSelector);
        } else {
            return page.locator(desktopSelector);
        }
    }

    public void clickAddAddress() {
        Locator submitBtn = getDynamicLocator(addAddressBtn, mSaveAddress);
        
        submitBtn.scrollIntoViewIfNeeded();
        submitBtn.click(new Locator.ClickOptions().setForce(true));
    }

    public void clickSaveAddress() {
       Locator saveBtn = getDynamicLocator(saveAddress, mSaveAddress);
       saveBtn.scrollIntoViewIfNeeded();
       saveBtn.click(new Locator.ClickOptions().setForce(true));
    }

    public void confirmVerifiedAddress() {
        String locator = isMobile ? mConfirmCheckbox : confirmCheckbox;
        String locator2 = isMobile ? mUseThisAddressBtn : useThisAddressBtn;
        page.locator(locator).check(new com.microsoft.playwright.Locator.CheckOptions().setForce(true));
        page.locator(locator2).click();
    }

    public void fillNewAddressFormAndSubmit(String nickname, String fName, String lName, String phone, String address, String city, String stateCode, String zip) {
        
        Locator nicknameLoc = getDynamicLocator(nicknameInput, mNicknameInput);
        nicknameLoc.clear();
        nicknameLoc.fill(nickname);

        Locator fNameLoc = getDynamicLocator(firstNameInput, mFirstNameInput);
        fNameLoc.clear();
        fNameLoc.fill(fName);

        Locator lNameLoc = getDynamicLocator(lastNameInput, mLastNameInput);
        lNameLoc.clear();
        lNameLoc.fill(lName);

        Locator phoneLoc = getDynamicLocator(phoneInput, mPhoneInput);
        phoneLoc.clear();
        phoneLoc.fill(phone);

        Locator addressLoc = getDynamicLocator(addressInput, mAddressInput);
        addressLoc.clear();
        addressLoc.fill(address);

        Locator cityLoc = getDynamicLocator(cityInput, mCityInput);
        cityLoc.clear();
        cityLoc.fill(city);

        Locator stateLoc = getDynamicLocator(stateDropdown, mStateDropdown);
        stateLoc.selectOption(stateCode);
        
        Locator zipLoc = getDynamicLocator(zipInput, mZipInput);
        zipLoc.clear();
        zipLoc.fill(zip);

        if (isMobile) {
            zipLoc.press("Tab"); 
        }
    }
}
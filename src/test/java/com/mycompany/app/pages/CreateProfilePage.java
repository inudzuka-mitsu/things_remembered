package com.mycompany.app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.SelectOption;

public class CreateProfilePage extends BasePage {

    private final boolean isMobile;

    public CreateProfilePage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // --- COMBINED LOCATORS (Desktop ID, Mobile ID) ---
    
    private final String guestCheckbox = "#id_guest_check, #IsGuestCheckout";
    private final String emailInput = "#ctl00_belowHeader_txtEmail, #UserEmail";
    private final String emailConfirmInput = "#ctl00_belowHeader_txtEmail2, #UserEmail2";
    private final String passwordInput = "#ctl00_belowHeader_txtPassword, #NewPassword";
    private final String passwordConfirmInput = "#ctl00_belowHeader_txtPassword2, #Password2";

    private final String billFirstNameInput = "#ctl00_belowHeader_txtBillFirstName, #CustomerBillingAddress_CustomerFirstName";
    private final String billLastNameInput = "#ctl00_belowHeader_txtBillLastName, #CustomerBillingAddress_CustomerLastName";
    private final String billAddress1Input = "#ctl00_belowHeader_txtBillAddress1, #CustomerBillingAddress_CustomerAddress1";
    private final String billAddress2Input = "#ctl00_belowHeader_txtBillAddress2, #CustomerBillingAddress_CustomerAddress2"; 
    private final String billCityInput = "#ctl00_belowHeader_txtBillCity, #CustomerBillingAddress_CustomerCity";
    private final String billCountrySelect = "#ctl00_belowHeader_txtBillCountry, #CustomerBillingAddress_CustomerCountry2";
    private final String billStateSelect = "#ctl00_belowHeader_txtBillState, #CustomerBillingAddress_CustomerState";
    private final String billZipInput = "#ctl00_belowHeader_txtBillZip, #CustomerBillingAddress_CustomerZip";
    private final String billPhoneInput = "#ctl00_belowHeader_txtBillPhone, #PhoneNumberModel_PhoneNumber";
    
    private final String shippingSameAsBillingCheckbox = "#ctl00_belowHeader_shipSameAsBill, #ShippingIsBilling";

    private final String continueButton = "#cmdSubmit, #submitbutton";

    // --- ACTIONS ---

    public void fillContactInformation(String email, String password, boolean isGuest) {
        page.locator(emailInput).fill(email);
        page.locator(emailConfirmInput).fill(email);

        Locator guestChk = page.locator(guestCheckbox);

        if (isGuest) {
            if (!guestChk.isChecked()) {
                System.out.println("Selecting 'Guest Checkout'...");
                // Force click is helpful for custom-styled checkboxes on mobile
                guestChk.check(new Locator.CheckOptions().setForce(true));
            }
            assertThat(guestChk).isChecked();
            
        } else {
            if (guestChk.isChecked()) {
                System.out.println("Unchecking 'Guest Checkout' to enter password...");
                guestChk.uncheck(new Locator.UncheckOptions().setForce(true));
            }
            page.locator(passwordInput).waitFor();
            
            page.locator(passwordInput).fill(password);
            page.locator(passwordConfirmInput).fill(password);
        }
    }

    public void fillBillingAddress(String firstName, String lastName, String address, String apt, 
                                   String city, String state, String zip, String phone) {
        
        page.locator(billFirstNameInput).fill(firstName);
        page.locator(billLastNameInput).fill(lastName);
        page.locator(billAddress1Input).fill(address);
        page.locator(billAddress2Input).fill(apt);
        page.locator(billCityInput).fill(city);
        
        page.selectOption(billCountrySelect, new SelectOption().setValue("US"));
        page.selectOption(billStateSelect, new SelectOption().setValue(state));
        
        page.locator(billZipInput).fill(zip);
        page.locator(billPhoneInput).fill(phone);
    }

    public void validateShippingSameAsBillingIsChecked() {
        Locator checkbox = page.locator(shippingSameAsBillingCheckbox);
        
        System.out.println("Validating 'Shipping Same as Billing' checkbox is checked...");
        assertThat(checkbox).isChecked();
    }

    public void clickContinueCheckout() {
        page.locator(continueButton).click();
    }
}
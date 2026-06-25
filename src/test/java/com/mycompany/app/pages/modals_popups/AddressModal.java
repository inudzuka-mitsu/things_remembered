package com.mycompany.app.pages.modals_popups;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.mycompany.app.pages.BasePage;

public class AddressModal extends BasePage {

    private final boolean isMobile;

    public AddressModal(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // --- COMBINED DESKTOP & MOBILE LOCATORS ---
    
    private final String shipToAddressBtn = "input[value='Ship To This Address'], button:has-text('Ship To This Address')";
    
    private final String saveAndContinueBtn = "input#ctl00_belowHeader_saveContinueBtn, button#ctl00_belowHeader_saveContinueBtn"; 
    
    private final String addressTextLocator = ".cstAddress, .adressbook-address";
    private final String addressCardContainer = ".itembox, .box__address-block";
    private final String addNewAddressBtn = "#ctl00_belowHeader_viewAddressBookControl_btn_addnewaddress, button#newAddressSubmit, button#ctl00_mainContent_viewAddressBookControl_btn_addnewaddress";

    // --- ACTIONS --- 

    public void selectFirstAddressAndShip() {
        page.locator(shipToAddressBtn).first().click(new Locator.ClickOptions().setForce(true));
    }

    public void selectSecondAddressAndShip() {
        page.locator(shipToAddressBtn).nth(1).click(new Locator.ClickOptions().setForce(true));
    }

    public String selectFirstAddressAndReturnText() {
        Locator firstCard = page.locator(addressCardContainer).first();
        Locator addressBlock = firstCard.locator(addressTextLocator);

        String fullText = addressBlock.innerText();
        Locator nicknameLocator = addressBlock.locator(".addrName");

        String cleanAddress = fullText;
        
        if (nicknameLocator.count() > 0) {
            String nickname = nicknameLocator.innerText();
            cleanAddress = cleanAddress.replace(nickname, "");
        }
        cleanAddress = cleanAddress.replace("Phone:", "").trim();
        
        firstCard.locator(shipToAddressBtn).click(new Locator.ClickOptions().setForce(true));
        
        return cleanAddress;
    }

    public String selectSecondAddressAndReturnText() {
        Locator firstCard = page.locator(addressCardContainer).nth(1);
        Locator addressBlock = firstCard.locator(addressTextLocator);
        String fullText = addressBlock.innerText();
        Locator nicknameLocator = addressBlock.locator(".addrName");

        String cleanAddress = fullText;

        if (nicknameLocator.count() > 0) {
            String nickname = nicknameLocator.innerText();
            cleanAddress = cleanAddress.replace(nickname, "");
        }

        cleanAddress = cleanAddress.replace("Phone:", "").trim();
        
        firstCard.locator(shipToAddressBtn).click(new Locator.ClickOptions().setForce(true));
        
        return cleanAddress;
    }

    public void editAddress(int index) {
        Locator card = page.locator(addressCardContainer).nth(index);
        
        String editBtnLocator = isMobile ? "a:has-text('Edit')" : "input[value='Edit']";
        
        card.locator(editBtnLocator).click(new Locator.ClickOptions().setForce(true));
    }

    public void clickSaveAndContinue() {
        page.waitForTimeout(9000); 
        if (!isMobile) {
            page.locator(saveAndContinueBtn).click();
        } else {
            Locator giftOptionsContinueBtn = page.locator(".gift-input-wrapper input#submitButton").first();
            giftOptionsContinueBtn.scrollIntoViewIfNeeded();
            giftOptionsContinueBtn.click(new Locator.ClickOptions().setForce(true));
        }
    }

    public void clickAddNewAddress() {
        page.locator(addNewAddressBtn).click();
    }

    public void validateLastAddress(String nickname, String firstName, String lastName, 
                                    String address, String city, String state, String zip, String phone) {

        Locator lastCard = page.locator(addressCardContainer).last();
        Locator addressBlock = lastCard.locator(addressTextLocator);
        
        assertThat(addressBlock).containsText(nickname);
        assertThat(addressBlock).containsText(firstName + " " + lastName);
        assertThat(addressBlock).containsText(address);
        assertThat(addressBlock).containsText(city);
        assertThat(addressBlock).containsText(state);
        assertThat(addressBlock).containsText(zip);
        assertThat(addressBlock).containsText(phone);
    }

    public void validateAddress(int index, String nickname, String firstName, String lastName, 
                                    String address, String city, String state, String zip, String phone) {

        Locator card = page.locator(addressCardContainer).nth(index);
        Locator addressBlock = card.locator(addressTextLocator);
        
        assertThat(addressBlock).containsText(nickname);
        assertThat(addressBlock).containsText(firstName + " " + lastName);
        assertThat(addressBlock).containsText(address);
        assertThat(addressBlock).containsText(city);
        assertThat(addressBlock).containsText(state);
        assertThat(addressBlock).containsText(zip);
        assertThat(addressBlock).containsText(phone);
    }
}
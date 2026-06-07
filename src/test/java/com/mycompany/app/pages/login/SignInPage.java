package com.mycompany.app.pages.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.mycompany.app.pages.BasePage;

public class SignInPage extends BasePage {

    private final boolean isMobile;

    public SignInPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // DESKTOP APP LOCATORS

    private final String emailInput = "input[id*='Email']";
    private final String passwordInput = "input[id*='Password']";
    private final String guestNewAccountBtn = "[name='ctl00$belowHeader$Button2']";

    // MOBILE APP LOCATORS

    private final String mobileGuestNewAccountBtn = "a[href='/CreateProfile.aspx']";

    public void enterEmail(String email) {
        page.locator(emailInput).fill(email);
    }

    public void enterPassword(String password) {
        page.locator(passwordInput).fill(password);
    }

    public void clickGuestNewAcc() {
        String locator = isMobile ? mobileGuestNewAccountBtn : guestNewAccountBtn;
        page.locator(locator).click();
    }

    public void clickSignIn() {
        Locator submitButton = page.locator(
            "button:has-text('Sign In'), " +       
            "input[value='Sign In'], " +          
            "input[id*='SignIn'], " +             
            "input[id*='Login'], " +               
            "[type='submit']"                     
        ).first();
        submitButton.click(new Locator.ClickOptions().setForce(true));
    }

    public void signIn(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
    }
}

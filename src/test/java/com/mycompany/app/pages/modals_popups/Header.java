package com.mycompany.app.pages.modals_popups;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.mycompany.app.pages.BasePage;

public class Header extends BasePage {

    private final boolean isMobile;

    public Header(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // DESKTOP APP LOCATORS

    private final String hamburgerMenuIcon = "#menuToggle";
    private final String signInLink = "a.link__signin-my-account";
    private final String accountMenuTrigger = "a.link__signin-my-account";

    // MOBILE APP LOCATORS

    private final String mobileSignInLink = "a.link__sign-in:has-text('Sign In')";
    private final String signedInNameLabel = "a span.sp__hello-name";
    private final String mobileCartLink = "a span.sp__icon-cart";

    public void clickHamburgerMenu() {
        page.locator(hamburgerMenuIcon).click(new Locator.ClickOptions().setForce(true));
    }

    public void clickSignIn() {
        if (isMobile) {
            page.locator(mobileSignInLink).last().click();
        } else {
            if (page.locator(accountMenuTrigger).isVisible()) {
                 page.locator(accountMenuTrigger).hover();
            }
            page.locator(signInLink).click();
        }
    }

    public void clickCartIcon() {
        page.locator(mobileCartLink).click();
    }

    public void validateSignedInName(String name) {
        System.out.println("Validating signed-in name contains: " + name);
        Locator nameElement = page.locator(signedInNameLabel).last();
        assertThat(nameElement).containsText(name);
    }
}
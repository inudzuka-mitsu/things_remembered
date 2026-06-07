package com.mycompany.app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePage extends BasePage {

    private final boolean isMobile;
    
    public HomePage(Page page, boolean isMobile) {
        super(page); 
        this.isMobile = isMobile;
    }

    // --- DESKTOP APP LOCATORS ---
    private final String searchBar = "input#searchBox";
    private final String viewCartButton = "div.right.fl a[href*='Cart.aspx']";
    private final String checkoutBtn = "";
    private final String addedToCartHeader = "";
    private final String personalizationDetails = "";
    private final String forHerNavLink = ".shop-recipients-list a:text-is('For Her')";

    // --- MOBILE APP LOCATORS ---
    private final String mobileSearchBar = "";
    private final String mobileForHerLink = "";
    private final String mobileAddedToCartHeader = "";
    private final String mobilePersonalizationDetails = "";
    private final String mobileViewCartButton = "";
    private final String mobileCheckoutBtn = "";

    public void typeProduct(String productName) {
       String locator = isMobile ? mobileSearchBar : searchBar;
       page.locator(locator).click();
       page.locator(locator).fill(productName);
    }

    public void clickForHer() {
        String locator = isMobile ? mobileForHerLink : forHerNavLink;
        page.locator(locator).scrollIntoViewIfNeeded();
        page.locator(locator).click();
    }

    public void searchProduct() {
        String locator = isMobile ? mobileSearchBar : searchBar;
        page.locator(locator).press("Enter");
    }

    public void clickViewCart() {
        String locator = isMobile ? mobileViewCartButton : viewCartButton;
        page.locator(locator).click(new Locator.ClickOptions().setForce(true));
    }

    public void clickCheckout() {
        String locator = isMobile ? mobileCheckoutBtn : checkoutBtn;
        page.locator(locator).first().click(new Locator.ClickOptions().setForce(true));
    }

    public void validateAddedToCartVisible() {
        if (isMobile) {
            Locator header = page.locator(mobileAddedToCartHeader);
            assertThat(header).isVisible();
            assertThat(header).containsText("added this item to your cart"); 
        } else {
            Locator header = page.locator(addedToCartHeader);
            assertThat(header).isVisible();
            assertThat(header).containsText("Added To Cart");
        }
    }

    public void validatePersonalization(String color, String font, String name) {
        String locator = isMobile ? mobilePersonalizationDetails : personalizationDetails;
        Locator details = page.locator(locator).first();
        
        System.out.println(">>> Waiting for personalization details to render...");
        details.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE).setTimeout(15000));

        if (color != null && !color.isEmpty()) {
            assertThat(details).containsText(color);
        }

        if (font != null && !font.isEmpty()) {
            assertThat(details).containsText(font);
        }

        if (name != null && !name.isEmpty()) {
            assertThat(details).containsText(name);
        }
    }
}
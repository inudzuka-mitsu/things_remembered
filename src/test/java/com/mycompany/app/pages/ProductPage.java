package com.mycompany.app.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductPage extends BasePage {

    private final boolean isMobile;

    public ProductPage(Page page, boolean isMobile) {
        super(page); 
        this.isMobile = isMobile;
    }

    // --- UNIFIED LOCATORS ---
    private final String mainActionBtn = "#btnPersonalize";
    
    private final String handleColorDropdown = "#option-select-container select";


    public void clickPersonalizeBtn() {
        System.out.println(">>> Clicking Personalize...");
        Locator btn = page.locator(mainActionBtn).first();
        
        btn.scrollIntoViewIfNeeded();
        btn.click(new Locator.ClickOptions().setForce(true));
    }

    public void clickStartDesigning() {
        System.out.println(">>> Clicking Start Designing...");
        Locator btn = page.locator(mainActionBtn).first();
        
        btn.scrollIntoViewIfNeeded();
        btn.click(new Locator.ClickOptions().setForce(true));
    }

    public void validateDefaultHandleColor(String expectedText) {
        Locator selectedOption = page.locator(handleColorDropdown).locator("option:checked");
        assertThat(selectedOption).containsText(expectedText);
    }
}
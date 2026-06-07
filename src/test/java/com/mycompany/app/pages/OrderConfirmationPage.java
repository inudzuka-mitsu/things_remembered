package com.mycompany.app.pages;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class OrderConfirmationPage extends BasePage {

    public OrderConfirmationPage(Page page) {
        super(page); 
    }

    public void verifyOrderSuccessMessage() {
        System.out.println("Validating Order Success Message...");
        
        assertThat(page.locator("body")).containsText(
            "has been successfully entered into our system"
        );
            
        assertThat(page.locator("body")).containsText(
            "A complete order confirmation will be emailed to you"
        );
        
        System.out.println("Order Success Message validated!");
    }
}
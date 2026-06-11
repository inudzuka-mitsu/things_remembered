package com.mycompany.app.pages;

import com.microsoft.playwright.Page;

public class OrderHistoryPage extends BasePage {

    private final boolean isMobile;

    public OrderHistoryPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // --- DESKTOP APP LOCATORS ---
    private final String viewDetailsButton = ".content__wrapper .content-right-col a.base__btn";
    private final String reorderBtn = ".btn_wrapper .reorderItem, div.btn_wrapper a[data-action='reorderitem']";
    private final String reportIssueBtn = ".btn_wrapper a:text-is('REPORT ISSUE'), a:has-text('Report Issue')";
    private final String writeReviewBtn = ".btn_wrapper a:text-is('WRITE A REVIEW'), a:has-text('Write a Review')";

    private final String productContainer = "td.cart-item-content";
    private final String itemNameLoc = productContainer + " > div:nth-child(1) > b:nth-child(1)";
    private final String itemNumberLoc = productContainer + " > div:nth-child(1) > b:nth-child(2)";
    private final String quantityLoc = productContainer + " >> text=Quantity: >> b";
    private final String priceLoc = productContainer + " .sale.info b";

    // --- MOBILE APP LOCATORS ---
    private final String mobileViewDetailsButton = "a.txt__view-details";
    
    // Both Name and Number share this same locator on mobile
    private final String mobileItemNameAndNumber = ".cartProduct_name"; 
    
    private final String mobileQuantityLoc = ".divCartInfo li:has-text('Qty:') span";
    private final String mobilePriceLoc = ".divCartInfo li:has-text('Price:') b";

    public void clickViewDetailsOfFirstOrder() {
        String locator = isMobile ? mobileViewDetailsButton : viewDetailsButton;
        page.locator(locator).first().click();
    }

    public void clickReorder() {
        page.locator(reorderBtn).first().click();
    }

    public void clickReportIssue() {
        page.locator(reportIssueBtn).first().click();
    }

    public void clickWriteReview() {
        page.locator(writeReviewBtn).first().click();
    }

    public String getItemName() {
        String activeLocator = isMobile ? mobileItemNameAndNumber : itemNameLoc;
        String rawText = page.locator(activeLocator).first().innerText().trim();
        
        if (isMobile && rawText.contains("- Item#:")) {
            return rawText.split("- Item#:")[0].trim();
        }
        return rawText;
    }

    public String getItemNumber() {
        String activeLocator = isMobile ? mobileItemNameAndNumber : itemNumberLoc;
        String rawText = page.locator(activeLocator).first().innerText().trim();
        
        if (isMobile && rawText.contains("- Item#:")) {
            return rawText.split("- Item#:")[1].trim();
        }
        return rawText;
    }

    public String getItemQuantity() {
        String activeLocator = isMobile ? mobileQuantityLoc : quantityLoc;
        return page.locator(activeLocator).first().innerText().trim();
    }

    public String getItemPrice() {
        String activeLocator = isMobile ? mobilePriceLoc : priceLoc;
        return page.locator(activeLocator).first().innerText().trim();
    }
}
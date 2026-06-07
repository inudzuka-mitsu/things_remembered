package com.mycompany.app.pages.modals_popups;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.mycompany.app.pages.BasePage;

public class CustomizeGiftModal extends BasePage {

    private final boolean isMobile;

    private final FrameLocator modalFrame;
    private final FrameLocator mobileModalFrame;

    public CustomizeGiftModal(Page page, boolean isMobile) {
        super(page);
        this.modalFrame = page.frameLocator("#pmallmodaliframe");
        this.mobileModalFrame = page.frameLocator("#personalizationView");
        this.isMobile = isMobile;
    }

    public void selectClassicGiftBox() {
        if (!isMobile) {
            modalFrame.getByLabel("Classic gift box")
                  .check(new Locator.CheckOptions().setForce(true));
        } else {
            mobileModalFrame.getByLabel("Classic gift box")
                  .check(new Locator.CheckOptions().setForce(true));
        }
        
    }

    public void clickContinue() {
        if (!isMobile) {
            modalFrame.getByText("Continue").click();
        } else {
            mobileModalFrame.getByText("Continue").click();
        }
    } 
}

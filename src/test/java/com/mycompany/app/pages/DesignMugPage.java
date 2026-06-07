package com.mycompany.app.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class DesignMugPage extends BasePage {

    public DesignMugPage(Page page) {
        super(page);
    }

    private final String dyoIframeSelector = "#DyoFrame";
    private final String skipButton = "#btnSkip"; 
    private final String gotItButton = "button:has-text('Got It')"; 
    private final String proceedButton = "#products-settings, button#addtocart";

    public void clickSkip() {
        FrameLocator dyoFrame = page.frameLocator(dyoIframeSelector);
        Locator skipBtn = dyoFrame.locator(skipButton).or(dyoFrame.locator(gotItButton)).first();

        try {
            System.out.println("Checking for tutorial overlay...");
            skipBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));

            System.out.println("Tutorial found. Clicking Skip...");
            skipBtn.click(new Locator.ClickOptions().setForce(true));
            skipBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(5000));
        } catch (Exception e) {
            System.out.println("Tutorial overlay did not appear. Proceeding.");
        }
    }

    public void clickProceed() {
        FrameLocator dyoFrame = page.frameLocator(dyoIframeSelector);
        Locator btn = dyoFrame.locator(proceedButton).first();
        
        System.out.println("Waiting for Proceed button (#products-settings)...");
        btn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        System.out.println("Clicking PROCEED...");
        btn.click(new Locator.ClickOptions().setForce(true));
    }
}
package com.mycompany.app.pages.modals_popups;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mycompany.app.pages.BasePage;

public class DesignPopup extends BasePage {

    public DesignPopup(Page page) {
        super(page);
    }

    public DesignPopup(Page page, boolean isMobile) {
        super(page);
    }

    private final String popupContainerSelector = ".PresentationValidatorPopup";
    private final String confirmationCheckboxSelector = "span[data-sid='validationPopupCheckBox']"; 
    private final String proceedButtonSelector = "button[data-sid='popupDrawerPrimaryButton']";

    private boolean isPopupInIframe = false;

    private Locator getDynamicLocator(String selector) {
        if (isPopupInIframe) {
            return page.frameLocator("#app_iframe").locator(selector).first();
        } else {
            return page.locator(selector).first();
        }
    }

    public void handleValidationPopup() {
        System.out.println(">>> Checking for validation popup...");
        
        long endTime = System.currentTimeMillis() + 20000;
        boolean popupAppeared = false;
        
        while (System.currentTimeMillis() < endTime) {
            
            if (page.locator(popupContainerSelector).first().isVisible()) {
                isPopupInIframe = false;
                popupAppeared = true;
                System.out.println(">>> Popup found on the Main Page!");
                break;
            }
            
            if (page.locator("#app_iframe").count() > 0 && 
                page.frameLocator("#app_iframe").locator(popupContainerSelector).first().isVisible()) {
                isPopupInIframe = true;
                popupAppeared = true;
                System.out.println(">>> Popup found INSIDE the Iframe!");
                break;
            }
            
            page.waitForTimeout(500);
        }

        if (!popupAppeared) {
            System.out.println(">>> No validation popup appeared after 20 seconds. Moving on...");
            return;
        }

        System.out.println(">>> Validation popup detected. Handling it...");
        
        Locator checkbox = getDynamicLocator(confirmationCheckboxSelector);
        checkbox.scrollIntoViewIfNeeded();
        
        System.out.println(">>> Clicking checkbox...");
        checkbox.click(); 
        
        Locator proceedBtn = getDynamicLocator(proceedButtonSelector);
        
        System.out.println(">>> Waiting for proceed button to enable...");
        assertThat(proceedBtn).isEnabled();
        
        System.out.println(">>> Clicking proceed...");
        proceedBtn.click();
        
        System.out.println(">>> Waiting for popup to disappear...");
        getDynamicLocator(popupContainerSelector).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(10000));
        
        System.out.println(">>> Validation popup cleared successfully.");
    }
}
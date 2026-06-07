package com.mycompany.app.pages;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.SelectOption;

public class PhotoEditorPage extends BasePage {

    private final boolean isMobile;

    public PhotoEditorPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // --- COMBINED LOCATORS ---
    
    private final String editorIframe = "iframe[name='pmallmodaliframe'], iframe#personalizationView";
    
    // Locators that exist on BOTH desktop and mobile
    private final String categoryDropdown = "select[name='message-library-category']"; 
    private final String libraryMessageList = ".message-library-content li, .msgSelection li"; 
    
    // Desktop-Specific Studio Locators
    private final String photoTab = "//button[contains(text(), 'Photos')]"; 
    private final String designTab = "//button[contains(text(), 'Design')]"; 
    private final String addPhotoLaterBtn = "button:has-text('Add Photos Later')";
    private final String libraryPhotoSource = ".LibraryPhotosList .PhotoPreview";
    private final String productPhotoSlot = ".PhotoSlot";
    private final String addToCartContainer = ".OrderButtonContainer";
    private final String desktopAddToCartBtn = "#ctl00_mainContent_addToCart_addToCartButton";
    private final String desktopContinueBtn = "a#cmdAddonGiftBoxV3";

    // Mobile-Specific Form Locators
    private final String studioUploadBtn = ".UploadLocalFilesButton, .EmptyBottomLibraryCTAButton";
    private final String mobileSelectPhotoBtn = ".photo-upload-btn, .select_photo_button__pdp-upload";
    private final String mobileContinueBtn = "input#continueShoppingLink";

    // --- ACTIONS ---

    private FrameLocator getEditorFrame() {
        return page.frameLocator(editorIframe);
    }

    public void selectCategory(String categoryName) {
        page.waitForTimeout(5000);
        System.out.println("Selecting category: " + categoryName);
        Locator dropdown = getEditorFrame().locator(categoryDropdown).first();
        dropdown.scrollIntoViewIfNeeded();
        dropdown.selectOption(new SelectOption().setLabel(categoryName));
    }

    public void selectFirstLibraryMessage() {
        page.waitForTimeout(5000);
        System.out.println("Selecting the first message from the library...");
        Locator listItems = getEditorFrame().locator(libraryMessageList);
        listItems.first().waitFor();

        if (listItems.count() > 0) {
            listItems.first().scrollIntoViewIfNeeded();
            listItems.first().click(new Locator.ClickOptions().setForce(true));
        } else {
            throw new RuntimeException("Library list is empty!");
        }
    }

    public void clickPhotoTab() {
        if (!isMobile) {
            System.out.println("Switching to Photo Tab...");
            Locator tab = getEditorFrame().locator(photoTab).first();
            tab.waitFor();
            tab.click(new Locator.ClickOptions().setForce(true));
        } else {
            Locator btn = getEditorFrame().locator("button.PhotoSlot").first();
            btn.click(new Locator.ClickOptions().setForce(true));
        }
    }

    public void clickDesignTab() {
        if (!isMobile) {
            System.out.println("Switching to Design Tab...");
            Locator tab = getEditorFrame().locator(designTab).first();
            tab.waitFor();
            tab.click(new Locator.ClickOptions().setForce(true));
        } else {
            Locator btn = getEditorFrame().locator("button.PhotoSlot").first();
            btn.click(new Locator.ClickOptions().setForce(true));
        }
    }

    public void clickAddPhotoLater() {
    
            System.out.println("Clicking 'Add Photo Later'...");
            Locator btn = getEditorFrame().locator(addPhotoLaterBtn).first();
            btn.waitFor();
            btn.click(new Locator.ClickOptions().setForce(true));
        
    }

    public void uploadPhoto(String absoluteFilePath) {
        System.out.println("Uploading photo from: " + absoluteFilePath);
        
        Locator legacyMobileBtn = getEditorFrame().locator(mobileSelectPhotoBtn).first();
        
        if (isMobile && legacyMobileBtn.isVisible()) {
            System.out.println("Detected legacy mobile form. Opening crop iframe...");
            legacyMobileBtn.click(new Locator.ClickOptions().setForce(true));
        } 
        else {
            System.out.println("Detected Studio Editor. Launching file chooser...");
            FileChooser fileChooser = page.waitForFileChooser(() -> {
                Locator btn = getEditorFrame().locator(studioUploadBtn).last();
                btn.scrollIntoViewIfNeeded();
                btn.click(new Locator.ClickOptions().setForce(true));
            });
            fileChooser.setFiles(Paths.get(absoluteFilePath));
        }
        
        System.out.println("Photo uploaded successfully.");
    }

    public void dragPhotoToSlot() {
        System.out.println("Assigning photo to slot...");
        Locator loadedPhoto = getEditorFrame().locator(".PhotoPreview.isLoaded").last();
        loadedPhoto.waitFor();

        if (!isMobile) {
            System.out.println("Desktop Flow: Dragging photo to slot...");
            Locator source = getEditorFrame().locator(libraryPhotoSource).last();
            Locator target = getEditorFrame().locator(productPhotoSlot).first();
            
            source.dragTo(target);
            System.out.println("Photo dragged successfully.");
        } else {
            System.out.println("Mobile Flow: Tapping photo to assign to selected slot...");
            loadedPhoto.scrollIntoViewIfNeeded();
            loadedPhoto.click(new Locator.ClickOptions().setForce(true));
            System.out.println("Photo assigned successfully.");
        }
    }

    public void clickAddToCart() {
        System.out.println("Clicking 'Add To Cart'...");

        if (isMobile) {
            Locator btn = getEditorFrame().locator("button#addToCartLink, #submitButton").first();
            
            System.out.println(">>> Waiting for button to attach to DOM...");
            btn.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.ATTACHED));
            
            System.out.println(">>> Firing JavaScript Native Click...");
            btn.evaluate("node => node.click()");
            
        } else {
            getEditorFrame().locator(addToCartContainer + ":not(.isDisabled)").waitFor();
            getEditorFrame().locator(desktopAddToCartBtn).click();
        }
        
        System.out.println("Add to Cart clicked.");
    }
    
    public void clickContinue() {
        System.out.println("Clicking 'Continue'...");

        if (isMobile) {
            Locator btn = getEditorFrame().locator(mobileContinueBtn).first();
            
            System.out.println(">>> Waiting for mobile Continue button to attach...");
            btn.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.ATTACHED));
            
            System.out.println(">>> Firing JavaScript Native Click on Continue...");
            btn.evaluate("node => node.click()");
        } else {
            Locator btn = getEditorFrame().locator(desktopContinueBtn).first();
            btn.waitFor();
            btn.click(new Locator.ClickOptions().setForce(true));
        }
        
        System.out.println("Continue clicked.");
    }

    public void verifyPhotoAssignedToSlot() {
        System.out.println(">>> Verifying photo is rendered in the slot...");
        
        Locator slotCanvas = getEditorFrame().locator(".PhotoSlot .PhotoSlotImagePreview").first();
        slotCanvas.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.ATTACHED).setTimeout(15000));
        
        Locator slotOverlay = getEditorFrame().locator(".PhotoSlot .PhotoSlotOverlay").first();
        assertThat(slotOverlay).not().hasClass(Pattern.compile(".*isEmpty.*"));
        
        Locator mainPreviewImage = getEditorFrame().locator(".ProjectPreviewImage").first();
        assertThat(mainPreviewImage).hasAttribute("src", Pattern.compile(".*\\.jpg.*", Pattern.CASE_INSENSITIVE));

        System.out.println(">>> Photo assignment verified successfully!");
    }
}
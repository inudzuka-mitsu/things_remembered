package com.mycompany.app.pages.modals_popups;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mycompany.app.pages.BasePage;

public class PersonalizeItemModal extends BasePage {

    public PersonalizeItemModal(Page page) {
        super(page);
    }

    // --- UNIFIED DESKTOP & MOBILE LOCATORS ---
    private final String threadColorDropdown = "tr:has(.pers-title:has-text('Thread Color')) + tr .dropdown-btn, fieldset:has(.personalization_name:has-text('Thread Color')) .dropdown-btn, div.dropdown-btn";
    private final String fontDropdown = "tr:has(.pers-title:has-text('Font')) + tr .dropdown-btn, fieldset:has(.personalization_name:has-text('Font')) .dropdown-btn, div.dropdown-btn";
    private final String colorDropdownByLabel = "tr:has(.pers-title:has-text('Color')) + tr .dropdown-btn, fieldset:has(.personalization_name:has-text('Color')) .dropdown-btn, div.dropdown-btn";
    
    private final String activeDropdownOptions = ".select-active li[data-val='%s'], .select-active li[data-option='%s']";
    private final String productImage = "#productImage";
    private final String continueButton = "input#ctl00_mainContent_addToCart_addToCartButton, button#addToCartLink[value='Continue']";
    private final String contBtn = "input#continueShoppingLink, #cmdAddonGiftBox";
    private final String addToCartBtn = "input[value='Add To Cart']";
    private final String noGiftBoxRadio = "label:has-text('No Gift Box')";

    private final String uploadPhotoBtn = "div.uploadthumb img, div.uploadthumbnail img";

    private Locator getLocator(String selector) {
        if (page.locator("#pmallmodaliframe").count() > 0) {
            return page.frameLocator("#pmallmodaliframe").locator(selector);
        } 
        else if (page.locator("iframe#personalizationView").count() > 0) {
            return page.frameLocator("iframe#personalizationView").locator(selector);
        } 
        else {
            return page.locator(selector);
        }
    }

    private Locator getModalElement(String selector) {
        // Poll for the element to appear in ANY context (Main Page, Mobile Frame, or Desktop Frame)
        for (int i = 0; i < 30; i++) { // Polls for up to 15 seconds
            
            // 1. Check Main Page (Typical for mobile modal breakouts)
            if (page.locator(selector).count() > 0) {
                return page.locator(selector);
            }
            
            // 2. Check Mobile Frame
            if (page.locator("#personalizationView").count() > 0 && 
                page.frameLocator("#personalizationView").locator(selector).count() > 0) {
                return page.frameLocator("#personalizationView").locator(selector);
            }
            
            // 3. Check Desktop Frame
            if (page.locator("#pmallmodaliframe").count() > 0 && 
                page.frameLocator("#pmallmodaliframe").locator(selector).count() > 0) {
                return page.frameLocator("#pmallmodaliframe").locator(selector);
            }
            
            page.waitForTimeout(500); // wait half a second before trying again
        }
        
        // Fallback to let Playwright handle the timeout natively if it never appears
        return page.locator(selector);
    }

    public void uploadPhoto(String filePath) {
        // 1. Click the thumbnail using your standard locator (since you confirmed this works)
        getLocator(uploadPhotoBtn).click();
        
        // 2. The crop UI might be injected into the main page OR stay in the iframe.
        // Use the polling locator to find exactly where it rendered.
        Locator fileInput = getModalElement("#hFinderUploadFile");
        fileInput.setInputFiles(Paths.get(filePath));
        
        // 3. Find the canvas and save button dynamically
        Locator canvas = getModalElement("canvas#previewCanvas");
        canvas.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        
        Locator saveCropBtn = getModalElement("a#saveCrop");
        saveCropBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        saveCropBtn.click();
    }

    public void fillInputByLabel(String labelText, String value) {
        String selector = String.format("tr:has(.pers-title:has-text('%s')) + tr input, input[data-val-required*='%s']", labelText, labelText);
        
        Locator input = getLocator(selector).first();
        input.scrollIntoViewIfNeeded();
        input.clear();
        input.fill(value);
        input.press("Tab");
    }

    public void selectThreadColor(String color) {
        Locator dropdown = getLocator(threadColorDropdown).first();
        dropdown.scrollIntoViewIfNeeded();
        dropdown.click(new Locator.ClickOptions().setForce(true));
        
        String optionLocator = String.format(activeDropdownOptions, color, color);
        Locator colorOption = getLocator(optionLocator).first();
        colorOption.scrollIntoViewIfNeeded();
        colorOption.click(new Locator.ClickOptions().setForce(true));
    }

    public void selectFont(String font) {
        Locator dropdown = getLocator(fontDropdown).last();
        dropdown.scrollIntoViewIfNeeded();
        dropdown.click(new Locator.ClickOptions().setForce(true));
        
        String optionLocator = String.format(activeDropdownOptions, font, font);
        Locator fontOption = getLocator(optionLocator).first();
    
        fontOption.scrollIntoViewIfNeeded();
        fontOption.click(new Locator.ClickOptions().setForce(true));
    }

    public void selectColor(String color) {
        Locator dropdown = getLocator(colorDropdownByLabel).first();
        dropdown.scrollIntoViewIfNeeded();
        dropdown.click(new Locator.ClickOptions().setForce(true));

        String optionLocator = String.format(activeDropdownOptions, color, color);
        Locator colorOption = getLocator(optionLocator).first();
        
        colorOption.scrollIntoViewIfNeeded();
        colorOption.click(new Locator.ClickOptions().setForce(true));
    }

    public void selectNoGiftBox() {
        getLocator(noGiftBoxRadio).click();
    }

    public void enterName(String name) {
        fillInputByLabel("Name", name);
    }

    public void enterMonogram(String monogram) {
        fillInputByLabel("Initial Monogram", monogram);
    }

    public void enterGiftSetName(String name) {
        fillInputByLabel("Name", name);
    }

    public void checkPersonalizationCorrect() {
        System.out.println(">>> Confirming personalization...");
        Locator confirmLabel = getLocator("label:has-text('The personalization I entered is correct')").first();
        confirmLabel.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        confirmLabel.click();
    }

    public void clickContinue() {
        System.out.println(">>> Waiting for the Continue button to become enabled...");
        
        Locator btn = getLocator(continueButton).first();
        
        btn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertThat(btn).isEnabled();
        
        System.out.println(">>> Button is enabled. Clicking Continue...");
        
        btn.scrollIntoViewIfNeeded();
        btn.click(new Locator.ClickOptions().setForce(true));
    }

    public void clickContinueBtn() {
        getLocator(contBtn).click();
    }

    public void clickAddToCart() {
        System.out.println(">>> Waiting for the Add to Cart button to become enabled...");
        
        Locator btn = getLocator(addToCartBtn).first();
        
        btn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertThat(btn).isEnabled();
        
        System.out.println(">>> Button is enabled. Clicking Add to Cart...");
        
        btn.scrollIntoViewIfNeeded();
        btn.click(new Locator.ClickOptions().setForce(true));
    }

    public void fillPersonalizationAndAddToCart(String color, String font, String name) {
        selectThreadColor(color);
        selectFont(font);
        enterName(name);
        verifyPreviewImagePersonalization(color, font, name);
        checkPersonalizationCorrect();
        clickAddToCart();
    }

    public void fillGiftSetPersonalizationAndAddToCart(String monogram, String name) throws InterruptedException {
        enterGiftSetName(name);
        enterMonogram(monogram);
        Thread.sleep(5000);
        verifyGiftSetPreviewImage(monogram, name);
        clickContinue();
        Thread.sleep(5000);
    }

    public void verifyPreviewImagePersonalization(String color, String font, String name) {
        List<String> activeParams = new ArrayList<>();
        
        if (color != null && !color.isEmpty()) activeParams.add(color);
        if (font != null && !font.isEmpty()) activeParams.add(font);
        if (name != null && !name.isEmpty()) activeParams.add(name);

        StringBuilder regexBuilder = new StringBuilder(".*");

        for (int i = 0; i < activeParams.size(); i++) {
            int valueIndex = i + 1; 
            String safeValue = escapeForRegex(activeParams.get(i));
            regexBuilder.append("value").append(valueIndex).append("=").append(safeValue).append(".*");
        }

        String finalRegex = regexBuilder.toString();
        System.out.println("Validating Image Src with Dynamic Regex: " + finalRegex);

        Pattern srcPattern = Pattern.compile(finalRegex, Pattern.CASE_INSENSITIVE);

        assertThat(getLocator(productImage))
            .hasAttribute("src", srcPattern, 
                new LocatorAssertions.HasAttributeOptions().setTimeout(20000));
    }

    private String escapeForRegex(String input) {
        return input.replace(" ", "+").replace("+", "\\+");
    }

    public void verifyGiftSetPreviewImage(String monogram, String name) {
        String safeMonogram = monogram.replace(" ", "+").replace("+", "\\+");
        String safeName = name.replace(" ", "+").replace("+", "\\+");

        String regex = String.format(".*value1=%s.*value2=%s.*", safeMonogram, safeName);
        Pattern srcPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        assertThat(getLocator(productImage))
            .hasAttribute("src", srcPattern, 
                new LocatorAssertions.HasAttributeOptions().setTimeout(20000));
    }

    public void enterMessage(String message) {
        fillTextAreaByLabel("Message", message); 
    }

    public void fillTextAreaByLabel(String labelText, String value) {
        String selector = String.format("tr:has(.pers-title:has-text('%s')) + tr textarea, textarea[data-val-required*='%s']", labelText, labelText);
        Locator input = getLocator(selector).first();
        input.scrollIntoViewIfNeeded();
        input.clear();
        input.fill(value);
        input.press("Tab");
    }
}
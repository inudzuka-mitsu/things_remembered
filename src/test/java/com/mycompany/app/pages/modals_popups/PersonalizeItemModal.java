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

    private final String monogramRadio = "label:has-text('Monogram'):visible";
    private final String nameRadio = "label:has-text('Name'):visible";
    private final String initialRadio = "label:has-text('Initial'):visible";

    private final String initialInput = "input[placeholder='Initial']:visible";
    private final String nameInputBox = "input[placeholder='Name']:visible";
    private final String monoFirstInput = "input[placeholder='First']:visible";
    private final String monoSecondInput = "input[placeholder='Second']:visible";
    private final String monoThirdInput = "input[placeholder='Third']:visible";
    private final String multiLineTextArea = "tr:has(.pers-title:has-text('Personalization')) + tr textarea, div.personalization_name:has-text('Personalization') ~ textarea";
    private final String fontDropdownToggle = ".monogramcontent:visible .dropdownToggle, .imagedropdown:visible .dropdownToggle, "
            + "tr:has(.pers-title:has-text('Font')) + tr .dropdown-btn, fieldset:has(.personalization_name:has-text('Font')) .dropdown-btn, div.personalization_name:has-text('Font') ~ .custom-dropdown__wrapper .viewchoices-button, "
            + "tr:has(.pers-title:has-text('Choose Font')) + tr .dropdown-btn, fieldset:has(.personalization_name:has-text('Choose Font')) .dropdown-btn, div.personalization_name:has-text('Choose Font') ~ .custom-dropdown__wrapper .viewchoices-button";

    private final String fontOptionTemplate = ".monogramctdd:visible .imgdd[data-imgname='%s'], .imagedropdown:visible .imgdd[data-imgname='%s'], li[data-val='%s'], div.viewchoices-modal:visible ul li a:has(b:has-text('%s'))";

    private final String threadColorDropdown = "tr:has(.pers-title:has-text('Thread Color')) + tr .dropdown-btn, fieldset:has(.personalization_name:has-text('Thread Color')) .dropdown-btn, div.dropdown-btn";
    private final String colorDropdownByLabel = "tr:has(.pers-title:has-text('Color')) + tr .dropdown-btn, fieldset:has(.personalization_name:has-text('Color')) .dropdown-btn, div.dropdown-btn";
    private final String activeDropdownOptions = ".select-active li[data-val='%s'], .select-active li[data-option='%s']";

    private final String productImage = "#productImage";
    private final String continueButton = "input#ctl00_mainContent_addToCart_addToCartButton:visible, #addToCartLink[value='Continue']:visible";
    private final String contBtn = "input#continueShoppingLink, #cmdAddonGiftBox";

    private final String addToCartBtn = "input#addToCartLink, input[value='Add To Cart']:visible, input[value='Add to Cart']:visible, button:has-text('Add To Cart'):visible, button:has-text('Add to Cart'):visible, #addToCartLink[value='Add To Cart']:visible, #addToCartLink[value='Add to Cart']:visible, input#ctl00_mainContent_addToCart_addToCartButton";

    private final String noGiftBoxRadio = "label:has-text('No Gift Box')";
    private final String uploadPhotoBtn = "div.uploadthumb img, div.uploadthumbnail img";
    private final String customDropdownToggleTemplate = "tr:has(.pers-title:has-text('%s')) + tr .dropdown-btn, div.personalization_name:has-text('%s') ~ .custom-dropdown__wrapper .viewchoices-button";
    private final String customDropdownOptionTemplate = "tr:has(.pers-title:has-text('%s')) + tr li[data-val='%s'], div.viewchoices-modal:visible ul li a:has(b:has-text('%s'))";

    private Locator getLocator(String selector) {
        if (page.locator("#pmallmodaliframe").count() > 0) {
            return page.frameLocator("#pmallmodaliframe").locator(selector);
        } else if (page.locator("iframe#personalizationView").count() > 0) {
            return page.frameLocator("iframe#personalizationView").locator(selector);
        } else {
            return page.locator(selector);
        }
    }

    public void selectInitialAndFill(String initialText) {
        Locator radio = getLocator(initialRadio).first();
        radio.scrollIntoViewIfNeeded();
        radio.click();

        Locator input = getLocator(initialInput).first();
        input.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        input.clear();
        input.fill(initialText);
        input.press("Tab");
    }

    public void selectNameAndFill(String nameText) {
        Locator radio = getLocator(nameRadio).first();
        radio.scrollIntoViewIfNeeded();
        radio.click();

        Locator input = getLocator(nameInputBox).first();
        input.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        input.clear();
        input.fill(nameText);
        input.press("Tab");
    }

    public void selectCustomDropdownDesktop(String dropdownTitle, String optionName) {
        if (optionName == null || optionName.isEmpty()) {
            return;
        }

        System.out.println(">>> Selecting '" + optionName + "' from '" + dropdownTitle + "' dropdown...");

        String toggleSelector = String.format(customDropdownToggleTemplate, dropdownTitle, dropdownTitle);
        Locator toggleBtn = getLocator(toggleSelector).first();

        toggleBtn.scrollIntoViewIfNeeded();
        toggleBtn.click(new Locator.ClickOptions().setForce(true));

        page.waitForTimeout(500);

        String optionSelector = String.format(customDropdownOptionTemplate, dropdownTitle, optionName, optionName);
        Locator optionToSelect = getLocator(optionSelector).first();

        optionToSelect.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));

        optionToSelect.evaluate("node => { node.scrollIntoView(); node.click(); }");

        page.waitForTimeout(1500);
    }

    public void selectCustomDropdown(String dropdownTitle, String optionName) {
        if (optionName == null || optionName.isEmpty()) {
            return;
        }

        System.out.println(">>> Selecting '" + optionName + "' from '" + dropdownTitle + "' dropdown...");

        try {
            String titleXPath = String.format("//div[contains(@class, 'personalization_name') and normalize-space(text())='%s']", dropdownTitle);
            Locator selectNode = page.frameLocator("iframe#personalizationView").locator(titleXPath + "/following::select[1]");

            String dropdownId = selectNode.getAttribute("id");
            if (dropdownId == null) {
                System.out.println(">>> [ERROR] Could not find underlying select ID for title: " + dropdownTitle);
                return;
            }

            String exactModalId = dropdownId + "_modal";

            String optionSelector = String.format("div#%s ul li a:has(b:text-is('%s'))", exactModalId, optionName);
            Locator optionToSelect = page.frameLocator("iframe#personalizationView").locator(optionSelector).first();

            optionToSelect.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(5000));

            String hrefValue = optionToSelect.getAttribute("href");

            if (hrefValue != null && hrefValue.startsWith("javascript:")) {
                String jsCommand = hrefValue.replace("javascript:", "").trim();
                System.out.println(">>> Bypassing UI overlay. Executing direct function: " + jsCommand);

                page.frameLocator("iframe#personalizationView").locator(":root").evaluate("() => { " + jsCommand + " }");
            } else {
                System.out.println(">>> [ERROR] Failed to find javascript href for option: " + optionName);
            }

            page.waitForTimeout(500);

            Locator modalCloseBtn = page.frameLocator("iframe#personalizationView")
                    .locator(String.format("div#%s .viewchoices-close-btn", exactModalId)).first();

            try {
                modalCloseBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000));
                System.out.println(">>> Closing mobile selection modal via JS...");
                modalCloseBtn.evaluate("node => node.click()");
            } catch (Exception e) {
                System.out.println(">>> Modal close button not visible or already closed. Proceeding...");
            }

        } catch (Exception e) {
            System.out.println(">>> [ERROR] Failed during selectCustomDropdown for " + optionName + ": " + e.getMessage());
        }
    }

    public void fillMultiLinePersonalization(String text) {
        System.out.println(">>> Filling multi-line personalization text...");

        Locator textArea = getLocator(multiLineTextArea).first();
        textArea.scrollIntoViewIfNeeded();

        textArea.click();
        textArea.clear();

        textArea.evaluate("(el, msg) => { "
                + "el.focus(); "
                + "el.value = msg; "
                + "el.dispatchEvent(new KeyboardEvent('keydown', { bubbles: true, key: 'A' })); "
                + "el.dispatchEvent(new Event('input', { bubbles: true })); "
                + "el.dispatchEvent(new KeyboardEvent('keyup', { bubbles: true, key: 'A' })); "
                + "el.dispatchEvent(new Event('change', { bubbles: true })); "
                + "el.dispatchEvent(new Event('blur', { bubbles: true })); "
                + "}", text);

        textArea.press("Tab");

        page.waitForTimeout(1500);
    }

    public void enterPersonalizationMessage(String message) {
        System.out.println(">>> Waiting for Step 7 to load...");

        page.waitForTimeout(5000);

        System.out.println(">>> Entering personalization message...");

        Locator textArea = getLocator("div#personalizationForm textarea:visible").first();
        textArea.scrollIntoViewIfNeeded();

        textArea.click();
        textArea.clear();

        // --- UPDATED JS INJECTION ---
        textArea.evaluate("(el, msg) => { "
                + "el.focus(); "
                + "el.value = msg; "
                + "el.dispatchEvent(new KeyboardEvent('keydown', { bubbles: true, key: 'A' })); "
                + "el.dispatchEvent(new Event('input', { bubbles: true })); "
                + "el.dispatchEvent(new KeyboardEvent('keyup', { bubbles: true, key: 'A' })); "
                + "el.dispatchEvent(new Event('change', { bubbles: true })); "
                + "el.dispatchEvent(new Event('blur', { bubbles: true })); "
                + "}", message);

        textArea.press("Tab");

        page.waitForTimeout(500);
    }

    public void selectMonogramAndFill(String first, String second, String third) {
        Locator radio = getLocator(monogramRadio).first();
        radio.scrollIntoViewIfNeeded();
        radio.click();

        Locator firstBox = getLocator(monoFirstInput).first();
        firstBox.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        firstBox.clear();
        firstBox.fill(first);

        Locator secondBox = getLocator(monoSecondInput).first();
        secondBox.clear();
        secondBox.fill(second);

        Locator thirdBox = getLocator(monoThirdInput).first();
        thirdBox.clear();
        thirdBox.fill(third);
        thirdBox.press("Tab");
    }

    public void selectFont(String fontName) {
        if (fontName == null || fontName.isEmpty()) {
            return;
        }

        System.out.println(">>> Selecting '" + fontName + "' from font dropdown...");

        Locator dropdownBtn = getLocator(fontDropdownToggle).first();

        dropdownBtn.evaluate("node => { node.scrollIntoView(); node.click(); }");

        page.waitForTimeout(500);

        String optionSelector = String.format(fontOptionTemplate, fontName, fontName, fontName, fontName);
        Locator fontOption = getLocator(optionSelector).first();

        fontOption.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));

        fontOption.evaluate("node => { node.scrollIntoView(); node.click(); }");

        page.waitForTimeout(500);

        try {
            Locator doneBtn = getLocator("div.viewchoices-modal:visible .viewchoices-close-btn").first();
            if (doneBtn.count() > 0) {
                System.out.println(">>> Closing mobile font modal...");
                doneBtn.evaluate("node => { node.click(); }");
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
        }
    }

    private Locator getModalElement(String selector) {
        for (int i = 0; i < 30; i++) {
            if (page.locator(selector).count() > 0) {
                return page.locator(selector);
            }
            if (page.locator("#personalizationView").count() > 0
                    && page.frameLocator("#personalizationView").locator(selector).count() > 0) {
                return page.frameLocator("#personalizationView").locator(selector);
            }
            if (page.locator("#pmallmodaliframe").count() > 0
                    && page.frameLocator("#pmallmodaliframe").locator(selector).count() > 0) {
                return page.frameLocator("#pmallmodaliframe").locator(selector);
            }
            page.waitForTimeout(500);
        }
        return page.locator(selector);
    }

    public void uploadPhoto(String filePath) {
        getLocator(uploadPhotoBtn).click();
        Locator fileInput = getModalElement("#hFinderUploadFile");
        fileInput.setInputFiles(Paths.get(filePath));

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
        System.out.println(">>> Selecting Thread Color: " + color);
        Locator dropdown = getLocator(threadColorDropdown).first();
        dropdown.scrollIntoViewIfNeeded();

        try {
            dropdown.click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            System.out.println(">>> Native click blocked on dropdown toggle. Forcing JS click...");
            dropdown.evaluate("node => node.click()");
        }

        String optionLocator = String.format(activeDropdownOptions, color, color);
        Locator colorOption = getLocator(optionLocator).first();
        colorOption.scrollIntoViewIfNeeded();

        try {
            colorOption.click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            System.out.println(">>> Native click blocked on color option. Forcing JS click...");
            colorOption.evaluate("node => node.click()");
        }
    }

    public void selectColor(String color) {
        System.out.println(">>> Selecting Color: " + color);
        Locator dropdown = getLocator(colorDropdownByLabel).first();
        dropdown.scrollIntoViewIfNeeded();

        try {
            dropdown.click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            System.out.println(">>> Native click blocked on dropdown toggle. Forcing JS click...");
            dropdown.evaluate("node => node.click()");
        }

        String optionLocator = String.format(activeDropdownOptions, color, color);
        Locator colorOption = getLocator(optionLocator).first();
        colorOption.scrollIntoViewIfNeeded();

        try {
            colorOption.click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            System.out.println(">>> Native click blocked on color option. Forcing JS click...");
            colorOption.evaluate("node => node.click()");
        }
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
        page.waitForTimeout(5000);
        System.out.println(">>> Confirming personalization...");
        Locator confirmLabel = getLocator("label:has-text('The personalization I entered is correct')").first();

        try {
            confirmLabel.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(3000));

            confirmLabel.scrollIntoViewIfNeeded();
            confirmLabel.click();
            System.out.println(">>> Checkbox found and clicked.");

        } catch (com.microsoft.playwright.TimeoutError e) {
            System.out.println(">>> No confirmation checkbox rendered on this step. Bypassing safely.");
        }
    }

    public void clickContinue() {
        System.out.println(">>> Waiting for the Continue button to become enabled...");
        Locator btn = getLocator(continueButton).first();
        btn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertThat(btn).isEnabled();

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

    public void fillInitialAndContinue(String initialText, String fontName) {
        System.out.println(">>> Selecting 'Initial' radio button...");
        page.waitForTimeout(2000);

        Locator radio = getLocator(initialRadio).first();
        radio.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));

        radio.click();

        System.out.println(">>> Waiting for input box and typing initial: " + initialText);
        Locator input = getLocator(initialInput).first();
        input.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        input.clear();
        input.fill(initialText);

        input.press("Tab");

        System.out.println(">>> Opening custom dropdown and selecting font: " + fontName);
        selectFont(fontName);

        System.out.println(">>> Clicking Continue...");
        clickContinue();

        page.waitForTimeout(1000);
    }

    public void verifyPreviewImagePersonalization(String color, String font, String name) {
        List<String> activeParams = new ArrayList<>();
        if (color != null && !color.isEmpty()) {
            activeParams.add(color);
        }
        if (font != null && !font.isEmpty()) {
            activeParams.add(font);
        }
        if (name != null && !name.isEmpty()) {
            activeParams.add(name);
        }

        StringBuilder regexBuilder = new StringBuilder(".*");
        for (int i = 0; i < activeParams.size(); i++) {
            int valueIndex = i + 1;
            String safeValue = escapeForRegex(activeParams.get(i));
            regexBuilder.append("value").append(valueIndex).append("=").append(safeValue).append(".*");
        }

        String finalRegex = regexBuilder.toString();
        Pattern srcPattern = Pattern.compile(finalRegex, Pattern.CASE_INSENSITIVE);
        assertThat(getLocator(productImage)).hasAttribute("src", srcPattern, new LocatorAssertions.HasAttributeOptions().setTimeout(20000));
    }

    private String escapeForRegex(String input) {
        return input.replace(" ", "+").replace("+", "\\+");
    }

    public void verifyGiftSetPreviewImage(String monogram, String name) {
        String safeMonogram = monogram.replace(" ", "+").replace("+", "\\+");
        String safeName = name.replace(" ", "+").replace("+", "\\+");
        String regex = String.format(".*value1=%s.*value2=%s.*", safeMonogram, safeName);
        Pattern srcPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        assertThat(getLocator(productImage)).hasAttribute("src", srcPattern, new LocatorAssertions.HasAttributeOptions().setTimeout(20000));
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

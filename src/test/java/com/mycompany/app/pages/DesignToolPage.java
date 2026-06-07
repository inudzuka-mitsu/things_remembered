package com.mycompany.app.pages;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DesignToolPage extends BasePage {

    public DesignToolPage(Page page) {
        super(page);
    }

    private final String iframeSelector = "#app_iframe";
    private final String goStraightToEditorBtn = "text=Go Straight To Editor";
    private final String editorCanvas = "canvas"; 

    public void clickGoStraightToEditor() {
        page.frameLocator(iframeSelector)
            .locator(goStraightToEditorBtn)
            .click();
    }

    public void validateEditorLoaded() {
        assertThat(page.frameLocator(iframeSelector).locator(goStraightToEditorBtn)).isHidden();
        assertThat(page.frameLocator(iframeSelector).locator(editorCanvas)).isVisible();
    }
}

package com.mycompany.app.pages;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PhotoGiftsPage extends BasePage {

    private final String productImages = ".PhotoFirst_product img";

    private final String productCard = ".PhotoFirst_product";

    public PhotoGiftsPage(Page page) {
        super(page);
    }

    public void clickPillow() {
        System.out.println("Searching for Pillow product...");
        clickProductByName("Pillow");
    }

    public void clickProductByName(String partialName) {
        Locator product = page.locator(productCard)
                              .filter(new Locator.FilterOptions().setHasText(partialName))
                              .first(); 

        System.out.println("Clicking product containing text: " + partialName);
        assertThat(product).isVisible();
        product.scrollIntoViewIfNeeded();
        
        Locator productLink = product.locator("a").first();
        
        productLink.click(new Locator.ClickOptions().setForce(true));
    }

   public void uploadInspirationPhoto(String absoluteFilePath) {
        System.out.println("Uploading photo to Photo First uploader: " + absoluteFilePath);
        
        Locator uploadLabel = page.locator("label[for='photoFirstFileUploader']")
                                  .filter(new Locator.FilterOptions().setHasText("Upload"));
                                  
        FileChooser fileChooser = page.waitForFileChooser(() -> {
            uploadLabel.first().click(); 
        });

        fileChooser.setFiles(Paths.get(absoluteFilePath));
    }

    public void waitForProcessing() {
        System.out.println("Waiting 20 seconds for inspiration generation...");
        page.waitForTimeout(20000);
    }

    public void validateAllItemsHavePhoto() {
        System.out.println("Validating all items have the uploaded photo...");
        
        Locator images = page.locator(productImages);
        int count = images.count();
        
        System.out.println("Found " + count + " products.");
        assertTrue(count > 0, "No photo gift products were found in the grid.");

        for (int i = 0; i < count; i++) {
            Locator img = images.nth(i);
            assertThat(img).isVisible();

            String existPhotoName = img.getAttribute("data-existphotoname");
            if (existPhotoName == null || existPhotoName.isEmpty()) {
                throw new AssertionError("Product at index " + i + " does not have an uploaded photo attached.");
            }
            assertThat(img).hasAttribute("src", Pattern.compile(".*preview\\.iglx.*"));
        }
        
        System.out.println("Validation passed: All " + count + " items display the uploaded photo.");
    }
}
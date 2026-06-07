package com.mycompany.app.pages.modals_popups;

import java.nio.file.Paths;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mycompany.app.pages.BasePage;

public class PhotoUploadModal extends BasePage {

    private final String iframeSelector = "#pmallmodaliframe";
    private final String initialSelectBtn = ".select_photo_button"; 
    private final String realFileInput = "#hFinderUploadFile";
    private final String saveButton = "#saveCrop"; 
    private final String loadingSpinner = "#previewloadingImg";
    private final String photoNameDisplay = ".select-photo-name";

    private final String uploadVideoBtn = "#uploadVideoBtn";
    private final String addVideoToProductBtn = "#submitUploadVideo";


    public PhotoUploadModal(Page page) {
        super(page);
    }

    private FrameLocator getFrame() {
        return page.frameLocator(iframeSelector);
    }

    public void uploadPhoto(String absoluteFilePath) {
        System.out.println("Starting photo upload flow...");
        FrameLocator frame = getFrame();

        frame.locator(initialSelectBtn).click();
        System.out.println("Setting input file: " + absoluteFilePath);
        frame.locator(realFileInput).setInputFiles(Paths.get(absoluteFilePath));

        Locator saveBtn = frame.locator(saveButton);
        saveBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        
        System.out.println("Clicking Save...");
        saveBtn.click();
    }

    public boolean validatePhotoUploaded() {
        System.out.println("Validating upload success...");
        FrameLocator frame = getFrame();

        try {
            Locator spinner = frame.locator(loadingSpinner);
            if (spinner.isVisible()) {
                spinner.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.HIDDEN)
                    .setTimeout(15000)); 
            }

            Locator photoLabel = frame.locator(photoNameDisplay);
            photoLabel.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
        
            System.out.println("Upload Confirmed. File: " + photoLabel.textContent().trim());
            return true;
            
        } catch (Exception e) {
            System.err.println("Validation failed: Success elements did not appear.");
            return false;
        }
    }

    
    public void uploadVideo(String absoluteFilePath) {
    System.out.println("Starting video upload (native picker detected)...");
    FrameLocator frame = getFrame();

    if (!Paths.get(absoluteFilePath).toFile().exists()) {
        throw new RuntimeException("Video file not found: " + absoluteFilePath);
    }

    FileChooser chooser = page.waitForFileChooser(() -> {
        frame.locator(uploadVideoBtn).click();
    });

    System.out.println("Setting video file: " + absoluteFilePath);
    chooser.setFiles(Paths.get(absoluteFilePath));

    Locator videoPreview = frame.locator(".video-preview-block, [data-video-step='3']");
    videoPreview.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
            .setTimeout(60000));

    frame.locator(addVideoToProductBtn).click();
}

}
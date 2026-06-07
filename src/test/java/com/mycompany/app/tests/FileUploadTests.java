package com.mycompany.app.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.PhotoGiftsPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.PersonalizeItemModal;

// This test is configured for desktop app, iPhone 13 Pro Max, Samsung Galaxy A52

public class FileUploadTests extends TestBase {

    private StagingLoginPage stagingLoginPage;
    private PersonalizeItemModal personalizeModal;
    private CartPage cartPage;
    private HomePage homePage;
    private PhotoGiftsPage photoGiftsPage;

    @Test
    @DisplayName("Validate user can customize gifts with an uploaded photo")
    void uploadPhotoGift() {

        stagingLoginPage = new StagingLoginPage(page);
        personalizeModal = new PersonalizeItemModal(page);
        cartPage = new CartPage(page, isMobile());
        homePage = new HomePage(page, isMobile());
        photoGiftsPage = new PhotoGiftsPage(page);

        String PRODUCT_URL = getProperty("baseUrl") + "/Personalized-Photo-Gifts-s34.store";
        String photoPath = System.getProperty("user.dir") + "/src/test/resources/lake.jpg";

        String env = System.getProperty("env", "stg");
        
        if ("prod".equalsIgnoreCase(env)) {
            page.navigate(getProperty("baseUrl"));
        } else {
            page.navigate(getProperty("stagingBaseUrl"));
        }
        stagingLoginPage.closePopUp();

        page.navigate(PRODUCT_URL);
        photoGiftsPage.uploadInspirationPhoto(photoPath);
        photoGiftsPage.waitForProcessing();
        photoGiftsPage.validateAllItemsHavePhoto();
        photoGiftsPage.clickPillow();

        personalizeModal.clickAddToCart();
        personalizeModal.clickContinueBtn();

        homePage.clickViewCart();

        cartPage.validateProductInCart("Your Own Photo Personalized 14\" Throw Pillow");
    }

}

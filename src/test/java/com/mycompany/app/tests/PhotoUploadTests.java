package com.mycompany.app.tests;

import org.junit.jupiter.api.Test;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.PhotoEditorPage;
import com.mycompany.app.pages.ProductPage;
import com.mycompany.app.pages.login.StagingLoginPage;

// This test is configured for desktop app, iPhone 13 Pro Max, Samsung Galaxy A52

public class PhotoUploadTests extends TestBase {

    private StagingLoginPage stagingLoginPage;
    private ProductPage productPage;
    private PhotoEditorPage photoEditorPage;
    private HomePage homePage;
    private CartPage cartPage;

    @Test
    void photoEditor() {

        stagingLoginPage = new StagingLoginPage(page);
        productPage = new ProductPage(page, isMobile());
        photoEditorPage = new PhotoEditorPage(page, isMobile());
        homePage = new HomePage(page, isMobile());
        cartPage = new CartPage(page, isMobile());

        String PRODUCT_URL = getProperty("baseUrl") + "/Family-Photo-Personalized-Coffee-Mugs-p25561.prod?sdest=dept&sdestid=2115&storeid=34&categoryid=2115";
        String photoPath = System.getProperty("user.dir") + "/src/test/resources/lake.jpg";

         String env = System.getProperty("env", "stg");
        
        if ("prod".equalsIgnoreCase(env)) {
            page.navigate(getProperty("baseUrl"));
        } else {
            page.navigate(getProperty("stagingBaseUrl"));
        }
        stagingLoginPage.closePopUp();

        page.navigate(PRODUCT_URL);
        productPage.clickPersonalizeBtn();

        photoEditorPage.clickAddPhotoLater();
        if (!isMobile()) { photoEditorPage.clickDesignTab();}
        photoEditorPage.selectCategory("Anniversary");
        photoEditorPage.selectFirstLibraryMessage();
        photoEditorPage.clickPhotoTab();
        photoEditorPage.uploadPhoto(photoPath);
        photoEditorPage.clickPhotoTab();
        photoEditorPage.dragPhotoToSlot();
        photoEditorPage.verifyPhotoAssignedToSlot();
        photoEditorPage.clickAddToCart();
        photoEditorPage.clickContinue();

        homePage.clickViewCart();
        cartPage.validateProductInCart("Family Photo Personalized Coffee Mug");
    }

}

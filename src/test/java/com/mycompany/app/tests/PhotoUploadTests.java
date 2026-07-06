package com.mycompany.app.tests;

import org.junit.jupiter.api.Test;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.ProductPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.PersonalizeItemModal;

// Works for TR Desktop

public class PhotoUploadTests extends TestBase {

    private StagingLoginPage stagingLoginPage;
    private ProductPage productPage;
    private PersonalizeItemModal personalizeModal;
    private HomePage homePage;
    private CartPage cartPage;

    @Test
    void photoEditor() {

        stagingLoginPage = new StagingLoginPage(page);
        productPage = new ProductPage(page, isMobile());
        homePage = new HomePage(page, isMobile());
        cartPage = new CartPage(page, isMobile());
        personalizeModal = new PersonalizeItemModal(page);

        String PRODUCT_URL = getProperty("baseUrl") + "/Custom-Photo-Golf-Towel-i68313.item?productid=25113&sdest=Search&sdestid=181406962";
        String photoPath = System.getProperty("user.dir") + "/src/test/resources/lake.jpg";

        page.navigate(PRODUCT_URL);
         stagingLoginPage.closePopUp();
        productPage.clickPersonalizeBtn();
        personalizeModal.uploadPhoto(photoPath);
        personalizeModal.checkPersonalizationCorrect();
        personalizeModal.clickAddToCart();

        homePage.clickViewCart();
        cartPage.validateProductInCart("Personalized Photo Golf Towel");
    }

}

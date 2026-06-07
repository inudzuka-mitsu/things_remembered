package com.mycompany.app.tests;

import org.junit.jupiter.api.Test;

import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.ProductCatalogPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.ProductModal;

// Works for TR Desktop

public class ProductSearchTests extends TestBase {

     @Test
     void searchForProduct() {

        StagingLoginPage lp = new StagingLoginPage(page);
        HomePage hp = new HomePage(page, isMobile());
        ProductModal modal = new ProductModal(page);
        ProductCatalogPage pc = new ProductCatalogPage(page, isMobile());

        String productName = "Socks";

        String env = System.getProperty("env", "stg");
        
        if ("prod".equalsIgnoreCase(env)) {
            page.navigate(getProperty("baseUrl"));
        } else {
            page.navigate(getProperty("stagingBaseUrl"));
        }
        lp.closePopUp();
        hp.typeProduct(productName);

        modal.validateAllProductsContainProductName(productName);
        modal.validateAllProductSuggestionsContainProductName(productName);

        hp.searchProduct();
        if (!isMobile()) {
         pc.validateCurrentSelection(productName);
        }
        pc.validateItemSearchResults(productName);
     }
 }
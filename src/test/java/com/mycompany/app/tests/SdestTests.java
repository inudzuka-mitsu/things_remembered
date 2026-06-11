package com.mycompany.app.tests;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.ForHerPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.ProductCatalogPage;
import com.mycompany.app.pages.login.StagingLoginPage;

// These tests are configured for TR desktop app (stg and prod), iPhone 13 Pro Max, Samsung Galaxy A52 

public class SdestTests extends TestBase {

    private StagingLoginPage stagingLoginPage;
    private HomePage homePage;
    private ForHerPage forHerPage;
    private ProductCatalogPage pcp;

    @BeforeEach
    @SuppressWarnings("unused")
    void setupPages() {
        stagingLoginPage = new StagingLoginPage(page);
        homePage = new HomePage(page, isMobile());
        forHerPage = new ForHerPage(page, isMobile());
        pcp = new ProductCatalogPage(page, isMobile());

        page.navigate(getProperty("baseUrl"));

        page.navigate(getProperty("baseUrl"), new com.microsoft.playwright.Page.NavigateOptions()
            .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        
        stagingLoginPage.closePopUp();
    }

    @Test
    @DisplayName("Validate URL parameters when navigating via 'For Her' category")
    void verifyForHerProductUrlParameters() {
        homePage.clickForHer();
        forHerPage.clickCategory("Vases");
        forHerPage.clickFirstProduct();

        assertThat(page).hasURL(Pattern.compile(".*sdest=.*&sdestid=."));
    }

    @Test
    @DisplayName("Validate URL parameters when navigating via Search Results")
    void verifySearchResultsUrlParameters() {
        String searchQuery = "ornaments";

        homePage.typeProduct(searchQuery);
        homePage.searchProduct();
    
        page.waitForTimeout(10000);
        pcp.clickFirstProduct();

        String expectedUrlRegex = isMobile() 
                ? ".*productid=\\d+&sdest=.*&sdestid=.*"   
                : ".*prod\\?sdest=.*&sdestid=.*";         

    
        assertThat(page).hasURL(Pattern.compile(expectedUrlRegex));
    }
}

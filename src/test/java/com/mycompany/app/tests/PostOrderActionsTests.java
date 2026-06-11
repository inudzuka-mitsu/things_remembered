package com.mycompany.app.tests;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.mycompany.app.base.TestBase;
import com.mycompany.app.pages.AccountPage;
import com.mycompany.app.pages.CartPage;
import com.mycompany.app.pages.HomePage;
import com.mycompany.app.pages.OrderHistoryPage;
import com.mycompany.app.pages.login.SignInPage;
import com.mycompany.app.pages.login.StagingLoginPage;
import com.mycompany.app.pages.modals_popups.Header;

// These tests are configured for TR desktop app (stg and prod), iPhone 13 Pro Max, Samsung Galaxy A52

public class PostOrderActionsTests extends TestBase {

    private SignInPage signInPage;
    private StagingLoginPage stagingLoginPage;
    private Header header;
    private AccountPage accountPage;
    private OrderHistoryPage orderHistoryPage;
    private HomePage homePage;
    private CartPage cartPage;

    @BeforeEach
    @SuppressWarnings("unused")
    void setupAndNavigateToOrderDetails() {

        signInPage = new SignInPage(page, isMobile());
        stagingLoginPage = new StagingLoginPage(page);
        header = new Header(page, isMobile());
        accountPage = new AccountPage(page, isMobile());
        orderHistoryPage = new OrderHistoryPage(page, isMobile());
        homePage = new HomePage(page, isMobile());
        cartPage = new CartPage(page, isMobile());

        String testEmail = getProperty("test_email_2");
        String testPassword = getProperty("test_password_2");

        page.navigate(getProperty("baseUrl"));
        stagingLoginPage.closePopUp();

        header.clickSignIn();
        signInPage.signIn(testEmail, testPassword);

        accountPage.clickOrderHistory();
        orderHistoryPage.clickViewDetailsOfFirstOrder();
    }

    @Test
    @DisplayName("Verify 'Write a Review' button redirects to review page")
    void validateWriteReview() {

        orderHistoryPage.clickWriteReview();

        assertThat(page).hasURL(Pattern.compile(".*ProductReviewWrite\\.aspx.*"));
    }

    @Test
    @DisplayName("Verify 'Report Issue' button redirects to remake page")
    void validateReportIssue() {

        orderHistoryPage.clickReportIssue();
        
        assertThat(page).hasURL(Pattern.compile(".*OrderRemake\\.aspx.*"));
    }

    @Test
    @DisplayName("Verify 'Re-order Item' button redirects to re-order page")
    void reorderItem() {

        String name = orderHistoryPage.getItemName();

        orderHistoryPage.clickReorder();

        homePage.clickViewCart();

        cartPage.validateProductInCart(name);
    }
}

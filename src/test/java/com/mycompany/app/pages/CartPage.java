package com.mycompany.app.pages;

import java.util.regex.Pattern;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CartPage extends BasePage {

    private final boolean isMobile;

    public CartPage(Page page, boolean isMobile) {
        super(page);
        this.isMobile = isMobile;
    }

    // --- DESKTOP AND MOBILE LOCATORS ---
    private final String quantityInput = "input.inp__qty-title";
    private final String updateButton = "input.btn__qty-update, .updateQtyBT";
    private final String itemPriceText = "li:has-text('Price:') b, .li__item-price b, li:has(label:has-text('Price:')) span.bold, li:has(label:has-text('Price:'))";
    private final String boxPriceText = ".li__gift-box span.reg";
    private final String totalPriceText = ".li__item-total .sp__amt-total, .li__itm-total .sp__amt-total";
    private final String proceedToCheckoutBtn = "a.begin-checkout:has-text('Proceed To Checkout'), button.begin-checkout:has-text('Proceed To Checkout')";
    private final String saveForLaterLink = "ul.list__prev-edit a:has-text('Save for later')";
    private final String editLink = "ul.list__prev-edit a:has-text('Edit'), input.pereditBtn[value='Edit']";
    private final String moveToCartLink = "#ctl00_mainContent_savedItemsList .block__saveto-cart .moveSavedItem";
    private final String savedItemsContainer = "#ctl00_mainContent_savedItemsList, .block__saved-item";
    private final String emptyCartContainer = "#ctl00_mainContent_cartEmpty";
    private final String savedNotificationText = "#ctl00_mainContent_orderItemsSavedContent2019_notificationsList li";
    private final String showMoreSavedItemsLink = "#ctl00_mainContent_orderItemsSavedContent2019_moreSavedItemsLink";

    public double getItemPrice() {
        return parsePrice(page.locator(itemPriceText).first().innerText());
    }

    public double getBoxPrice() {
        if (page.locator(boxPriceText).isVisible()) {
            return parsePrice(page.locator(boxPriceText).first().innerText());
        }
        return 0.00;
    }

    public void clickProceedToCheckout() {
        Locator btn = page.locator(proceedToCheckoutBtn).first();
        btn.scrollIntoViewIfNeeded();
        btn.click(new Locator.ClickOptions().setForce(true));
    }

    public void expandSavedItemsListIfNeeded() {
        Locator showMoreBtn = page.locator(showMoreSavedItemsLink).first();
        try {
            showMoreBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
            System.out.println(">>> 'Show More' link detected! Expanding the saved items list...");

            showMoreBtn.scrollIntoViewIfNeeded();
            showMoreBtn.click(new Locator.ClickOptions().setForce(true));

            page.waitForTimeout(2000);

        } catch (com.microsoft.playwright.TimeoutError e) {
            System.out.println(">>> No 'Show More' link present (3 or fewer items). Proceeding...");
        }
    }

    public void updateQuantityAndVerifyTotal(int newQuantity) {
        double itemPrice = getItemPrice();
        double boxPrice = getBoxPrice();

        double unitCost = itemPrice + boxPrice;

        double expectedTotal = unitCost * newQuantity;
        String expectedTotalStr = String.format("%.2f", expectedTotal);

        System.out.println("--- Cart Calculation Check ---");
        System.out.println("New Quantity: " + newQuantity);
        System.out.println("Expected Total: $" + expectedTotalStr);

        page.locator(quantityInput).first().fill(String.valueOf(newQuantity));
        page.locator(updateButton).first().click(new Locator.ClickOptions().setForce(true));

        String safeTotal = expectedTotalStr.replace(".", "\\.");
        Pattern pricePattern = Pattern.compile(".*\\$" + safeTotal + ".*");

        assertThat(page.locator(totalPriceText).first()).hasText(pricePattern);
    }

    private double parsePrice(String priceText) {
        if (priceText == null || priceText.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
    }

    public void clickSaveForLater() {
        Locator saveLink = page.locator(saveForLaterLink).first();

        try {
            saveLink.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(5000));
            saveLink.scrollIntoViewIfNeeded();
            try {
                saveLink.click(new Locator.ClickOptions().setForce(true).setTimeout(2000));
            } catch (Exception clickError) {
                System.out.println("Standard click intercepted by overlay. Attempting JS click...");
                saveLink.dispatchEvent("click");
            }

        } catch (Exception e) {
            System.out.println("Failed to click 'Save for later'. Error: " + e.getMessage());
        }
    }

    public void clickEdit() {
        Locator editBtn = page.locator(editLink).first();

        try {
            editBtn.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(5000));
            editBtn.scrollIntoViewIfNeeded();
            try {
                editBtn.click(new Locator.ClickOptions().setForce(true).setTimeout(2000));
            } catch (Exception clickError) {
                System.out.println("Standard click intercepted by overlay. Attempting JS click...");
                editBtn.dispatchEvent("click");
            }

        } catch (Exception e) {
            System.out.println("Failed to click 'Edit' link. Error: " + e.getMessage());
        }
    }

    public void clickMoveToCart() {
        Locator moveLink = page.locator(moveToCartLink).first();

        try {
            moveLink.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5000));
            moveLink.scrollIntoViewIfNeeded();
            try {
                moveLink.click(new Locator.ClickOptions().setForce(true).setTimeout(2000));
            } catch (Exception clickError) {
                System.out.println("Standard click on 'Move To Cart' intercepted. Attempting JS click...");
                moveLink.dispatchEvent("click");
            }
        } catch (Exception e) {
            System.out.println("Failed to click 'Move To Cart'. Error: " + e.getMessage());
        }
    }

    public void clickMoveToCartSpecProd(String productName) {
        System.out.println(">>> Moving '" + productName + "' back to cart...");

        Locator moveLink;

        if (isMobile) {
            Locator productBlock = page.locator(".block__saved-item")
                    .filter(new Locator.FilterOptions().setHasText(productName))
                    .first();
            moveLink = productBlock.locator(".moveSavedItem").first();
        } else {
            Locator productBlock = page.locator(".block__saveto-cart")
                    .filter(new Locator.FilterOptions().setHasText(productName))
                    .first();
            moveLink = productBlock.locator(".moveSavedItem").first();
        }

        moveLink.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        moveLink.scrollIntoViewIfNeeded();

        try {
            moveLink.click(new Locator.ClickOptions().setForce(true).setTimeout(2000));
        } catch (Exception clickError) {
            System.out.println("Standard click on 'Move To Cart' intercepted. Attempting JS click...");
            moveLink.dispatchEvent("click");
        }
    }

    public void validateProductInSavedForLater(String productName) {
        Locator container = page.locator(savedItemsContainer);
        container.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        Locator savedProductTitle = container.locator(".block__saveto-cart h3 a")
                .filter(new Locator.FilterOptions().setHasText(productName))
                .first();

        Locator savedProductTitle2 = container.locator("a")
                .filter(new Locator.FilterOptions().setHasText(productName))
                .first();

        if (!isMobile) {
            assertThat(savedProductTitle).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        } else {
            assertThat(savedProductTitle2).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        }
    }

    public void validateEmptyCartAndSavedMessage() {
        if (isMobile) {
            System.out.println(">>> Validating Mobile empty cart message...");
            Locator mobileEmptyContainer = page.locator(".section-info-yellow").first();
            assertThat(mobileEmptyContainer).isVisible();

            assertThat(mobileEmptyContainer).containsText("Currently, your shopping cart is empty!");
            assertThat(mobileEmptyContainer).containsText("Shop by stores or visit our homepage.");
        } else {
            System.out.println(">>> Validating Desktop empty cart message...");
            Locator emptyContainer = page.locator(emptyCartContainer);
            assertThat(emptyContainer).isVisible();

            assertThat(emptyContainer).containsText("Currently, there are no items in your shopping cart!");
            assertThat(emptyContainer).containsText("Return to our Home Page to find the perfect, personalized gift!");
            assertThat(emptyContainer.locator("a:has-text('Continue Shopping')")).isVisible();
            assertThat(emptyContainer.locator("a:has-text('Homepage')")).isVisible();

            Locator notification = page.locator(savedNotificationText);
            assertThat(notification).isVisible();
            assertThat(notification).containsText("Requested item has been put into saved items list");
        }
    }

    public void validateProductInCart(String productName) {
        String locator = isMobile ? "div.block__cart-item h3" : ".block__shopping-cart h3 a";
        Locator cartItemTitle = page.locator(locator)
                .filter(new Locator.FilterOptions().setHasText(productName)).first();
        assertThat(cartItemTitle).isVisible(
                new LocatorAssertions.IsVisibleOptions().setTimeout(30000)
        );
    }

    public void validateProductAddedToCart(String productName) {
        System.out.println(">>> Validating '" + productName + "' was successfully added back to cart...");

        String safeName = productName;
        if (productName.contains("&")) {
            safeName = productName.split("&")[0].trim();
        }

        String selector = isMobile
                ? "h2.primaryFont14.bold"
                : ".block.block__added-to-cart #ctl00_mainContent_itemAddedToCart_txtAddToCartProduct";

        Locator cartItemTitle = page.locator(selector)
                .filter(new Locator.FilterOptions().setHasText(safeName))
                .first();

        try {
            assertThat(cartItemTitle).isVisible(
                    new LocatorAssertions.IsVisibleOptions().setTimeout(30000)
            );
            System.out.println(">>> Successfully validated item is in the cart.");
        } catch (AssertionError e) {
            System.out.println(">>> [ERROR] Item not found. Dumping all h2 text on page for debugging:");
            for (String text : page.locator("h2").allInnerTexts()) {
                System.out.println("Found H2: " + text);
            }
            throw e;
        }
    }

    public void clickSaveForLaterSpecProd(String productName) {
        System.out.println(">>> Clicking 'Save for later' specifically for: " + productName);
        String safeName = productName;
        if (productName.contains("&")) {
            safeName = productName.split("&")[0].trim();
        }

        Locator itemContainer;
        if (isMobile) {
            itemContainer = page.locator("ul").filter(
                    new Locator.FilterOptions().setHas(
                            page.locator(String.format("input[data-itemname*='%s']", safeName))
                    )
            ).first();
        } else {

            itemContainer = page.locator(".block__shopping-cart")
                    .filter(new Locator.FilterOptions().setHasText(safeName))
                    .first();
        }

        itemContainer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));

        Locator saveBtn = itemContainer.locator("a", new Locator.LocatorOptions().setHasText("Save for later")).first();
        saveBtn.scrollIntoViewIfNeeded();

        page.onceDialog(dialog -> {
            System.out.println(">>> Auto-accepting native confirmation dialog: " + dialog.message());
            dialog.accept();
        });

        try {
            saveBtn.click(new Locator.ClickOptions().setForce(true));
        } catch (Exception e) {
            System.out.println(">>> Native click blocked by viewport geometry, forcing JS execution...");
            saveBtn.evaluate("node => node.click()");
        }

        page.waitForTimeout(3000);
    }

    public void validateProductInSavedForLaterSpecProd(String productName) {
        System.out.println(">>> Validating '" + productName + "' is in the Saved for Later list...");

        Locator container;

        if (isMobile) {
            container = page.locator(".order-change-body, .block__saved-item").first();
        } else {
            container = page.locator(savedItemsContainer).first();
        }

        container.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        Locator savedProductTitle = container.locator("h3, a")
                .filter(new Locator.FilterOptions().setHasText(productName))
                .first();

        assertThat(savedProductTitle).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
    }
}

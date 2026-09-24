package ui;

import com.codeborne.selenide.logevents.SelenideLogger;
import config.UiConfig;
import io.qameta.allure.Feature;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutCompletePage;
import pages.CheckoutOverviewPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static io.qameta.allure.Allure.step;

@Feature("SauceDemo shopping")
public class SauceDemoSmokeTest {

    private static final String PRODUCT_NAME = "Sauce Labs Backpack";
    // Public password provided by SauceDemo for its demo users.
    private static final String DEMO_PASSWORD = "secret_sauce";

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        UiConfig.configure();
    }

    @Test(groups = {"ui", "smoke"}, description = "Standard user can log in and see the product catalog")
    public void standardUserCanLogIn() {
        logInAsStandardUser();
    }

    @Test(groups = {"ui", "smoke"}, description = "Locked-out user sees the correct login error")
    public void lockedOutUserCannotLogIn() {
        LoginPage loginPage = openLoginPage();

        step("Attempt to log in as the locked-out user",
                () -> loginPage.submitCredentials("locked_out_user", DEMO_PASSWORD));

        step("Verify the locked-out error and that the login form remains visible",
                () -> loginPage.shouldHaveError("Epic sadface: Sorry, this user has been locked out."));
    }

    @Test(groups = {"ui", "smoke"}, description = "Standard user can add a product and see it in the cart")
    public void standardUserCanAddProductToCart() {
        InventoryPage inventoryPage = logInAsStandardUser();
        CartPage cartPage = addProductAndOpenCart(inventoryPage);

        step("Verify the cart contains one " + PRODUCT_NAME,
                () -> cartPage.shouldContainSingleProduct(PRODUCT_NAME));
    }

    @Test(groups = {"ui", "smoke"}, description = "Standard user can complete checkout for one product")
    public void standardUserCanCompleteCheckout() {
        InventoryPage inventoryPage = logInAsStandardUser();
        CartPage cartPage = addProductAndOpenCart(inventoryPage);

        step("Verify the cart contains one " + PRODUCT_NAME,
                () -> cartPage.shouldContainSingleProduct(PRODUCT_NAME));

        CheckoutPage checkoutPage = step("Proceed to checkout", cartPage::proceedToCheckout);
        step("Enter customer information: Nikita Test, postal code 6000",
                () -> checkoutPage.enterCustomerInformation("Nikita", "Test", "6000"));

        CheckoutOverviewPage overviewPage = step("Continue to the order overview", checkoutPage::continueToOverview);
        step("Verify the order contains one " + PRODUCT_NAME,
                () -> overviewPage.shouldContainSingleProduct(PRODUCT_NAME));

        CheckoutCompletePage completePage = step("Finish checkout", overviewPage::finishCheckout);
        step("Verify the order was completed successfully", completePage::shouldShowSuccessfulOrder);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            closeWebDriver();
        } finally {
            SelenideLogger.removeListener("AllureSelenide");
        }
    }

    private LoginPage openLoginPage() {
        return step("Open SauceDemo", () -> new LoginPage().open());
    }

    private InventoryPage logInAsStandardUser() {
        LoginPage loginPage = openLoginPage();
        InventoryPage inventoryPage = step("Log in as the standard user",
                () -> loginPage.loginAs("standard_user", DEMO_PASSWORD));
        step("Verify the inventory page and product catalog are displayed", inventoryPage::shouldBeLoaded);
        return inventoryPage;
    }

    private CartPage addProductAndOpenCart(InventoryPage inventoryPage) {
        step("Add " + PRODUCT_NAME + " to the cart", () -> inventoryPage.addProductToCart(PRODUCT_NAME));
        step("Verify the cart badge shows 1", () -> inventoryPage.shouldHaveCartBadge(1));
        return step("Open the cart", inventoryPage::openCart);
    }
}

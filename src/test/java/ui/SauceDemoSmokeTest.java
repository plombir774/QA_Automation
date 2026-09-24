package ui;

import com.codeborne.selenide.logevents.SelenideLogger;
import config.UiConfig;
import io.qameta.allure.Feature;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static io.qameta.allure.Allure.step;

@Feature("SauceDemo login")
public class SauceDemoSmokeTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        UiConfig.configure();
    }

    @Test(groups = {"ui", "smoke"}, description = "Standard user can log in and see the product catalog")
    public void standardUserCanLogIn() {
        LoginPage loginPage = step("Open SauceDemo", () -> new LoginPage().open());

        // Public credentials provided by SauceDemo for testing.
        InventoryPage inventoryPage = step("Log in as the standard user",
                () -> loginPage.loginAs("standard_user", "secret_sauce"));

        step("Verify the product catalog is displayed", inventoryPage::shouldBeLoaded);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            closeWebDriver();
        } finally {
            SelenideLogger.removeListener("AllureSelenide");
        }
    }
}

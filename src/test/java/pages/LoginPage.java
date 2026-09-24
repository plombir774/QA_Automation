package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameInput = $("[data-test='username']");
    private final SelenideElement passwordInput = $("[data-test='password']");
    private final SelenideElement loginButton = $("[data-test='login-button']");

    public LoginPage open() {
        Selenide.open("/");
        loginButton.shouldBe(visible);
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        submitCredentials(username, password);
        return new InventoryPage();
    }

    public void submitCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        loginButton.click();
    }

    public void shouldHaveError(String message) {
        $("[data-test='error']").shouldBe(visible).shouldHave(exactText(message));
        loginButton.shouldBe(visible);
    }
}

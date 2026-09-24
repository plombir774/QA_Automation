package pages;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutCompletePage {

    public void shouldShowSuccessfulOrder() {
        $("[data-test='title']").shouldBe(visible).shouldHave(exactText("Checkout: Complete!"));
        $("[data-test='complete-header']").shouldBe(visible)
                .shouldHave(exactText("Thank you for your order!"));
    }
}

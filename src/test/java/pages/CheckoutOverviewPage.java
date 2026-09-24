package pages;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CheckoutOverviewPage {

    public CheckoutOverviewPage shouldBeLoaded() {
        $("[data-test='title']").shouldBe(visible).shouldHave(exactText("Checkout: Overview"));
        return this;
    }

    public void shouldContainSingleProduct(String productName) {
        $$("[data-test='inventory-item-name']").shouldHave(exactTexts(productName));
        $("[data-test='item-quantity']").shouldHave(exactText("1"));
    }

    public CheckoutCompletePage finishCheckout() {
        $("[data-test='finish']").click();
        return new CheckoutCompletePage();
    }
}

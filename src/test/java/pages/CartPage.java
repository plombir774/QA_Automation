package pages;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {

    public CartPage shouldBeLoaded() {
        $("[data-test='title']").shouldBe(visible).shouldHave(exactText("Your Cart"));
        return this;
    }

    public void shouldContainSingleProduct(String productName) {
        $$("[data-test='inventory-item-name']").shouldHave(exactTexts(productName));
        $("[data-test='item-quantity']").shouldHave(exactText("1"));
    }

    public CheckoutPage proceedToCheckout() {
        $("[data-test='checkout']").click();
        return new CheckoutPage().shouldBeLoaded();
    }
}

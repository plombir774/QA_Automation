package pages;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class InventoryPage {

    public void shouldBeLoaded() {
        webdriver().shouldHave(urlContaining("/inventory.html"));
        $("[data-test='title']").shouldBe(visible).shouldHave(exactText("Products"));
        $("[data-test='inventory-container']").shouldBe(visible);
        $$("[data-test='inventory-item']").shouldHave(sizeGreaterThan(0));
    }

    public void addProductToCart(String productName) {
        $$("[data-test='inventory-item']").findBy(text(productName))
                .$("[data-test^='add-to-cart-']").click();
    }

    public void shouldHaveCartBadge(int count) {
        $("[data-test='shopping-cart-badge']").shouldBe(visible)
                .shouldHave(exactText(String.valueOf(count)));
    }

    public CartPage openCart() {
        $("[data-test='shopping-cart-link']").click();
        return new CartPage().shouldBeLoaded();
    }
}

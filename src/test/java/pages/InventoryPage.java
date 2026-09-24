package pages;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class InventoryPage {

    public void shouldBeLoaded() {
        $("[data-test='title']").shouldBe(visible).shouldHave(exactText("Products"));
        $("[data-test='inventory-container']").shouldBe(visible);
        $$("[data-test='inventory-item']").shouldHave(sizeGreaterThan(0));
    }
}

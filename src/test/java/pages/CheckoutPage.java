package pages;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutPage {

    public CheckoutPage shouldBeLoaded() {
        $("[data-test='title']").shouldBe(visible).shouldHave(exactText("Checkout: Your Information"));
        return this;
    }

    public void enterCustomerInformation(String firstName, String lastName, String postalCode) {
        $("[data-test='firstName']").setValue(firstName);
        $("[data-test='lastName']").setValue(lastName);
        $("[data-test='postalCode']").setValue(postalCode);
    }

    public CheckoutOverviewPage continueToOverview() {
        $("[data-test='continue']").click();
        return new CheckoutOverviewPage().shouldBeLoaded();
    }
}

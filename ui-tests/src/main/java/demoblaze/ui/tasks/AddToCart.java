package demoblaze.ui.tasks;

import demoblaze.ui.interactions.AcceptTheAlert;
import demoblaze.ui.ui.StorePage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.List;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.containsText;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public final class AddToCart {

    private AddToCart() {
    }

    public static Performable theProduct(String product) {
        return Task.where("{0} adds '" + product + "' to the cart",
                OpenTheStore.homePage(),
                Click.on(StorePage.PRODUCT_LINK.of(product)),
                WaitUntil.the(StorePage.PRODUCT_NAME, containsText(product)),
                WaitUntil.the(StorePage.ADD_TO_CART, isClickable()),
                Click.on(StorePage.ADD_TO_CART),
                AcceptTheAlert.shownByTheSite()
        );
    }

    public static Performable theProducts(List<String> products) {
        return Task.where("{0} adds " + products + " to the cart",
                products.stream().map(AddToCart::theProduct).toArray(Performable[]::new));
    }
}

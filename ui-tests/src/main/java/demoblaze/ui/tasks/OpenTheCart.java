package demoblaze.ui.tasks;

import demoblaze.ui.interactions.WaitForTheCart;
import demoblaze.ui.ui.CartPage;
import demoblaze.ui.ui.StorePage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.List;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class OpenTheCart {

    private OpenTheCart() {
    }

    /**
     * Opens the cart and waits until it is fully rendered: the cart loads each line with its own request,
     * so the total is only final once every expected row is on the page.
     */
    public static Performable containing(List<String> products) {
        return Task.where("{0} opens the cart containing " + products,
                Click.on(StorePage.CART_LINK),
                WaitUntil.the(CartPage.PLACE_ORDER, isVisible()),
                WaitForTheCart.toListExactly(products)
        );
    }
}

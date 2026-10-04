package demoblaze.ui.tasks;

import demoblaze.ui.ui.CartPage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.List;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotPresent;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class RemoveFromCart {

    private RemoveFromCart() {
    }

    /**
     * Deletes one product; the page reloads, so the task then waits for the remaining rows.
     */
    public static Performable theProduct(String product, List<String> remainingProducts) {
        Performable[] waitForRemaining = remainingProducts.stream()
                .map(remaining -> WaitUntil.the(CartPage.ROW_OF_PRODUCT.of(remaining), isVisible()))
                .toArray(Performable[]::new);
        return Task.where("{0} removes '" + product + "' from the cart",
                Click.on(CartPage.DELETE_LINK_OF_PRODUCT.of(product)),
                WaitUntil.the(CartPage.ROW_OF_PRODUCT.of(product), isNotPresent()),
                Task.where("{0} waits for the remaining products " + remainingProducts, waitForRemaining)
        );
    }
}

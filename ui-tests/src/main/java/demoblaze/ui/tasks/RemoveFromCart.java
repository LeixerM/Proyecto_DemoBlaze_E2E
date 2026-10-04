package demoblaze.ui.tasks;

import demoblaze.ui.interactions.WaitForAPageReload;
import demoblaze.ui.interactions.WaitForTheCart;
import demoblaze.ui.ui.CartPage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;

import java.util.List;

public final class RemoveFromCart {

    private RemoveFromCart() {
    }

    /**
     * Deletes one product. The site answers "Delete" with {@code location.reload()} and then rebuilds the
     * table row by row, so the task waits for the reload and then for the rebuilt cart to list exactly
     * the remaining products with a matching total, instead of polling elements of the old page.
     */
    public static Performable theProduct(String product, List<String> remainingProducts) {
        return Task.where("{0} removes '" + product + "' from the cart",
                WaitForAPageReload.markTheCurrentPage(),
                Click.on(CartPage.DELETE_LINK_OF_PRODUCT.of(product)),
                WaitForAPageReload.afterTheMarkedPage(),
                WaitForTheCart.toListExactly(remainingProducts)
        );
    }
}

package demoblaze.ui.tasks;

import demoblaze.ui.ui.CartPage;
import demoblaze.ui.ui.StorePage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.ArrayList;
import java.util.List;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class OpenTheCart {

    private OpenTheCart() {
    }

    /**
     * Opens the cart and waits until every expected row is rendered: the cart loads each line
     * with its own request, so the total is only final once all rows are on the page.
     */
    public static Performable containing(List<String> products) {
        List<Performable> steps = new ArrayList<>();
        steps.add(Click.on(StorePage.CART_LINK));
        steps.add(WaitUntil.the(CartPage.PLACE_ORDER, isVisible()));
        products.forEach(product -> steps.add(WaitUntil.the(CartPage.ROW_OF_PRODUCT.of(product), isVisible())));
        return Task.where("{0} opens the cart containing " + products, steps.toArray(Performable[]::new));
    }
}

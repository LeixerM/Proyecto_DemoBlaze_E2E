package demoblaze.ui.tasks;

import demoblaze.ui.models.Customer;
import demoblaze.ui.ui.CartPage;
import demoblaze.ui.ui.PlaceOrderModal;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class PlaceTheOrder {

    private PlaceTheOrder() {
    }

    public static Performable withBillingDetailsOf(Customer customer) {
        return Task.where("{0} places the order as " + customer.name(),
                Click.on(CartPage.PLACE_ORDER),
                WaitUntil.the(PlaceOrderModal.NAME, isVisible()),
                Enter.theValue(customer.name()).into(PlaceOrderModal.NAME),
                Enter.theValue(customer.country()).into(PlaceOrderModal.COUNTRY),
                Enter.theValue(customer.city()).into(PlaceOrderModal.CITY),
                Enter.theValue(customer.creditCard()).into(PlaceOrderModal.CREDIT_CARD),
                Enter.theValue(customer.month()).into(PlaceOrderModal.MONTH),
                Enter.theValue(customer.year()).into(PlaceOrderModal.YEAR),
                Click.on(PlaceOrderModal.PURCHASE)
        );
    }

    /** Submits the order form without filling it in. */
    public static Performable withoutBillingDetails() {
        return Task.where("{0} tries to purchase without billing details",
                Click.on(CartPage.PLACE_ORDER),
                WaitUntil.the(PlaceOrderModal.PURCHASE, isVisible()),
                Click.on(PlaceOrderModal.PURCHASE)
        );
    }
}

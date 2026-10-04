package demoblaze.ui.interactions;

import demoblaze.ui.models.CartSnapshot;
import demoblaze.ui.questions.TheCart;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.waits.Wait;
import net.serenitybdd.screenplay.waits.WaitWithTimeout;
import net.thucydides.model.ThucydidesSystemProperty;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

/**
 * Waits, for no longer than {@code webdriver.wait.for.timeout}, until the cart page is fully rendered:
 * it lists exactly the expected products and its total equals the sum of their prices.
 */
public final class WaitForTheCart {

    private static final int DEFAULT_TIMEOUT_MILLIS = 15_000;

    private WaitForTheCart() {
    }

    public static Performable toListExactly(Collection<String> products) {
        List<String> expected = List.copyOf(products);
        // Typed local: the generic forNoMoreThan(Duration) would otherwise be inferred as Task.where's Consumer overload.
        WaitWithTimeout waitForTheRenderedCart = Wait.until(TheCart.contents(), isSettledWith(expected)).forNoMoreThan(timeout());
        return Task.where("{0} waits until the cart lists exactly " + expected, waitForTheRenderedCart);
    }

    static Duration timeout() {
        int millis = SystemEnvironmentVariables.currentEnvironmentVariables()
                .getPropertyAsInteger(ThucydidesSystemProperty.WEBDRIVER_WAIT_FOR_TIMEOUT, DEFAULT_TIMEOUT_MILLIS);
        return Duration.ofMillis(millis);
    }

    private static Matcher<CartSnapshot> isSettledWith(List<String> expected) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(CartSnapshot cart) {
                return cart.isSettledWith(expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a fully rendered cart listing exactly " + expected
                        + " with a total equal to the sum of their prices");
            }
        };
    }
}

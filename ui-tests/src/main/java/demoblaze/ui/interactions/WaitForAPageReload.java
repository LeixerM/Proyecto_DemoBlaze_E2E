package demoblaze.ui.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.waits.Wait;
import net.serenitybdd.screenplay.waits.WaitWithTimeout;
import org.openqa.selenium.WebDriverException;

import static org.hamcrest.Matchers.is;

/**
 * Some Demoblaze actions (e.g. "Delete" in the cart) end with {@code location.reload()}. Reading the page
 * right after the click can hit the old document, so the current document is marked first and the actor
 * then waits, for a bounded time, until a document without the mark (the reloaded one) is shown.
 */
public final class WaitForAPageReload {

    private static final String MARK = "data-awaiting-reload";

    private WaitForAPageReload() {
    }

    public static Performable markTheCurrentPage() {
        return Interaction.where("{0} marks the current page to detect its reload",
                actor -> BrowseTheWeb.as(actor)
                        .evaluateJavascript("document.documentElement.setAttribute('" + MARK + "', 'true');"));
    }

    public static Performable afterTheMarkedPage() {
        // Typed local so the generic forNoMoreThan(Duration) resolves to a concrete Performable.
        WaitWithTimeout waitForTheReload = Wait.until(theMarkedPageIsGone(), is(true)).forNoMoreThan(WaitForTheCart.timeout());
        return waitForTheReload;
    }

    private static Question<Boolean> theMarkedPageIsGone() {
        return Question.about("whether the page was reloaded").answeredBy(WaitForAPageReload::markIsGone);
    }

    private static boolean markIsGone(Actor actor) {
        try {
            Object marked = BrowseTheWeb.as(actor)
                    .evaluateJavascript("return document.documentElement.hasAttribute('" + MARK + "');");
            return Boolean.FALSE.equals(marked);
        } catch (WebDriverException pageIsStillNavigating) {
            return false;
        }
    }
}

package demoblaze.ui.questions;

import demoblaze.ui.models.CartSnapshot;
import demoblaze.ui.ui.CartPage;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.Collection;
import java.util.List;

/**
 * Reads the cart page. Every read locates the elements again (nothing is cached), because the site
 * rebuilds the whole table after a page reload and any element found before that becomes stale.
 */
public final class TheCart {

    /** A read that hits a stale element is simply repeated against freshly located elements. */
    private static final int READ_ATTEMPTS = 3;

    private TheCart() {
    }

    public static Question<CartSnapshot> contents() {
        return Question.about("the cart contents").answeredBy(TheCart::readContents);
    }

    public static Question<Collection<String>> productNames() {
        return Question.about("the products in the cart")
                .answeredBy(actor -> readContents(actor).productNames());
    }

    public static Question<Integer> total() {
        return Question.about("the cart total").answeredBy(actor -> readContents(actor).total());
    }

    private static CartSnapshot readContents(Actor actor) {
        for (int attempt = 1; attempt <= READ_ATTEMPTS; attempt++) {
            try {
                List<String> totals = textsOf(CartPage.TOTAL, actor);
                return CartSnapshot.read(
                        textsOf(CartPage.PRODUCT_TITLES, actor),
                        textsOf(CartPage.PRODUCT_PRICES, actor),
                        totals.isEmpty() ? null : totals.get(0));
            } catch (StaleElementReferenceException pageWasRebuiltMidRead) {
                // The table was replaced between locating and reading an element: read it again.
            }
        }
        return CartSnapshot.unreadable();
    }

    private static List<String> textsOf(Target target, Actor actor) {
        return target.resolveAllFor(actor).stream().map(WebElementFacade::getText).toList();
    }
}

package demoblaze.ui.questions;

import demoblaze.ui.ui.CartPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import java.util.Collection;
import java.util.List;

public final class TheCart {

    private TheCart() {
    }

    public static Question<Collection<String>> productNames() {
        return Question.about("the products in the cart")
                .answeredBy(actor -> List.copyOf(Text.ofEach(CartPage.PRODUCT_TITLES).answeredBy(actor)
                        .stream().map(String::trim).toList()));
    }

    public static Question<Integer> total() {
        return Question.about("the cart total")
                .answeredBy(actor -> Integer.parseInt(Text.of(CartPage.TOTAL).answeredBy(actor).trim()));
    }
}

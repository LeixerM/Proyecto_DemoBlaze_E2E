package demoblaze.ui.questions;

import demoblaze.ui.models.PurchaseReceipt;
import demoblaze.ui.ui.PlaceOrderModal;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.function.Function;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Each field of the confirmation dialog is its own question, so a failure names the exact field. */
public final class ThePurchaseConfirmation {

    private ThePurchaseConfirmation() {
    }

    public static Question<String> title() {
        return Question.about("the purchase confirmation title")
                .answeredBy(actor -> {
                    waitForTheConfirmation(actor);
                    return Text.of(PlaceOrderModal.CONFIRMATION_TITLE).answeredBy(actor).trim();
                });
    }

    public static Question<String> orderId() {
        return field("the order id on the receipt", PurchaseReceipt::orderId);
    }

    public static Question<Integer> amount() {
        return field("the amount on the receipt", PurchaseReceipt::amount);
    }

    public static Question<String> cardNumber() {
        return field("the card number on the receipt", PurchaseReceipt::cardNumber);
    }

    public static Question<String> customerName() {
        return field("the customer name on the receipt", PurchaseReceipt::name);
    }

    public static Question<String> date() {
        return field("the date on the receipt", PurchaseReceipt::date);
    }

    private static <T> Question<T> field(String description, Function<PurchaseReceipt, T> extractor) {
        return Question.about(description).answeredBy(actor -> extractor.apply(receipt(actor)));
    }

    private static PurchaseReceipt receipt(Actor actor) {
        waitForTheConfirmation(actor);
        return PurchaseReceipt.parse(Text.of(PlaceOrderModal.CONFIRMATION_DETAILS).answeredBy(actor));
    }

    private static void waitForTheConfirmation(Actor actor) {
        actor.attemptsTo(WaitUntil.the(PlaceOrderModal.CONFIRMATION_DETAILS, isVisible()));
    }
}

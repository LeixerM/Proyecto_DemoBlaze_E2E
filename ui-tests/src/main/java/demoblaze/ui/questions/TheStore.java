package demoblaze.ui.questions;

import demoblaze.ui.interactions.AcceptTheAlert;
import demoblaze.ui.ui.StorePage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class TheStore {

    private TheStore() {
    }

    /** The greeting shown in the navigation bar once a user is logged in. */
    public static Question<String> welcomeMessage() {
        return Question.about("the welcome message")
                .answeredBy(actor -> {
                    actor.attemptsTo(WaitUntil.the(StorePage.WELCOME_MESSAGE, isVisible()));
                    return Text.of(StorePage.WELCOME_MESSAGE).answeredBy(actor).trim();
                });
    }

    /** Text of the last browser alert accepted with {@link AcceptTheAlert}. */
    public static Question<String> lastAlertMessage() {
        return Question.about("the last alert message")
                .answeredBy(actor -> actor.recall(AcceptTheAlert.LAST_ALERT_TEXT));
    }
}

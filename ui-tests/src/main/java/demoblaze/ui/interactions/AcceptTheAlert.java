package demoblaze.ui.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Waits for the native browser alert Demoblaze uses for feedback ("Product added", "Wrong password.", ...),
 * remembers its text for later questions and accepts it.
 */
public class AcceptTheAlert implements Interaction {

    public static final String LAST_ALERT_TEXT = "lastAlertText";

    public static AcceptTheAlert shownByTheSite() {
        return new AcceptTheAlert();
    }

    @Override
    @Step("{0} accepts the browser alert")
    public <T extends Actor> void performAs(T actor) {
        Alert alert = BrowseTheWeb.as(actor).waitFor(ExpectedConditions.alertIsPresent());
        actor.remember(LAST_ALERT_TEXT, alert.getText());
        alert.accept();
    }
}

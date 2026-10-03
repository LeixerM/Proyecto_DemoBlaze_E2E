package demoblaze.ui.tasks;

import demoblaze.ui.models.Credentials;
import demoblaze.ui.ui.LogInModal;
import demoblaze.ui.ui.StorePage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class LogIn {

    private LogIn() {
    }

    public static Performable withThe(Credentials credentials) {
        return Task.where("{0} logs in as " + credentials.username(),
                Click.on(StorePage.LOG_IN_LINK),
                WaitUntil.the(LogInModal.USERNAME, isVisible()),
                Enter.theValue(credentials.username()).into(LogInModal.USERNAME),
                Enter.theValue(credentials.password()).into(LogInModal.PASSWORD),
                Click.on(LogInModal.LOG_IN_BUTTON)
        );
    }
}

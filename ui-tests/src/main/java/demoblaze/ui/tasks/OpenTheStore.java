package demoblaze.ui.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;

public final class OpenTheStore {

    private OpenTheStore() {
    }

    /** Opens the home page configured as {@code webdriver.base.url} in serenity.conf. */
    public static Performable homePage() {
        return Task.where("{0} opens the Demoblaze store", Open.browserOn().thePageNamed("home.page"));
    }
}

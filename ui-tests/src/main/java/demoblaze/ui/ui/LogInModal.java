package demoblaze.ui.ui;

import net.serenitybdd.screenplay.targets.Target;

public final class LogInModal {

    public static final Target USERNAME = Target.the("the log in username field").locatedBy("#loginusername");
    public static final Target PASSWORD = Target.the("the log in password field").locatedBy("#loginpassword");
    public static final Target LOG_IN_BUTTON = Target.the("the 'Log in' button")
            .locatedBy("//div[@id='logInModal']//button[normalize-space()='Log in']");

    private LogInModal() {
    }
}

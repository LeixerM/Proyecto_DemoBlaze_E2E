package demoblaze.ui.models;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/** A Demoblaze account. Each scenario generates its own, so runs never collide. */
public record Credentials(String username, String password) {

    public static Credentials unique() {
        String suffix = UUID.randomUUID().toString().replace("-", "");
        return new Credentials("qa_ui_" + suffix.substring(0, 12), "Pw-" + suffix.substring(12, 24));
    }

    /**
     * The website Base64-encodes the password before calling the API, so an account created
     * directly through the API must store the same encoding to be usable from the browser.
     */
    public String passwordAsSentByTheWebsite() {
        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String toString() {
        return "Credentials[username=" + username + ", password=***]";
    }
}

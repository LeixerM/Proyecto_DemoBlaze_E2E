package demoblaze.ui.tasks;

import demoblaze.ui.models.Credentials;
import demoblaze.ui.questions.TheApiResponse;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

import static io.restassured.http.ContentType.JSON;

/** Creates an account through the REST API (requires the CallAnApi ability), as test setup for UI scenarios. */
public final class RegisterAnAccount {

    private RegisterAnAccount() {
    }

    public static Performable throughTheApi(Credentials credentials) {
        Map<String, String> body = Map.of(
                "username", credentials.username(),
                "password", credentials.passwordAsSentByTheWebsite());
        return Task.where("{0} registers the account " + credentials.username() + " through the API",
                Post.to("/signup").with(request -> request.contentType(JSON).body(body)),
                Ensure.that(TheApiResponse.statusCode()).isEqualTo(200),
                // Demoblaze answers 200 for errors too; an empty JSON string ("") is the only success body.
                Ensure.that(TheApiResponse.body()).isEqualTo("\"\"")
        );
    }
}

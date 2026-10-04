package demoblaze.ui.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

public final class TheApiResponse {

    private TheApiResponse() {
    }

    public static Question<Integer> statusCode() {
        return Question.about("the API response status code")
                .answeredBy(actor -> SerenityRest.lastResponse().statusCode());
    }

    public static Question<String> body() {
        return Question.about("the API response body")
                .answeredBy(actor -> SerenityRest.lastResponse().asString().trim());
    }
}

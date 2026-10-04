package demoblaze.ui.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CredentialsTest {

    @Test
    void encodesThePasswordLikeTheWebsite() {
        // The site sends btoa("Secret123") on sign up and log in.
        assertEquals("U2VjcmV0MTIz", new Credentials("ana", "Secret123").passwordAsSentByTheWebsite());
    }

    @Test
    void generatesADifferentAccountEveryTime() {
        assertNotEquals(Credentials.unique().username(), Credentials.unique().username());
    }

    @Test
    void neverPrintsThePassword() {
        assertFalse(new Credentials("ana", "Secret123").toString().contains("Secret123"));
    }
}

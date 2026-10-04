package demoblaze.ui.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartSnapshotTest {

    @Test
    void isSettledWhenItListsExactlyTheExpectedProductsAndTheTotalMatchesTheirPrices() {
        CartSnapshot cart = CartSnapshot.read(List.of(" Nokia lumia 1520 ", "Samsung galaxy s6"), List.of("820", "360"), " 1180 ");

        assertTrue(cart.isSettledWith(List.of("Samsung galaxy s6", "Nokia lumia 1520")));
    }

    @Test
    void isNotSettledWhileTheRemovedProductIsStillListed() {
        CartSnapshot cart = CartSnapshot.read(List.of("Samsung galaxy s6", "Nokia lumia 1520"), List.of("360", "820"), "1180");

        assertFalse(cart.isSettledWith(List.of("Samsung galaxy s6")));
    }

    @Test
    void isNotSettledWhileTheRowsAreStillBeingAppended() {
        // After a reload the site appends one row per AJAX response; the expected product is still missing.
        CartSnapshot cart = CartSnapshot.read(List.of("Samsung galaxy s6"), List.of("360"), "360");

        assertFalse(cart.isSettledWith(List.of("Samsung galaxy s6", "Nokia lumia 1520")));
    }

    @Test
    void isNotSettledWhenTheTotalDoesNotMatchTheListedPrices() {
        CartSnapshot cart = CartSnapshot.read(List.of("Samsung galaxy s6"), List.of("360"), "1180");

        assertFalse(cart.isSettledWith(List.of("Samsung galaxy s6")));
    }

    @Test
    void anEmptyOrUnreadableTotalIsReportedAsMissing() {
        assertNull(CartSnapshot.read(List.of(), List.of(), "").total());
        assertNull(CartSnapshot.read(List.of(), List.of(), null).total());
        assertFalse(CartSnapshot.unreadable().isSettledWith(List.of()));
    }

    @Test
    void trimsTheProductNames() {
        assertEquals(List.of("Samsung galaxy s6"), CartSnapshot.read(List.of(" Samsung galaxy s6\n"), List.of("360"), "360").productNames());
    }
}

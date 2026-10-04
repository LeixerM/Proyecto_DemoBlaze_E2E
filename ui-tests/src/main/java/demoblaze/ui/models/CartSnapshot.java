package demoblaze.ui.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * What the cart page shows at one moment: product names, their prices and the total.
 *
 * <p>The site builds the cart incrementally (one AJAX call per row, updating the total each time),
 * so a snapshot is only trustworthy once it is <em>settled</em>: it lists exactly the expected
 * products and the total equals the sum of the listed prices.
 */
public record CartSnapshot(List<String> productNames, List<Integer> prices, Integer total) {

    public CartSnapshot {
        productNames = List.copyOf(productNames);
        // A price cell that is not a number yet is kept as null, so the list must accept nulls.
        prices = Collections.unmodifiableList(new ArrayList<>(prices));
    }

    /** Builds a snapshot from the raw texts on the page; an empty or non-numeric total becomes {@code null}. */
    public static CartSnapshot read(List<String> names, List<String> prices, String total) {
        return new CartSnapshot(
                names.stream().map(String::trim).toList(),
                prices.stream().map(CartSnapshot::numberOrNull).toList(),
                numberOrNull(total));
    }

    /** The page was replaced while it was being read, so nothing on it can be trusted yet. */
    public static CartSnapshot unreadable() {
        return new CartSnapshot(List.of(), List.of(), null);
    }

    public boolean isSettledWith(Collection<String> expectedProducts) {
        return listsExactly(expectedProducts) && totalMatchesThePrices();
    }

    private boolean listsExactly(Collection<String> expectedProducts) {
        return productNames.stream().sorted().toList().equals(expectedProducts.stream().sorted().toList());
    }

    private boolean totalMatchesThePrices() {
        if (total == null || prices.size() != productNames.size() || prices.stream().anyMatch(Objects::isNull)) {
            return false;
        }
        return prices.stream().mapToInt(Integer::intValue).sum() == total;
    }

    private static Integer numberOrNull(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException notANumber) {
            return null;
        }
    }
}

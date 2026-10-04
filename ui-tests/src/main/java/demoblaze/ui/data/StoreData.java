package demoblaze.ui.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import demoblaze.ui.models.Customer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

/** Test data loaded once from {@code data/store.json} on the classpath. */
public final class StoreData {

    private static final String RESOURCE = "/data/store.json";
    private static final Data DATA = load();

    private StoreData() {
    }

    /** Price of a product as listed in the catalog, in USD. */
    public static int priceOf(String product) {
        Integer price = DATA.catalog().get(product);
        if (price == null) {
            throw new IllegalArgumentException("Unknown product in " + RESOURCE + ": " + product);
        }
        return price;
    }

    public static int totalPriceOf(List<String> products) {
        return products.stream().mapToInt(StoreData::priceOf).sum();
    }

    public static Customer customer(String profile) {
        Customer customer = DATA.customers().get(profile);
        if (customer == null) {
            throw new IllegalArgumentException("Unknown customer profile in " + RESOURCE + ": " + profile);
        }
        return customer;
    }

    private static Data load() {
        try (InputStream json = StoreData.class.getResourceAsStream(RESOURCE)) {
            if (json == null) {
                throw new IllegalStateException(RESOURCE + " not found on the classpath");
            }
            return new ObjectMapper().readValue(json, Data.class);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + RESOURCE, e);
        }
    }

    private record Data(Map<String, Integer> catalog, Map<String, Customer> customers) {
    }
}

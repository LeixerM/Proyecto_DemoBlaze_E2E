package demoblaze.ui.models;

/** Billing details typed into the "Place order" form. */
public record Customer(String name, String country, String city, String creditCard, String month, String year) {
}

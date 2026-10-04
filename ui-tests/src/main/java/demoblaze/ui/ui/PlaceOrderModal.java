package demoblaze.ui.ui;

import net.serenitybdd.screenplay.targets.Target;

public final class PlaceOrderModal {

    public static final Target NAME = Target.the("the order name field").locatedBy("#name");
    public static final Target COUNTRY = Target.the("the order country field").locatedBy("#country");
    public static final Target CITY = Target.the("the order city field").locatedBy("#city");
    public static final Target CREDIT_CARD = Target.the("the order credit card field").locatedBy("#card");
    public static final Target MONTH = Target.the("the order month field").locatedBy("#month");
    public static final Target YEAR = Target.the("the order year field").locatedBy("#year");
    public static final Target PURCHASE = Target.the("the 'Purchase' button")
            .locatedBy("//div[@id='orderModal']//button[normalize-space()='Purchase']");

    public static final Target CONFIRMATION_TITLE = Target.the("the purchase confirmation title")
            .locatedBy(".sweet-alert h2");
    public static final Target CONFIRMATION_DETAILS = Target.the("the purchase confirmation details")
            .locatedBy(".sweet-alert p.lead");

    private PlaceOrderModal() {
    }
}

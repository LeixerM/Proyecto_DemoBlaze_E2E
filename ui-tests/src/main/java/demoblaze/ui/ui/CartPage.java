package demoblaze.ui.ui;

import net.serenitybdd.screenplay.targets.Target;

public final class CartPage {

    public static final Target PRODUCT_TITLES = Target.the("the product titles in the cart")
            .locatedBy("#tbodyid tr td:nth-child(2)");
    public static final Target ROW_OF_PRODUCT = Target.the("the cart row of '{0}'")
            .locatedBy("//tbody[@id='tbodyid']/tr[td[2][normalize-space()='{0}']]");
    public static final Target DELETE_LINK_OF_PRODUCT = Target.the("the 'Delete' link of '{0}'")
            .locatedBy("//tbody[@id='tbodyid']/tr[td[2][normalize-space()='{0}']]//a[normalize-space()='Delete']");
    public static final Target TOTAL = Target.the("the cart total").locatedBy("#totalp");
    public static final Target PLACE_ORDER = Target.the("the 'Place Order' button")
            .locatedBy("//button[normalize-space()='Place Order']");

    private CartPage() {
    }
}

package demoblaze.ui.ui;

import net.serenitybdd.screenplay.targets.Target;

/** Home page catalog, product page and navigation bar. */
public final class StorePage {

    public static final Target PRODUCT_LINK = Target.the("the '{0}' product link")
            .locatedBy("//div[@id='tbodyid']//h4[@class='card-title']/a[normalize-space()='{0}']");
    public static final Target PRODUCT_NAME = Target.the("the product name")
            .locatedBy("#tbodyid h2.name");
    public static final Target ADD_TO_CART = Target.the("the 'Add to cart' button")
            .locatedBy("//div[@id='tbodyid']//a[normalize-space()='Add to cart']");

    public static final Target CART_LINK = Target.the("the 'Cart' link").locatedBy("#cartur");
    public static final Target LOG_IN_LINK = Target.the("the 'Log in' link").locatedBy("#login2");
    // Only matches once the page has reloaded after log in, so waits never hold a stale element.
    public static final Target WELCOME_MESSAGE = Target.the("the welcome message")
            .locatedBy("//a[@id='nameofuser' and starts-with(normalize-space(), 'Welcome')]");

    private StorePage() {
    }
}

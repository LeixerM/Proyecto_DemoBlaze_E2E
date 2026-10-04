package demoblaze.ui.stepdefinitions;

import demoblaze.ui.data.StoreData;
import demoblaze.ui.interactions.AcceptTheAlert;
import demoblaze.ui.models.Credentials;
import demoblaze.ui.models.Customer;
import demoblaze.ui.models.PurchaseReceipt;
import demoblaze.ui.questions.TheCart;
import demoblaze.ui.questions.ThePurchaseConfirmation;
import demoblaze.ui.questions.TheStore;
import demoblaze.ui.tasks.AddToCart;
import demoblaze.ui.tasks.LogIn;
import demoblaze.ui.tasks.OpenTheCart;
import demoblaze.ui.tasks.OpenTheStore;
import demoblaze.ui.tasks.PlaceTheOrder;
import demoblaze.ui.tasks.RegisterAnAccount;
import demoblaze.ui.tasks.RemoveFromCart;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.environment.SystemEnvironmentVariables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/** Thin glue: maps Gherkin to Screenplay tasks and Ensure assertions. */
public class StoreStepDefinitions {

    private static final String PRODUCTS_IN_CART = "productsInCart";
    private static final String ORDER_TOTAL = "orderTotal";
    private static final String CUSTOMER = "customer";
    private static final String CREDENTIALS = "credentials";

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("{word} is browsing the Demoblaze store")
    public void isBrowsingTheStore(String actorName) {
        actor(actorName).wasAbleTo(OpenTheStore.homePage());
    }

    @Given("{word} has an account registered through the API")
    public void hasAnAccountRegisteredThroughTheApi(String actorName) {
        Credentials credentials = Credentials.unique();
        Actor actor = actor(actorName).whoCan(CallAnApi.at(apiUrl()));
        actor.remember(CREDENTIALS, credentials);
        actor.wasAbleTo(RegisterAnAccount.throughTheApi(credentials));
    }

    @When("she adds the following products to the cart")
    public void addsProductsToTheCart(List<String> products) {
        theActorInTheSpotlight().remember(PRODUCTS_IN_CART, new ArrayList<>(products));
        theActorInTheSpotlight().attemptsTo(
                AddToCart.theProducts(products),
                OpenTheCart.containing(products)
        );
    }

    @When("she removes {string} from the cart")
    public void removesFromTheCart(String product) {
        List<String> products = theActorInTheSpotlight().recall(PRODUCTS_IN_CART);
        products.remove(product);
        theActorInTheSpotlight().attemptsTo(RemoveFromCart.theProduct(product, products));
    }

    @Then("the cart lists exactly those products")
    public void theCartListsTheAddedProducts() {
        List<String> products = theActorInTheSpotlight().recall(PRODUCTS_IN_CART);
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(TheCart.productNames()).containsExactlyInAnyOrderElementsFrom(products)
        );
    }

    @Then("the cart lists exactly these products")
    public void theCartListsTheseProducts(List<String> expectedProducts) {
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(TheCart.productNames()).containsExactlyInAnyOrderElementsFrom(expectedProducts)
        );
    }

    @Then("the cart total is the sum of their catalog prices")
    public void theCartTotalIsTheSumOfCatalogPrices() {
        List<String> products = theActorInTheSpotlight().recall(PRODUCTS_IN_CART);
        int expectedTotal = StoreData.totalPriceOf(products);
        theActorInTheSpotlight().remember(ORDER_TOTAL, expectedTotal);
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(TheCart.total()).isEqualTo(expectedTotal)
        );
    }

    @When("she places the order with the billing details of the {string}")
    public void placesTheOrder(String customerProfile) {
        theActorInTheSpotlight().remember(CUSTOMER, StoreData.customer(customerProfile));
        theActorInTheSpotlight().attemptsTo(PlaceTheOrder.withBillingDetailsOf(StoreData.customer(customerProfile)));
    }

    @When("she tries to purchase without billing details")
    public void triesToPurchaseWithoutBillingDetails() {
        theActorInTheSpotlight().attemptsTo(
                PlaceTheOrder.withoutBillingDetails(),
                AcceptTheAlert.shownByTheSite()
        );
    }

    @Then("the purchase is confirmed with a receipt that matches the order")
    public void thePurchaseIsConfirmed() {
        Customer customer = theActorInTheSpotlight().recall(CUSTOMER);
        int orderTotal = theActorInTheSpotlight().recall(ORDER_TOTAL);
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(ThePurchaseConfirmation.title()).isEqualTo("Thank you for your purchase!"),
                Ensure.that(ThePurchaseConfirmation.orderId()).containsOnlyDigits(),
                Ensure.that(ThePurchaseConfirmation.amount()).isEqualTo(orderTotal),
                Ensure.that(ThePurchaseConfirmation.cardNumber()).isEqualTo(customer.creditCard()),
                Ensure.that(ThePurchaseConfirmation.customerName()).isEqualTo(customer.name()),
                Ensure.that(ThePurchaseConfirmation.date()).isEqualTo(PurchaseReceipt.dateAsPrintedBySite(LocalDate.now()))
        );
    }

    @When("she logs in with her account")
    public void logsInWithHerAccount() {
        Credentials credentials = theActorInTheSpotlight().recall(CREDENTIALS);
        theActorInTheSpotlight().attemptsTo(LogIn.withThe(credentials));
    }

    @When("she logs in with a wrong password")
    public void logsInWithAWrongPassword() {
        Credentials credentials = theActorInTheSpotlight().recall(CREDENTIALS);
        theActorInTheSpotlight().attemptsTo(
                LogIn.withThe(new Credentials(credentials.username(), credentials.password() + "-wrong")),
                AcceptTheAlert.shownByTheSite()
        );
    }

    @Then("the store welcomes her by username")
    public void theStoreWelcomesHer() {
        Credentials credentials = theActorInTheSpotlight().recall(CREDENTIALS);
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(TheStore.welcomeMessage()).isEqualTo("Welcome " + credentials.username())
        );
    }

    @Then("she is told {string}")
    public void sheIsTold(String message) {
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(TheStore.lastAlertMessage()).isEqualTo(message)
        );
    }

    private static Actor actor(String name) {
        return switch (name.toLowerCase()) {
            case "she", "he" -> theActorInTheSpotlight();
            default -> theActorCalled(name);
        };
    }

    private static String apiUrl() {
        return EnvironmentSpecificConfiguration.from(SystemEnvironmentVariables.currentEnvironmentVariables())
                .getProperty("demoblaze.api.url");
    }
}

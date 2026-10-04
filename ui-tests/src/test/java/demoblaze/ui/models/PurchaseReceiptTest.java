package demoblaze.ui.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchaseReceiptTest {

    @Test
    void parsesEveryFieldOfAMultiLineReceipt() {
        PurchaseReceipt receipt = PurchaseReceipt.parse(
                "Id: 4521873\nAmount: 1180 USD\nCard Number: 4111111111111111\nName: Juan Perez\nDate: 3/9/2026");

        assertEquals(new PurchaseReceipt("4521873", 1180, "4111111111111111", "Juan Perez", "3/9/2026"), receipt);
    }

    @Test
    void parsesAReceiptRenderedOnOneLine() {
        PurchaseReceipt receipt = PurchaseReceipt.parse(
                "Id: 7 Amount: 360 USD Card Number: 4111 Name: Ana Maria Date: 1/0/2027");

        assertEquals("Ana Maria", receipt.name());
        assertEquals(360, receipt.amount());
        assertEquals("1/0/2027", receipt.date());
    }

    @Test
    void rejectsTextThatIsNotAReceipt() {
        assertThrows(IllegalArgumentException.class, () -> PurchaseReceipt.parse("Thank you!"));
    }

    @Test
    void printsTheMonthZeroBasedLikeTheSite() {
        assertEquals("3/9/2026", PurchaseReceipt.dateAsPrintedBySite(LocalDate.of(2026, 10, 3)));
        assertEquals("1/0/2027", PurchaseReceipt.dateAsPrintedBySite(LocalDate.of(2027, 1, 1)));
    }
}

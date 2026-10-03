package demoblaze.ui.models;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The confirmation shown after a purchase, e.g.
 * {@code Id: 8456123 Amount: 1180 USD Card Number: 4111... Name: Ana Date: 3/9/2026}.
 */
public record PurchaseReceipt(String orderId, int amount, String cardNumber, String name, String date) {

    private static final Pattern RECEIPT = Pattern.compile(
            "Id:\\s*(\\d+)\\s*Amount:\\s*(\\d+)\\s*USD\\s*Card Number:\\s*(.*?)\\s*Name:\\s*(.*?)\\s*Date:\\s*(\\S+)",
            Pattern.DOTALL);

    public static PurchaseReceipt parse(String text) {
        Matcher matcher = RECEIPT.matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Not a Demoblaze purchase receipt: " + text);
        }
        return new PurchaseReceipt(matcher.group(1), Integer.parseInt(matcher.group(2)),
                matcher.group(3), matcher.group(4), matcher.group(5));
    }

    /**
     * The receipt date as Demoblaze prints it. The site builds it with JavaScript's zero-based
     * {@code Date.getMonth()}, so October 3rd 2026 is printed as {@code 3/9/2026}. This is a known
     * defect of the demo site; the suite pins the current behaviour so a fix becomes visible.
     */
    public static String dateAsPrintedBySite(LocalDate day) {
        return day.getDayOfMonth() + "/" + (day.getMonthValue() - 1) + "/" + day.getYear();
    }
}

package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage extends BasePage {
    private static final Pattern BALANCE_PATTERN = Pattern.compile("баланс:\\s*(\\d+)");

    private final SelenideElement dashboard = $(byTestId("dashboard"));

    public DashboardPage() {
        dashboard.shouldBe(visible);
    }

    public int getCardBalance(DataHelper.CardInfo card) {
        return extractBalance(getCard(card).text());
    }

    public TransferPage selectCardToDeposit(DataHelper.CardInfo card) {
        getCard(card).$(byTestId("action-deposit")).click();
        return new TransferPage(card);
    }

    private SelenideElement getCard(DataHelper.CardInfo card) {
        return $(byTestId(card.id()));
    }

    private int extractBalance(String text) {
        var matcher = BALANCE_PATTERN.matcher(text.replaceAll("\\s+", " "));
        if (!matcher.find()) {
            throw new IllegalStateException("Cannot extract balance from text: " + text);
        }
        return Integer.parseInt(matcher.group(1));
    }
}

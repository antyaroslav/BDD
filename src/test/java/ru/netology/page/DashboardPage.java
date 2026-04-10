package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage extends BasePage {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    private final SelenideElement dashboard = $(byTestId("dashboard"));
    private final SelenideElement reloadButton = $(byTestId("action-reload"));

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

    public DashboardPage reload() {
        reloadButton.click();
        dashboard.shouldBe(visible);
        return this;
    }

    private SelenideElement getCard(DataHelper.CardInfo card) {
        return $(byTestId(card.id()));
    }

    private int extractBalance(String text) {
        Matcher matcher = NUMBER_PATTERN.matcher(text);
        int lastNumber = -1;
        while (matcher.find()) {
            lastNumber = Integer.parseInt(matcher.group(1));
        }
        if (lastNumber < 0) {
            throw new IllegalStateException("Cannot extract balance from text: " + text);
        }
        return lastNumber;
    }
}

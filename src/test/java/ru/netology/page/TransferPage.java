package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage extends BasePage {
    private final SelenideElement amountInput = $(byTestIdAndTag("amount", "input"));
    private final SelenideElement fromInput = $(byTestIdAndTag("from", "input"));
    private final SelenideElement toInput = $(byTestIdAndTag("to", "input"));
    private final SelenideElement transferButton = $(byTestId("action-transfer"));
    private final SelenideElement cancelButton = $(byTestId("action-cancel"));
    private final SelenideElement errorNotification = $(byTestId("error-notification"));

    public TransferPage(DataHelper.CardInfo targetCard) {
        amountInput.shouldBe(visible);
        toInput.shouldHave(value("**** **** **** " + targetCard.getNumber().substring(targetCard.getNumber().length() - 4)));
    }

    public DashboardPage transferValidAmount(int amount, DataHelper.CardInfo fromCard) {
        fillTransferForm(amount, fromCard);
        submitTransfer();
        return new DashboardPage();
    }

    public TransferPage transferInvalidAmount(int amount, DataHelper.CardInfo fromCard) {
        fillTransferForm(amount, fromCard);
        submitTransfer();
        shouldShowError();
        return this;
    }

    public void shouldShowError() {
        errorNotification.shouldBe(visible).shouldHave(text("Ошибка!"));
    }

    public DashboardPage cancel() {
        cancelButton.click();
        return new DashboardPage();
    }

    private void fillTransferForm(int amount, DataHelper.CardInfo fromCard) {
        amountInput.setValue(String.valueOf(amount));
        fromInput.setValue(fromCard.getNumber());
    }

    private void submitTransfer() {
        transferButton.click();
    }
}
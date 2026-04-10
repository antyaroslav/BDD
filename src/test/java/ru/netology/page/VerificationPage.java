package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage extends BasePage {
    private final SelenideElement codeInput = $(byTestIdAndTag("code", "input"));
    private final SelenideElement verifyButton = $(byTestId("action-verify"));

    public VerificationPage() {
        codeInput.shouldBe(visible);
    }

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeInput.setValue(verificationCode.code());
        verifyButton.click();
        return new DashboardPage();
    }
}

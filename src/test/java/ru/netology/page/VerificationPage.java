package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage extends BasePage {
    private static final Duration PAGE_TIMEOUT = Duration.ofSeconds(15);

    private final SelenideElement codeInput = $(byTestIdAndTag("code", "input"));
    private final SelenideElement verifyButton = $(byTestId("action-verify"));

    public VerificationPage() {
        codeInput.shouldBe(visible, PAGE_TIMEOUT);
    }

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeInput.setValue(verificationCode.code());
        verifyButton.shouldBe(visible, PAGE_TIMEOUT).click();
        return new DashboardPage();
    }
}
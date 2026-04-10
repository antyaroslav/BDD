package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage extends BasePage {
    private final SelenideElement loginInput = $(byTestIdAndTag("login", "input"));
    private final SelenideElement passwordInput = $(byTestIdAndTag("password", "input"));
    private final SelenideElement continueButton = $(byTestId("action-login"));

    public LoginPage() {
        loginInput.shouldBe(visible);
    }

    public static LoginPage openPage() {
        open("/");
        return new LoginPage();
    }

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        loginInput.setValue(authInfo.login());
        passwordInput.setValue(authInfo.password());
        continueButton.click();
        return new VerificationPage();
    }
}

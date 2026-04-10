package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferTest extends BaseUiTest {
    @BeforeEach
    void cleanBrowserState() {
        open("/");
        clearBrowserCookies();
        executeJavaScript("window.localStorage.clear(); window.sessionStorage.clear();");
        refresh();
    }

    @Test
    void shouldTransferMoneyFromSecondCardToFirst() {
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();

        var dashboardPage = LoginPage.openPage()
                .validLogin(DataHelper.getAuthInfo())
                .validVerify(DataHelper.getVerificationCode());

        var firstBalanceBefore = dashboardPage.getCardBalance(firstCard);
        var secondBalanceBefore = dashboardPage.getCardBalance(secondCard);
        var amount = Math.min(500, secondBalanceBefore);

        var dashboardAfterTransfer = dashboardPage
                .selectCardToDeposit(firstCard)
                .transferValidAmount(amount, secondCard);

        assertEquals(firstBalanceBefore + amount, dashboardAfterTransfer.getCardBalance(firstCard));
        assertEquals(secondBalanceBefore - amount, dashboardAfterTransfer.getCardBalance(secondCard));
    }
}

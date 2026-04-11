package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferTest extends BaseUiTest {
    private final DataHelper.CardInfo firstCard = DataHelper.getFirstCard();
    private final DataHelper.CardInfo secondCard = DataHelper.getSecondCard();
    private DashboardPage dashboardPage;

    @BeforeEach
    void setUpSession() {
        open("/");
        dashboardPage = new LoginPage()
                .validLogin(DataHelper.getAuthInfo())
                .validVerify(DataHelper.getVerificationCode());
    }

    @Test
    void shouldTransferMoneyFromSecondCardToFirst() {
        var firstBalanceBefore = dashboardPage.getCardBalance(firstCard);
        var secondBalanceBefore = dashboardPage.getCardBalance(secondCard);
        var amount = Math.min(500, secondBalanceBefore);

        dashboardPage = dashboardPage
                .selectCardToDeposit(firstCard)
                .transferValidAmount(amount, secondCard);

        assertEquals(firstBalanceBefore + amount, dashboardPage.getCardBalance(firstCard));
        assertEquals(secondBalanceBefore - amount, dashboardPage.getCardBalance(secondCard));
    }
}
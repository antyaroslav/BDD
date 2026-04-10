package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import ru.netology.data.ApiAuthHelper;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransferTest extends BaseUiTest {
    private final DataHelper.CardInfo firstCard = DataHelper.getFirstCard();
    private final DataHelper.CardInfo secondCard = DataHelper.getSecondCard();
    private DashboardPage dashboardPage;

    @BeforeAll
    void setUpSession() {
        var authInfo = DataHelper.getAuthInfo();
        var token = ApiAuthHelper.loginAndGetToken(
                System.getProperty("selenide.baseUrl", "http://localhost:9999"),
                authInfo,
                DataHelper.getVerificationCode()
        );

        open("/");
        executeJavaScript(
                "window.sessionStorage.setItem('login', arguments[0]); " +
                        "window.sessionStorage.setItem('token', arguments[1]); " +
                        "window.history.pushState({}, '', '/dashboard'); " +
                        "window.dispatchEvent(new PopStateEvent('popstate'));",
                authInfo.login(),
                token
        );
        dashboardPage = new DashboardPage();
    }

    @Test
    @Order(1)
    void shouldShowBalancesForBothCards() {
        assertTrue(dashboardPage.getCardBalance(firstCard) >= 0);
        assertTrue(dashboardPage.getCardBalance(secondCard) >= 0);
    }

    @Test
    @Order(2)
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
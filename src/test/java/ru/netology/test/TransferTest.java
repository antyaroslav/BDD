package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransferTest extends BaseUiTest {
    private static final int AUTH_ATTEMPTS = 3;

    private final DataHelper.CardInfo firstCard = DataHelper.getFirstCard();
    private final DataHelper.CardInfo secondCard = DataHelper.getSecondCard();
    private DashboardPage dashboardPage;

    @BeforeAll
    void setUpSession() {
        dashboardPage = authorize();
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

    private DashboardPage authorize() {
        RuntimeException lastError = null;

        for (int attempt = 1; attempt <= AUTH_ATTEMPTS; attempt++) {
            try {
                open("/");
                clearBrowserCookies();
                clearBrowserLocalStorage();
                executeJavaScript("window.sessionStorage.clear();");
                refresh();

                return LoginPage.openPage()
                        .validLogin(DataHelper.getAuthInfo())
                        .validVerify(DataHelper.getVerificationCode());
            } catch (RuntimeException | AssertionError error) {
                closeWebDriver();
                lastError = new RuntimeException("Authorization attempt " + attempt + " failed", error);
            }
        }

        throw lastError;
    }
}
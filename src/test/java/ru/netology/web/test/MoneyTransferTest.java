package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV1;
import ru.netology.web.page.LoginPageV2;
import ru.netology.web.page.LoginPageV3;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {
  LoginPageV1 loginPageV1;
  DashboardPage dashboardPage;


  @BeforeEach
  void setup() {
    loginPageV1 = open("http://localhost:9999", LoginPageV1.class);
    var authInfo = DataHelper.getAuthInfo();
    var verificationCode = loginPageV1.validLogin(authInfo);
    var verificationPage = DataHelper.getVerificationCode();
    dashboardPage = verificationPage.validVerify(verificationCode);
  }
  @Test
  void shouldTransferFromFirstToSecond() {
    var cardInfoFirst = getCardInfoFirst();
    var cardInfoSecond = getCardInfoSecond();
    var cardBalanceFirst = dashboardPage.getCardBalance(cardInfoFirst);
    var cardBalanceSecond = dashboardPage.getCardBalance(cardInfoSecond);
    var amount = generateValidAmount(cardBalanceFirst);
    var expectedBalanceFirstCard = cardBalanceFirst - amount;
    var expectedBalanceSecondCard = cardBalanceSecond + amount;
    var transferPage = dashboardPage.selectCardToTransfer(cardInfoSecond);
    dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), cardInfoFirst);
    var actualBalanceFirstCard = dashboardPage.getCardBalance(cardInfoFirst);
    var actualBalanceSecondCard = dashboardPage.getCardBalance(cardInfoSecond);
    assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
    assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
  }

 @Test
  void shouldTransferInvalidAmount() {
   var cardInfoFirst = getCardInfoFirst();
   var cardInfoSecond = getCardInfoSecond();
   var cardBalanceFirst = dashboardPage.getCardBalance(cardInfoFirst);
   var cardBalanceSecond = dashboardPage.getCardBalance(cardInfoSecond);
   var amount = generateInvalidAmount(cardBalanceFirst);
   var transferPage = dashboardPage.selectCardToTransfer(cardInfoFirst);
   transferPage.makeTransfer(String.valueOf(amount), cardInfoSecond);
   transferPage.findErrorMessage("Выполнена попытка перевода суммы, превыщающей остаток на карте списания");
   var actualBalanceFirstCard = dashboardPage.getCardBalance(cardInfoFirst);
   var actualBalanceSecondCard = dashboardPage.getCardBalance(cardInfoSecond);
   assertEquals(cardBalanceFirst, actualBalanceFirstCard);
   assertEquals(cardBalanceSecond, actualBalanceSecondCard);
 }
}
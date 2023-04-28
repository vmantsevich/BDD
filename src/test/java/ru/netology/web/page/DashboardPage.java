package ru.netology.web.page;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

  private static final ElementsCollection cards = $$(".list__item div");
  private final SelenideElement heading = $("[data-test-id=dashboard]");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";

  public DashboardPage() {
    heading.shouldBe(visible);
  }

  public int getCardBalance(DataHelper.CardInfo cardInfo) {
    var text = cards.find(text(cardInfo.getCardNumber().substring(15))).getText();
    return extractBalance(text);
  }

  public CardTransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
    cards.find(text(cardInfo.getCardNumber().substring(15))).$("button").click();
    return new CardTransferPage();
  }

  private int extractBalance(String text) {
    var start = text.indexOf(balanceStart);
    var finish = text.indexOf(balanceFinish);
    var value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }


}
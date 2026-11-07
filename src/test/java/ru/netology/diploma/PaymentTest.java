package ru.netology.diploma;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;


public class PaymentTest {

    @BeforeAll
    static void setUp() {
        Configuration.browserSize = "1920x1080";
        Configuration.headless = false;
        Configuration.holdBrowserOpen = false;
        open("http://localhost:8080");
    }

    @Test
    @Description("Ввод невалидного месяца (13)")
    void shouldInvalidMonth13() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue("13");
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверно указан срок действия карты")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод короткого номера карты")
    void shouldShortCardNumber() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 444");
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Отправка формы с пустым полем Владелец")
    void shouldEmptyOwnerField() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue("");
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод цифр и спецсимволов в поле Владелец")
    void shouldNumbersAndSymbolsInOwnerField() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue("123!@#");
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод короткого CVC")
    void shouldShortCvc() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue("12");

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод имени владельца на кириллице")
    void shouldOwnerNameInCyrillic() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(DataHelper.getInvalidOwnerRu());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод истекшего года действия карты")
    void shouldExpiredYear() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue("22");
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Истёк срок действия карты")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод невалидного месяца (00)")
    void shouldInvalidMonth() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue("00");
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверно указан срок действия карты")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Отказ в выдаче кредита по отклоненной карте")
    void shouldDeclinedCreditByCard() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить в кредит")).click();

        var card = DataHelper.getDeclinedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $(".notification_status_error").shouldBe(visible, Duration.ofSeconds(15));

        String expectedStatus = "DECLINED";
        assertEquals(expectedStatus, DbUtils.getCreditStatus());
    }

    @Test
    @Description("Успешная выдача кредита по одобренной карте")
    void shouldSuccessCreditByApprovedCard() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить в кредит")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $(".notification_status_ok").shouldBe(visible, Duration.ofSeconds(15));

        String expectedStatus = "APPROVED";
        assertEquals(expectedStatus, DbUtils.getCreditStatus());
    }

    @Test
    @Description("Отправка пустой формы оплаты")
    void shouldSendEmptyForm() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));

        $$(".input__sub").findBy(text("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Успешная оплата по одобренной карте")
    void shouldSuccessPaymentByApprovedCard() throws SQLException {
        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $(".notification_status_ok").shouldBe(visible, Duration.ofSeconds(15));

        String expectedStatus = "APPROVED";
        assertEquals(expectedStatus, DbUtils.getPaymentStatus());
    }

    @Test
    @Description("Ввод очень далекого года (99)")
    void shouldFarFutureYear() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue("99");
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверно указан срок действия карты")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод очень длинного имени владельца")
    void shouldVeryLongOwnerName() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue("Ivan Ivan Ivan Ivan Ivan Ivan Ivan Ivan Ivan Ivan");
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Проверка записи заказа в таблицу order_entity при успешной оплате")
    void shouldOrderRecordInDbAfterPayment() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());
        $$("button.button").findBy(text("Продолжить")).click();

        $(".notification_status_ok").shouldBe(visible, Duration.ofSeconds(15));

        int ordersCount = DbUtils.countOrders();
        assertTrue(ordersCount > 0, "В таблице order_entity должна быть запись о заказе");
    }

    @Test
    @Description("Проверка, что номер карты не сохраняется в БД (требование PCI DSS)")
    void shouldCardNumberNotSavedInDb() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());
        $$("button.button").findBy(text("Продолжить")).click();

        $(".notification_status_ok").shouldBe(visible, Duration.ofSeconds(15));

        boolean isSaved = DbUtils.isCardNumberSaved(card.getNumber());
        assertFalse(isSaved, "Номер карты не должен сохраняться в БД (требование PCI DSS)");
    }

    @Test
    @Description("Ввод нулевого номера карты")
    void shouldZeroCardNumber() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue("0000 0000 0000 0000");
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $(".notification_status_error").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @Description("Ввод букв в поле CVC")
    void shouldLettersInCvc() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue("abc");

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Отправка формы с пустым полем CVC")
    void shouldEmptyCvcField() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue("");

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод только пробелов в поле Владелец")
    void shouldOnlySpacesInOwnerField() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue("     ");
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод букв в поле номера карты")
    void shouldLettersInCardNumber() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue("abcd efgh ijkl mnop");
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Очистка полей при переключении между формами Оплата и Кредит")
    void shouldClearFieldsWhenSwitchingForms() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить в кредит")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Купить")).click();

        String cardValue = $("[placeholder='0000 0000 0000 0000']").getValue();
        assertTrue(cardValue == null || cardValue.isEmpty(), "При переключении форм поля должны очищаться");
    }

    @Test
    @Description("Ввод только имени (без фамилии) в поле Владелец")
    void shouldOnlyFirstNameInOwnerField() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue("Ivan");
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод одной цифры в поле Месяц")
    void shouldOneDigitInMonth() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue("8");
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Двойной клик по кнопке Продолжить")
    void shouldDoubleClickOnContinueButton() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        var button = $$("button.button").findBy(text("Продолжить"));
        button.doubleClick();

        $(".notification_status_ok").shouldBe(visible, Duration.ofSeconds(15));
        
        int ordersCount = DbUtils.countOrders();
        assertTrue(ordersCount == 1, "При двойном клике должна создаться только одна запись в БД, а создалось: " + ordersCount);
    }

    @Test
    @Description("Проверка, что при кредите данные пишутся в credit_request_entity, а не в payment_entity")
    void shouldCreditWritesToCreditTableNotPayment() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить в кредит")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());
        $$("button.button").findBy(text("Продолжить")).click();

        $(".notification_status_ok").shouldBe(visible, Duration.ofSeconds(15));

        String creditStatus = DbUtils.getCreditStatus();
        assertNotNull(creditStatus, "В таблице credit_request_entity должна быть запись о кредите");

        String paymentStatus = DbUtils.getPaymentStatus();
        assertNull(paymentStatus, "При покупке в кредит данные НЕ должны писаться в payment_entity");
    }

    @Test
    @Description("Ввод кириллицы в поле Владелец при покупке в кредит")
    void shouldCyrillicInOwnerFieldCredit() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить в кредит")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(DataHelper.getInvalidOwnerRu());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Ввод слишком длинного номера карты (17 цифр)")
    void shouldTooLongCardNumber() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getApprovedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue("4444 4444 4444 44411");
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $$(".input__sub").findBy(text("Неверный формат")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @Description("Отказ в оплате по отклоненной карте")
    void shouldDeclinedPaymentByCard() throws SQLException {
        DbUtils.clearTables();

        $$("button.button").findBy(text("Купить")).click();

        var card = DataHelper.getDeclinedCard();

        $("[placeholder='0000 0000 0000 0000']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $$(".input__control").get(3).setValue(card.getOwner());
        $("[placeholder='999']").setValue(card.getCvc());

        $$("button.button").findBy(text("Продолжить")).click();

        $(".notification_status_error").shouldBe(visible, Duration.ofSeconds(15));

        String expectedStatus = "DECLINED";
        assertEquals(expectedStatus, DbUtils.getPaymentStatus());
    }
}

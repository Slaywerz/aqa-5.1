package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    // Здесь идет настройка для открытия страницы SUT перед каждым тестом, чтобы не делать код больше
    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }

//    Здесь у нас находятся позитивные проверки
//    Сценарий, когда мы планируем заявку без перепланирования

    @Test
    @DisplayName("Plain delivery date")
    void shouldBeReservedMeet() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").doubleClick().
                sendKeys(Keys.chord(Keys.BACK_SPACE));
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("ru"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(10));
        $("[data-test-id='success-notification'][data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text(DataGenerator.generateDate(daysToAddForFirstMeeting)));
    }

//  Сценарий, когда юзер решил перепланировать заявку

    @Test
    @DisplayName("Plane and Re-plain delivery date")
    void shouldBeChangedDate() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        val daysToAddForSecondMeeting = 7;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").doubleClick().
                sendKeys(Keys.chord(Keys.BACK_SPACE));
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("ru"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(10));
        $("[data-test-id='success-notification'][data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text(DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").doubleClick().
                sendKeys(Keys.chord(Keys.BACK_SPACE));
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForSecondMeeting));
        $(byText("Запланировать")).click();
        $(byText("Перепланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(10));
        $("[data-test-id='success-notification'][data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text(DataGenerator.generateDate(daysToAddForSecondMeeting)));
    }


//    SadPath сценарии проверяются по тегу .input_invalid, который динамически появялется при некорректном заполнении поля


    @Test
    @DisplayName("Empty city field")
    void shouldInvalidCityName() {
        $(byText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid").shouldHave(Condition.visible);
    }

//        В данной проверке для исключения задвоенных ситуаций дополнительно идет проверка по тексту сообщения под полем
//        Город, доставка в который не осуществляется специально на английском, чтоб не хардкодить маленькие города

    @Test
    @DisplayName("Incorrect city")
    void shouldHaveTextWithImpossible() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(DataGenerator.generateIncorrectCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").doubleClick().
                sendKeys(Keys.chord(Keys.BACK_SPACE));
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("ru"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $(byText("Доставка в выбранный город недоступна")).shouldHave(Condition.visible);
    }

//    Тест, в котором дата не удовлетворяет условию в минимум 3 дня от даты заполнения заявки

    @Test
    @DisplayName("Incorrect date day")
    void shouldDateHaveError() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 1;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").doubleClick().
                sendKeys(Keys.chord(Keys.BACK_SPACE));
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("ru"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input_invalid .input__box .input__control[placeholder='Дата встречи']").shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Empty date field")
    void shouldDateHaveEmpty() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").doubleClick().
                sendKeys(Keys.chord(Keys.BACK_SPACE));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("ru"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input_invalid .input__box .input__control[placeholder='Дата встречи']").shouldBe(Condition.visible);
    }

//    Имя на английском, поле принимает только русский

    @Test
    @DisplayName("Name isn't a correct")
    void shouldNameHaveError() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").doubleClick().
                sendKeys(Keys.chord(Keys.BACK_SPACE));
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("cn"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__box .input__control[name='name']").shouldBe(Condition.visible);
    }
}


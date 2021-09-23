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
    void TearDown(){
        closeWindow();
    }

    // Это строчка необходима для очистки полей, чтобы записать в него новое значение не запуская новый тест
    public String delete() {
        return Keys.chord(Keys.CONTROL + "a") + Keys.DELETE;
    }

    @Test
    @DisplayName("Plain delivery date")
    void shouldBeReservedMeet() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(delete());
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

    @Test
    @DisplayName("Plane and Re-plain delivery date")
    void shouldBeChangedDate(){
            val validUser = DataGenerator.Registration.generateUser("ru");
            val daysToAddForFirstMeeting = 4;
            val daysToAddForSecondMeeting = 7;
            val secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
            $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
            $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(delete());
            $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                    .generateDate(daysToAddForFirstMeeting));
            $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("ru"));
            $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
            $("[data-test-id='agreement'] .checkbox__box").click();
            $(byText("Запланировать")).click();
            $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(10));
            $("[data-test-id='success-notification'][data-test-id='success-notification'] .notification__content")
                    .shouldHave(Condition.text(DataGenerator.generateDate(daysToAddForFirstMeeting)));
            $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(delete());
            $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                    .generateDate(daysToAddForSecondMeeting));
            $(byText("Запланировать")).click();
            $(byText("Перепланировать")).click();
            $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(10));
            $("[data-test-id='success-notification'][data-test-id='success-notification'] .notification__content")
                    .shouldHave(Condition.text(DataGenerator.generateDate(daysToAddForSecondMeeting)));
    }
    @Test
    @DisplayName("Invalid City Name")
        void shouldInvalidCityName(){
        $(byText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid").shouldHave(Condition.visible);
        }
    @Test
    @DisplayName("Meet in city is impossible")
    void shouldHaveTextWithImpossible(){
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue("Колыма");
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(delete());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("ru"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $(byText("Доставка в выбранный город недоступна")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Meet is impossible in yours select day")
    void shouldDateHaveError(){
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 1;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(delete());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("ru"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input_invalid .input__box .input__control[placeholder='Дата встречи']").shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Name isn't a correct")
    void shouldNameHaveError() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(delete());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator
                .generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(DataGenerator.generateFullName("cn"));
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__box .input__control[name='name']").shouldBe(Condition.visible);
    }
}


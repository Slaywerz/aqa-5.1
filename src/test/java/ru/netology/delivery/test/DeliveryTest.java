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
    @DisplayName("Should be reserved meet")
    void shouldBeReservedMeet() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        val daysToAddForSecondMeeting = 7;
        val secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] .input__box .input__control[placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='name'] .input__box .input__control[name='name']").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__box .input__control[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(delete());
        $("[data-test-id='date'] .input__box .input__control[placeholder='Дата встречи']").setValue(DataGenerator.generateDate(daysToAddForFirstMeeting));
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("").shouldHave(Condition.text(DataGenerator.generateDate(daysToAddForFirstMeeting)));
    }
}


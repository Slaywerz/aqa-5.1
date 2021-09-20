package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.open;

public class DeliveryTest {

    // Здесь идет настройка для открытия страницы SUT перед каждым тестом, чтобы не делать код больше
    @BeforeEach
    void setUp(){
        open("http://localhost:9999/");
    }

    // Это строчка необходима для очистки полей, чтобы записать в него новое значение не запуская новый тест
    public String delete(){
        return Keys.chord(Keys.CONTROL + "a") + Keys.DELETE;
    }
}


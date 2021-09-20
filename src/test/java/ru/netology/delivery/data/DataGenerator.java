package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private final static Faker faker = new Faker(new Locale("ru"));

    public String generateDate(int days){
        String date = LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;
    }

    public static String generatePhone(String locale) {
        String phone = faker.numerify("+79#########");
        return phone;
    }

    public static String generateFullName(String locale){
        String name = faker.name().fullName();
        return name;
    }

    public static String generateCity(String locale){
        String city = faker.options().option("Санкт-Петербург, Москва, Уфа, Казань, Екатеринбург, Петрозаводск" +
                "Сыктывкар, Пермь, Самара, Нальчик, Магас, Магадан, Краснодар, Красноярск, Чебоксары, Грозный, Саранск");
        return city;
    }

    public static class Registration{
        private Registration(){
        }
    }

    public static UserInfo generateUser(String locale){
        UserInfo user = new UserInfo(generatePhone(locale), generateCity(locale), generateFullName(locale));
        return user;
    }

    @Value
    public static class UserInfo {
        String phone;
        String city;
        String name;
    }

    public static void main(String[] args) {
        System.out.println(generateCity("ru"));
    }
}

package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
public class DataGenerator {
    private static Faker faker = new Faker();

    public static String generateDate(int days){
        String date = LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;
    }

    public static String generatePhone(String locale) {

        String phone = faker.numerify("+79#########");
        return phone;
    }

    public static String generateFullName(String locale) {
        Faker faker1 = new Faker(new Locale(locale));
        String name = faker1.name().fullName();
        return name;
    }

//  Хардкодим большое количество административных центров субъектов РФ для генерации Faker'а
    public static String generateCity(){
        String city = faker.options().option("Санкт-Петербург", "Москва", "Уфа", "Казань", "Екатеринбург", "Петрозаводск",
                "Сыктывкар", "Пермь", "Самара", "Нальчик", "Магас", "Магадан", "Краснодар", "Красноярск", "Чебоксары",
                "Грозный", "Саранск", "Майкоп", "Анадырь", "Элиста", "Ижевск", "Якутск", "Владивосток", "Хабаровск", "Чита",
                "Кызыл", "Барнаул", "Калининград", "Волгоград", "Ставрополь", "Севастополь");
        return city;
    }

    public static class Registration{
        private Registration(){
        }


    public static UserInfo generateUser(String locale){
            UserInfo user = new UserInfo(generatePhone(locale), generateCity(), generateFullName(locale));
            return user;
    }

    @Value
    public static class UserInfo {
        String phone;
        String city;
        String name;
    }
}
}

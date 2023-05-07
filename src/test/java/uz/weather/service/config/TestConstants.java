package uz.weather.service.config;

import java.time.LocalDateTime;

import static uz.weather.service.config.Constants.DATE_TIME_FORMATTER;

public class TestConstants {
    public static final String FIRSTNAME = "FIRSTNAME";
    public static final String LASTNAME = "LASTNAME";
    public static final String PHONE_NUMBER = "+998999999999";
    public static final String EMAIL = "email@gmail.com";
    public static final String PASSWORD = "12345";
    public static final String CREATED_AT = createDate();
    public static final String CITY_NAME = "TASHKENT";
    public static final String CITY_NAME2 = "ANDIJAN";
    public static final String CITY_NAME3 = "FERGANA";
    public static final String COUNTRY = "UZBEKISTAN";
    public static final String SHORT_NAME_COUNTRY = "UZB";
    public static final String UPDATED_COUNTRY = "TURKISH";
    public static final String UPDATED_SHORT_NAME_COUNTRY = "TUR";
    public static final Integer HUMIDITY = 50;
    public static final Integer TEMPERATURE = 20;
    public static final Integer WIND = 25;
    public static final Integer VISIBILITY = 5000;
    public static final Integer PRESSURE = 30;
    public static final Integer UPDATED_PRESSURE = 20;
    public static final Integer UPDATED_HUMIDITY = 40;
    public static final Integer UPDATED_TEMPERATURE = 30;
    public static final Integer UPDATED_WIND = 35;
    public static final Integer UPDATED_VISIBILITY = 7000;
    public static final String UPDATED_EMAIL = "updated@gmail.com";
    public static final String UPDATED_FIRSTNAME = "UPDATED_FIRSTNAME";
    public static final String UPDATED_LASTNAME = "UPDATED_LASTNAME";
    public static final String UPDATED_PHONE_NUMBER = "+998907777777";
    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2023, 5, 5, 12, 35);
    public static final LocalDateTime LOCAL_DATE_TIME2 = LocalDateTime.of(2023,  5, 5, 12, 50);
    private static String createDate() {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.format(DATE_TIME_FORMATTER);
    }
}

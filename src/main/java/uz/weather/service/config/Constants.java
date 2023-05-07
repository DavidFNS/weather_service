package uz.weather.service.config;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String ADMIN_FIRSTNAME = "ADMIN";
    public static final String ADMIN_LASTNAME = "ADMIN";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PASSWORD = "123456789";
    public static final String ADMIN_PHONE_NUMBER = "+998999999999";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
}

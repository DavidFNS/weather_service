package uz.weather.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.weather.service.domain.City;
import uz.weather.service.domain.Subscriptions;
import uz.weather.service.domain.Users;
import uz.weather.service.domain.Weather;
import uz.weather.service.dto.UsersDTO;
import uz.weather.service.repository.CityRepository;
import uz.weather.service.repository.SubscriptionRepository;
import uz.weather.service.repository.UsersRepository;
import uz.weather.service.repository.WeatherRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static uz.weather.service.config.TestConstants.*;

@Component
public class WeatherTestHelper {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public List<City> saveDummyCities() {
        List<City> cities = List.of(
                City.builder()
                        .country(COUNTRY)
                        .name(CITY_NAME)
                        .shortName(SHORT_NAME_COUNTRY)
                        .visible(true)
                        .createdAt(LOCAL_DATE_TIME)
                        .build(),
                City.builder()
                        .country(COUNTRY)
                        .name(CITY_NAME2)
                        .shortName(SHORT_NAME_COUNTRY)
                        .visible(true)
                        .createdAt(LOCAL_DATE_TIME2)
                        .build(),
                City.builder()
                        .country(COUNTRY)
                        .name(CITY_NAME3)
                        .shortName(SHORT_NAME_COUNTRY)
                        .visible(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        return cityRepository.saveAll(cities).collectList().block();
    }

    public void saveDummyUsers() {
        List<Users> users = List.of(
                Users.builder()
                        .firstName(FIRSTNAME)
                        .lastName(LASTNAME)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .phoneNumber(PHONE_NUMBER)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Users.builder()
                        .firstName(FIRSTNAME)
                        .lastName(LASTNAME)
                        .email("EMAIL2")
                        .password(PASSWORD)
                        .phoneNumber(PHONE_NUMBER)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Users.builder()
                        .firstName(FIRSTNAME)
                        .lastName(LASTNAME)
                        .email("EMAIL3")
                        .password(PASSWORD)
                        .phoneNumber(PHONE_NUMBER)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Users.builder()
                        .firstName(FIRSTNAME)
                        .lastName(LASTNAME)
                        .email("EMAIL4")
                        .password(PASSWORD)
                        .phoneNumber(PHONE_NUMBER)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        userRepository.saveAll(users).collectList().block();
    }

    public UsersDTO createUserDTO() {
        return UsersDTO.builder()
                .firstName(FIRSTNAME)
                .lastName(LASTNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .createdAt(CREATED_AT)
                .build();
    }

    public void saveDummySubscriptions(List<City> cities, Users user) {
        List<Subscriptions> subscriptions = List.of(
                Subscriptions.builder()
                        .city(cities.get(0))
                        .cityId(cities.get(0).getId())
                        .user(user)
                        .userId(Objects.requireNonNull(user).getId())
                        .subscribedAt(LOCAL_DATE_TIME)
                        .build(),
                Subscriptions.builder()
                        .city(cities.get(1))
                        .cityId(cities.get(1).getId())
                        .user(user)
                        .userId(Objects.requireNonNull(user).getId())
                        .subscribedAt(LOCAL_DATE_TIME)
                        .build(),
                Subscriptions.builder()
                        .city(cities.get(2))
                        .cityId(cities.get(2).getId())
                        .user(user)
                        .userId(Objects.requireNonNull(user).getId())
                        .subscribedAt(LOCAL_DATE_TIME)
                        .build()
        );

        subscriptionRepository.saveAll(subscriptions).collectList().block();
    }

    public List<Weather> saveDummyWeathers(List<City> cities) {
        List<Weather> weathers = List.of(
                Weather.builder()
                        .humidity(HUMIDITY)
                        .visibility(VISIBILITY)
                        .wind(WIND)
                        .temperature(TEMPERATURE)
                        .pressure(PRESSURE)
                        .city(cities.get(0))
                        .cityId(cities.get(0).getId())
                        .date(LOCAL_DATE_TIME)
                        .build(),
                Weather.builder()
                        .humidity(HUMIDITY)
                        .visibility(VISIBILITY)
                        .wind(WIND)
                        .temperature(TEMPERATURE)
                        .pressure(PRESSURE)
                        .city(cities.get(1))
                        .cityId(cities.get(1).getId())
                        .date(LOCAL_DATE_TIME)
                        .build(),
                Weather.builder()
                        .humidity(HUMIDITY)
                        .visibility(VISIBILITY)
                        .wind(WIND)
                        .temperature(TEMPERATURE)
                        .pressure(PRESSURE)
                        .city(cities.get(2))
                        .cityId(cities.get(2).getId())
                        .date(LOCAL_DATE_TIME)
                        .build()
        );

        return weatherRepository.saveAll(weathers).collectList().block();
    }
}

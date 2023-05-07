package uz.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import uz.weather.service.domain.City;
import uz.weather.service.domain.Users;
import uz.weather.service.domain.Weather;
import uz.weather.service.dto.*;
import uz.weather.service.repository.CityRepository;
import uz.weather.service.repository.SubscriptionRepository;
import uz.weather.service.repository.UsersRepository;
import uz.weather.service.repository.WeatherRepository;
import uz.weather.service.service.mapper.WeatherMapper;
import uz.weather.service.utils.WeatherTestHelper;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static uz.weather.service.config.Constants.ADMIN_EMAIL;
import static uz.weather.service.config.Constants.ADMIN_PASSWORD;
import static uz.weather.service.config.TestConstants.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class AdminMethodsTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private WeatherTestHelper testHelper;
    @Autowired
    private WeatherMapper weatherMapper;
    private static String token;

    @BeforeEach
    public void deleteUserResource() {
        webTestClient = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(60))
                .build();
        subscriptionRepository.deleteAll().block();
        weatherRepository.deleteAll().block();
        cityRepository.deleteAll().block();
    }

    @Order(10)
    @Test
    public void getTokenTestForAdmin() {
        var loginDTO = LoginDTO.builder()
                .email(ADMIN_EMAIL)
                .password(ADMIN_PASSWORD)
                .build();

        ResponseDTO<String> response = webTestClient.post()
                .uri("/users/login")
                .bodyValue(loginDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(new ParameterizedTypeReference<ResponseDTO<String>>() {
                })
                .getResponseBody()
                .doOnNext(responseData -> log.info("Response DATA: {}", responseData))
                .blockFirst();

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(response.getMessage(), "OK");

        token = response.getData();
    }

    @Order(20)
    @Test
    @SneakyThrows
    public void getAllUser() {
        userRepository.deleteAllByEmailNotEquals(ADMIN_EMAIL).block();
        testHelper.saveDummyUsers();

        Flux<UsersDTO> response = webTestClient.get()
                .uri("/users/user-list")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(UsersDTO.class)
                .getResponseBody();

        assertNotNull(response);
        List<UsersDTO> users = response.collectList().block();
        assertNotNull(users);
        assertEquals(users.size(), 5);

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(users));
    }

    @Test
    @Order(30)
    public void editUserTest() throws JsonProcessingException {
        Users user = userRepository.findByEmail(EMAIL).block();
        UsersDTO usersDTO = testHelper.createUserDTO();
        assert user != null;
        usersDTO.setId(user.getId());
        usersDTO.setEmail(UPDATED_EMAIL);
        usersDTO.setFirstName(UPDATED_FIRSTNAME);
        usersDTO.setLastName(UPDATED_LASTNAME);
        usersDTO.setPhoneNumber(UPDATED_PHONE_NUMBER);

        ResponseDTO<UsersDTO> response = webTestClient
                .put()
                .uri("/users/edit-user")
                .header("Authorization", "Bearer " + token)
                .bodyValue(usersDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(new ParameterizedTypeReference<ResponseDTO<UsersDTO>>() {})
                .getResponseBody()
                .blockFirst();

        assertNotNull(response);
        assertNotNull(response.getData());
        assertTrue(response.isSuccess());
        assertEquals(response.getMessage(), "UPDATED");
        assertEquals(response.getData().getEmail(), UPDATED_EMAIL);
        assertEquals(response.getData().getFirstName(), UPDATED_FIRSTNAME);
        assertEquals(response.getData().getLastName(), UPDATED_LASTNAME);
        assertEquals(response.getData().getPhoneNumber(), UPDATED_PHONE_NUMBER);

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    @Order(40)
    @Test
    @SneakyThrows
    public void getAllSubscriptionsTest() {
        List<City> cities = testHelper.saveDummyCities();
        Users user = userRepository.findByEmail(UPDATED_EMAIL).block();
        testHelper.saveDummySubscriptions(cities, user);

        Flux<SubscriptionDTO> response = webTestClient.get()
                .uri("/subscribe/all-subscriptions")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(SubscriptionDTO.class)
                .getResponseBody();

        assertNotNull(response);
        List<SubscriptionDTO> subscriptions = response.collectList().block();
        assertNotNull(subscriptions);
        assertEquals(subscriptions.size(), 3);

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(subscriptions));
    }

    @Order(50)
    @Test
    @SneakyThrows
    public void getSubscriptionsByUserIdTest() {
        List<City> cities = testHelper.saveDummyCities();
        Users user = userRepository.findByEmail(UPDATED_EMAIL).block();
        testHelper.saveDummySubscriptions(cities, user);
        assertNotNull(user);

        Flux<SubscriptionDTO> response = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                                .path("/subscribe/user-details")
                                .queryParam("userId", user.getId())
                                .build()
                )
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(SubscriptionDTO.class)
                .getResponseBody();

        assertNotNull(response);
        List<SubscriptionDTO> subscriptions = response.collectList().block();
        assertNotNull(subscriptions);
        assertEquals(subscriptions.size(), 3);

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(subscriptions));
    }

    @Test
    @Order(60)
    public void editCityTest() throws JsonProcessingException {
        List<City> cities = testHelper.saveDummyCities();
        City city = cities.get(0);

        CityDTO cityDTO = CityDTO.builder()
                // old
                .id(city.getId())
                .name(city.getName())
                // updated
                .country(UPDATED_COUNTRY)
                .shortName(UPDATED_SHORT_NAME_COUNTRY)
                .visible(false)
                .createdAt(CREATED_AT)
                .build();

        ResponseDTO<CityDTO> response = webTestClient
                .put()
                .uri("/cities/edit-city")
                .header("Authorization", "Bearer " + token)
                .bodyValue(cityDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(new ParameterizedTypeReference<ResponseDTO<CityDTO>>() {})
                .getResponseBody()
                .blockFirst();

        assertNotNull(response);
        assertNotNull(response.getData());
        assertTrue(response.isSuccess());
        assertEquals(response.getMessage(), "UPDATED");
        assertEquals(city.getCountry(), COUNTRY);
        assertEquals(response.getData().getCountry(), UPDATED_COUNTRY);
        assertEquals(city.getShortName(), SHORT_NAME_COUNTRY);
        assertEquals(response.getData().getShortName(), UPDATED_SHORT_NAME_COUNTRY);
        assertTrue(city.isVisible());
        assertEquals(response.getData().getVisible(), false);
        assertEquals(response.getData().getCreatedAt(), CREATED_AT);

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    @Test
    @Order(70)
    public void updateCityWeatherTest() throws JsonProcessingException {
        List<City> cities = testHelper.saveDummyCities();
        List<Weather> weathers = testHelper.saveDummyWeathers(cities);
        Weather weatherBeforeUpdated = weathers.get(0);

        WeatherDTO weatherDTO = weatherMapper.toDto(weathers.get(0));
        weatherDTO.setCityId(cities.get(1).getId());
        weatherDTO.setDate(CREATED_AT);
        weatherDTO.setPressure(UPDATED_PRESSURE);
        weatherDTO.setHumidity(UPDATED_HUMIDITY);
        weatherDTO.setVisibility(UPDATED_VISIBILITY);
        weatherDTO.setTemperature(UPDATED_TEMPERATURE);
        weatherDTO.setWind(UPDATED_WIND);

        ResponseDTO<WeatherDTO> response = webTestClient
                .put()
                .uri("/weather/update-city-weather")
                .header("Authorization", "Bearer " + token)
                .bodyValue(weatherDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(new ParameterizedTypeReference<ResponseDTO<WeatherDTO>>() {})
                .getResponseBody()
                .blockFirst();

        assertNotNull(response);
        assertNotNull(response.getData());
        assertTrue(response.isSuccess());
        assertEquals(response.getMessage(), "UPDATED");
        assertEquals(weatherBeforeUpdated.getCityId(), cities.get(0).getId());
        assertEquals(response.getData().getCityId(), cities.get(1).getId());
        assertEquals(weatherBeforeUpdated.getPressure(), PRESSURE);
        assertEquals(response.getData().getPressure(), UPDATED_PRESSURE);
        assertEquals(weatherBeforeUpdated.getHumidity(), HUMIDITY);
        assertEquals(response.getData().getHumidity(), UPDATED_HUMIDITY);
        assertEquals(weatherBeforeUpdated.getWind(), WIND);
        assertEquals(response.getData().getWind(), UPDATED_WIND);
        assertEquals(weatherBeforeUpdated.getTemperature(), TEMPERATURE);
        assertEquals(response.getData().getTemperature(), UPDATED_TEMPERATURE);

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }
}

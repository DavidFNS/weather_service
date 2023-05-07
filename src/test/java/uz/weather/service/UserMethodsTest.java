package uz.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import uz.weather.service.dto.*;
import uz.weather.service.repository.CityRepository;
import uz.weather.service.repository.SubscriptionRepository;
import uz.weather.service.repository.UsersRepository;
import uz.weather.service.repository.WeatherRepository;
import uz.weather.service.utils.WeatherTestHelper;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static uz.weather.service.config.TestConstants.EMAIL;
import static uz.weather.service.config.TestConstants.PASSWORD;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class UserMethodsTest {
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

    @Test
    @Order(10)
    public void registerUserTestToSuccess() throws JsonProcessingException {
        userRepository.deleteByEmail(EMAIL).block();

        UsersDTO usersDTO = testHelper.createUserDTO();

        ResponseDTO<UsersDTO> response = webTestClient
                .post()
                .uri("/users/register")
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
        assertEquals(response.getMessage(), "SAVED");
        assertEquals(response.getData().getEmail(), usersDTO.getEmail());
        assertEquals(response.getData().getFirstName(), usersDTO.getFirstName());
        assertEquals(response.getData().getLastName(), usersDTO.getLastName());
        assertEquals(response.getData().getPhoneNumber(), usersDTO.getPhoneNumber());
        assertEquals(response.getData().getCreatedAt(), usersDTO.getCreatedAt());

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    @Test
    @Order(20)
    public void registerUserTestWithExistingEmail() throws JsonProcessingException {
        UsersDTO usersDTO = testHelper.createUserDTO();
        ResponseDTO<UsersDTO> response = webTestClient
                .post()
                .uri("/users/register")
                .bodyValue(usersDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(new ParameterizedTypeReference<ResponseDTO<UsersDTO>>() {})
                .getResponseBody()
                .blockFirst();

        assertNotNull(response);
        assertNotNull(response.getData());
        assertFalse(response.isSuccess());
        assertEquals(response.getMessage(), "User with email " + usersDTO.getEmail() + " is already exists");

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    @Order(30)
    @Test
    public void getTokenTestForUser() {
//        saveDummyUser();
        var loginDTO = LoginDTO.builder()
                .email(EMAIL)
                .password(PASSWORD)
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

    @Order(40)
    @Test
    public void subscribeToCityTest() {
        List<City> cities = testHelper.saveDummyCities();

        ResponseDTO<SubscriptionDTO> response = webTestClient.post()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/subscribe/subscribe-to-city")
                                .queryParam("cityId", cities.get(0).getId())
                                .build()
                )
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(new ParameterizedTypeReference<ResponseDTO<SubscriptionDTO>>() {})
                .getResponseBody()
                .doOnNext(responseData -> log.info("Response DATA: {}", responseData))
                .blockFirst();

        assertNotNull(response);
        assertNotNull(response.getData());
        assertTrue(response.isSuccess());
        assertNotNull(response.getData().getCity());
        assertEquals(response.getData().getCity().getId(), cities.get(0).getId());
    }

    @Order(50)
    @Test
    public void getUserSubscriptionsTest() throws JsonProcessingException {
        List<City> cities = testHelper.saveDummyCities();
        testHelper.saveDummyWeathers(cities);

        // subscribe user manually
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                                .path("/subscribe/subscribe-to-city")
                                .queryParam("cityId", cities.get(0).getId())
                                .build()
                )
                .header("Authorization", "Bearer " + token)
                .exchange();

        Flux<WeatherDTO> response = webTestClient.get()
                .uri("/weather/user-subscriptions")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(WeatherDTO.class)
                .getResponseBody();

        assertNotNull(response);
        List<WeatherDTO> weathers = response.collectList().block();
        assertNotNull(weathers);
        assertEquals(weathers.size(), 1);
        assertEquals(Objects.requireNonNull(weatherRepository.findAll().collectList().block()).size(), 3);

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(weathers));
    }

    // check one of the admin methods to forbidden for user
    @Order(60)
    @Test
    public void getAllSubscriptionsTestToForbidden() {
        List<City> cities = testHelper.saveDummyCities();
        testHelper.saveDummyWeathers(cities);

        webTestClient.get()
                .uri("/subscribe/all-subscriptions")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isForbidden();
    }
}



















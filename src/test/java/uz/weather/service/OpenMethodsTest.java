package uz.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import uz.weather.service.dto.CityDTO;
import uz.weather.service.repository.CityRepository;
import uz.weather.service.repository.SubscriptionRepository;
import uz.weather.service.repository.WeatherRepository;
import uz.weather.service.utils.WeatherTestHelper;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)

public class OpenMethodsTest {
    @Autowired
    private ObjectMapper objectMapper;
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
    public void getAllVisibleCitiesTestToUnAuthorization() {
        testHelper.saveDummyCities();
        webTestClient.get()
                .uri("/cities/cities-list")
                .exchange()
                .expectStatus().isUnauthorized();
    }
    @Test
    @WithMockUser
    public void getAllVisibleCitiesTestToSuccess() throws JsonProcessingException {
        testHelper.saveDummyCities();

        Flux<CityDTO> response = webTestClient.get()
                .uri("/cities/cities-list")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(CityDTO.class)
                .getResponseBody();

        assertNotNull(response);
        List<CityDTO> cityList = response.collectList().block();
        assertNotNull(cityList);
        assertEquals(cityList.size(), 2);
        assertEquals(Objects.requireNonNull(cityRepository.findAll().collectList().block()).size(), 3);

        System.out.println("RESPONSE DATA: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }
}

package uz.weather.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.dto.WeatherDTO;
import uz.weather.service.service.WeatherService;

import javax.validation.Valid;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherResource {

    private final WeatherService weatherService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update-city-weather")
    public Mono<ResponseDTO<WeatherDTO>> updateCityWeather(@Valid @RequestBody WeatherDTO dto) {
        return weatherService.updateWeather(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-weather")
    public Mono<ResponseDTO<WeatherDTO>> addWeather(@Valid @RequestBody WeatherDTO dto) {
        return weatherService.updateWeather(dto);
    }

    @GetMapping("/user-subscriptions")
    public Flux<WeatherDTO> getSubscriptionsWeather() {
        return weatherService.getSubscriptionsWeather();
    }
}

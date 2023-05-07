package uz.weather.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.City;
import uz.weather.service.domain.Users;
import uz.weather.service.dto.CityDTO;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.dto.WeatherDTO;
import uz.weather.service.repository.CityRepository;
import uz.weather.service.repository.WeatherRepository;
import uz.weather.service.service.WeatherService;
import uz.weather.service.service.mapper.CityMapper;
import uz.weather.service.service.mapper.WeatherMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    // Admin method - update city weather
    @Override
    public Mono<ResponseDTO<WeatherDTO>> updateWeather(WeatherDTO dto) {
        log.debug("Request to update weather: {}", dto);

        return weatherRepository.findById(dto.getId())
                .flatMap( weather -> {
                            if (weather == null) {
                                log.warn("Weather not found by id: {}", dto.getId());

                                return Mono.just(
                                        ResponseDTO.<WeatherDTO>builder()
                                                .message("Weather not found by id: " + dto.getId())
                                                .build()
                                );
                            }

                    return weatherRepository.save(weatherMapper.toEntity(dto))
                            .map(weatherMapper::toDto)
                            .flatMap(this::findCityByIdAndLoadWeather)
                            .doOnSuccess(updatedWeather -> log.debug("Successfully updated weather: {}", updatedWeather))
                            .map( updatedWeather ->
                                    ResponseDTO.<WeatherDTO>builder()
                                            .message("UPDATED")
                                            .success(true)
                                            .data(updatedWeather)
                                            .build()
                            );
                        }
                );
    }

    // Client method - Get weather data for subscribed cities
    @Override
    public Flux<WeatherDTO> getSubscriptionsWeather() {
        log.debug("Request to get all subscription weathers");
        return ReactiveSecurityContextHolder.getContext()
                .flux()
                .flatMap(auth -> {
                    Users u = (Users) auth.getAuthentication().getPrincipal();
                    return weatherRepository.getAllWeatherForUser(u.getId())
                            .map(weatherMapper::toDto)
                            .flatMap(this::findCityByIdAndLoadWeather);
                });
    }

    @Override
    public Mono<ResponseDTO<WeatherDTO>> addWeather(WeatherDTO dto) {
        log.debug("Request to save weather: {}", dto);

        return weatherRepository.save(weatherMapper.toEntity(dto))
                .map(weatherMapper::toDto)
                .flatMap(this::findCityByIdAndLoadWeather)
                .doOnSuccess(savedWeather -> log.debug("Successfully saved weather: {}", savedWeather))
                .map( updatedWeather ->
                        ResponseDTO.<WeatherDTO>builder()
                                .message("UPDATED")
                                .success(true)
                                .data(updatedWeather)
                                .build()
                );
    }


    // Reload city details and return weather with city for each request
    private Mono<WeatherDTO> findCityByIdAndLoadWeather(WeatherDTO dto) {
        log.debug("Request to get city by weather: {}", dto);

        return Mono.just(dto)
                .zipWith(cityRepository.findById(dto.getCityId()))
                .map(result -> {
                    result.getT1().setCity(cityMapper.toDto(result.getT2()));
                    return result.getT1();
                });
    }
}

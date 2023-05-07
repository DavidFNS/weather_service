package uz.weather.service.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.dto.WeatherDTO;

public interface WeatherService {

    /**
     * Update weather of city and users get last changed weather info.
     * @param weather Weather info
     * @return Added weather info
     */
    Mono<ResponseDTO<WeatherDTO>> updateWeather(WeatherDTO weather);

    /**
     * Get all subscribed cities of logged-in user.
     * @return Flux of WeatherDto
     */
    Flux<WeatherDTO> getSubscriptionsWeather();

    /**
     * Adding weather
     * @param dto requested object to add
     * @return ResponseDto with added Weather information
     */
    Mono<ResponseDTO<WeatherDTO>> addWeather(WeatherDTO dto);
}

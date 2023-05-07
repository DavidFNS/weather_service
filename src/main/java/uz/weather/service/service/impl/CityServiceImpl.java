package uz.weather.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.City;
import uz.weather.service.dto.CityDTO;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.repository.CityRepository;
import uz.weather.service.service.CityService;
import uz.weather.service.service.mapper.CityMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    // Method for creating a new city
    @Override
    public Mono<ResponseDTO<CityDTO>> addCity(CityDTO dto) {
        log.debug("Request to save city: {}", dto);

        return cityRepository.save(cityMapper.toEntity(dto))
                .map(c -> ResponseDTO.<CityDTO>builder()
                        .data(cityMapper.toDto(c))
                        .success(true)
                        .message("OK")
                        .build());
    }

    // Method get list of cities
    @Override
    public Flux<CityDTO> getCitiesList() {
        log.debug("Request to get all cities");

        return cityRepository.findAllByVisible(true)
                .map(cityMapper::toDto);
    }

    // Admin method - edit city
    @Override
    public Mono<ResponseDTO<CityDTO>> editCity(CityDTO dto) {
        log.debug("Request to update city: {}", dto);

        return cityRepository.findById(dto.getId())
                .flatMap( city -> {
                            if (city == null) {
                                log.warn("City not found by id: {}", dto.getId());

                                return Mono.just(
                                        ResponseDTO.<CityDTO>builder()
                                                .message("City not found by id: " + dto.getId())
                                                .build()
                                );
                            }

                            City entity = cityMapper.toEntity(dto);
                            return cityRepository.save(entity)
                                    .map(cityMapper::toDto)
                                    .doOnSuccess(savedCityDTO -> log.debug("Successfully updated city: {}", savedCityDTO))
                                    .map(cityDTO ->
                                            ResponseDTO.<CityDTO>builder()
                                                    .message("UPDATED")
                                                    .success(true)
                                                    .data(cityDTO)
                                                    .build()
                                    );
                        }
                );
    }

    @Override
    public Mono<CityDTO> getById(Integer id) {
        log.debug("Request to get city by id: {}", id);

        return cityRepository.findById(id)
                .map(cityMapper::toDto);
    }
}

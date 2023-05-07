package uz.weather.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.dto.CityDTO;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.service.CityService;

import javax.validation.Valid;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CitiesResource {
    private final CityService cityService;
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public Mono<ResponseDTO<CityDTO>> addCity(@Valid @RequestBody CityDTO dto){
        return cityService.addCity(dto);
    }

    @GetMapping("/cities-list")
    public Flux<CityDTO> getCitiesList() {
        return cityService.getCitiesList();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit-city")
    public Mono<ResponseDTO<CityDTO>> editCity(@Valid @RequestBody CityDTO dto){
        return cityService.editCity(dto);
    }
}

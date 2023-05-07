package uz.weather.service.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.dto.CityDTO;
import uz.weather.service.dto.ResponseDTO;


public interface CityService {

    /**
     * Adding new City
     * @param dto requested object to add
     * @return ResponseDto with added City information
     */
    Mono<ResponseDTO<CityDTO>> addCity(CityDTO dto);

    /**
     * Getting all visible cities.
     * @return Flux of fetched cities.
     */
    Flux<CityDTO> getCitiesList();

    /**
     * Update city info. Works as patching method. Updates only changed fields.
     * @param dto Requested City for updating
     * @return ResponseDto with updated City
     */
    Mono<ResponseDTO<CityDTO>> editCity(CityDTO dto);

    /**
     * Getting City with its ID
     * @param id ID of city
     * @return Mono of fetched City
     */
    Mono<CityDTO> getById(Integer id);
}

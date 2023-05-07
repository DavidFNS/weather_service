package uz.weather.service.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.weather.service.domain.Weather;
import uz.weather.service.dto.CityDTO;
import uz.weather.service.dto.WeatherDTO;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, CityDTO.class})
public interface WeatherMapper extends EntityMapper<WeatherDTO, Weather> {

    @Mapping(target = "date", expression = "java(LocalDateTime.now())")
    Weather toEntity(WeatherDTO dto);

    @Mapping(target = "date", dateFormat = "dd.MM.yyyy HH:mm:ss")
    WeatherDTO toDto(Weather entity);
}

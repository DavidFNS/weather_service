package uz.weather.service.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.weather.service.domain.City;
import uz.weather.service.dto.CityDTO;

@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    CityDTO toDto(City city);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    City toEntity(CityDTO dto);
}

package uz.weather.service.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.weather.service.domain.Subscriptions;
import uz.weather.service.dto.CityDTO;
import uz.weather.service.dto.SubscriptionDTO;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, CityDTO.class})
public interface SubscriptionMapper extends EntityMapper<SubscriptionDTO, Subscriptions> {
    @Mapping(source = "subscribedAt", target = "subscribedAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    SubscriptionDTO toDto(Subscriptions entity);
}

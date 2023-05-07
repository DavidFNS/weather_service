package uz.weather.service.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.weather.service.domain.Users;
import uz.weather.service.dto.UsersDTO;

@Mapper(componentModel = "spring")
public interface UsersMapper extends EntityMapper<UsersDTO, Users> {
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    UsersDTO toDto(Users users);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    Users toEntity(UsersDTO dto);
}

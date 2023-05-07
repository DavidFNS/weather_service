package uz.weather.service.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.Users;
import uz.weather.service.dto.LoginDTO;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.dto.UsersDTO;

public interface UserService{

    /**
     * Adding new User to database
     * @param dto User's object that sent from client
     * @return Reactive response of UsersDto wrapped with ResponseDto
     */
    Mono<ResponseDTO<UsersDTO>> addUser(UsersDTO dto);

    /**
     * Change user info as PATCH method. Updates only given fields.
     * @param usersDto Requested object to update
     * @return ResponseDto of updated User
     */
    Mono<ResponseDTO<UsersDTO>> editUser(UsersDTO usersDto);

    /**
     * Get user by userId
     * @param id ID of user
     * @return Reactive response of UsersDto
     */
    Mono<UsersDTO> getUserById(Integer id);

    /**
     * Get user by userId
     * @param email of user
     * @return Reactive response of User
     */
    Mono<Users> findByEmail(String email);

    /**
     * Getting token with email and password.
     * @param loginDto email and password
     * @return ResponseDto with JWT token string
     */
    Mono<ResponseDTO<String>> getToken(LoginDTO loginDto);

    /**
     * Giving authority to user. This authority is used for protect or access some methods
     * @param userId User ID that is giving authority
     * @param roleId Giving authority to user
     * @return Authority ID
     */
    Mono<String> addRole(Integer userId, Integer roleId);

    /**
     * Getting all users.
     * @return Flux of fetched users.
     */
    Flux<UsersDTO> getAll();
}

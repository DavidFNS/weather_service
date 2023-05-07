package uz.weather.service.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.Authorities;
import uz.weather.service.domain.City;
import uz.weather.service.domain.Roles;
import uz.weather.service.domain.Users;
import uz.weather.service.dto.*;
import uz.weather.service.repository.AuthoritiesRepository;
import uz.weather.service.repository.UserAuthoritiesRepository;
import uz.weather.service.repository.UsersRepository;
import uz.weather.service.security.SecurityUtil;
import uz.weather.service.service.UserService;
import uz.weather.service.service.mapper.UsersMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static uz.weather.service.config.Constants.USER_ROLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UserService, ReactiveUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserAuthoritiesRepository userAuthoritiesRepository;
    private final SecurityUtil securityUtil;
    private final Gson gson;

    // Method for registering a new user
    public Mono<ResponseDTO<UsersDTO>> addUser(UsersDTO dto) {
        log.debug("Request to save user: {}", dto);

        // set encrypted password
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.setRoles(List.of(new Authorities(USER_ROLE)));

        return usersRepository.findByEmail(dto.getEmail())
                .map(dbUser ->
                        ResponseDTO.<UsersDTO>builder()
                            .message("User with email " + dto.getEmail() + " is already exists")
                            .data(dto)
                            .build())
                .switchIfEmpty(usersRepository.save(usersMapper.toEntity(dto))
                        .map(savedUser -> ResponseDTO.<UsersDTO>builder()
                                .success(true)
                                .data(usersMapper.toDto(savedUser))
                                .message("SAVED")
                                .build())
                        .doOnSuccess(userResponse -> log.debug("Successfully saved user: {}", userResponse))
                );
    }

    // Admin method - edit user
    @Override
    public Mono<ResponseDTO<UsersDTO>> editUser(UsersDTO usersDto) {
        log.debug("Request to update user: {}", usersDto);

        return usersRepository.findById(usersDto.getId())
                .flatMap( user -> {
                            if (user == null) {
                                log.warn("User not found by id: {}", usersDto.getId());

                                return Mono.just(
                                        ResponseDTO.<UsersDTO>builder()
                                                .message("User not found by id: " + usersDto.getId())
                                                .build()
                                );
                            }

                            Users entity = usersMapper.toEntity(usersDto);
                            return usersRepository.save(entity)
                                    .map(usersMapper::toDto)
                                    .doOnSuccess(savedUser -> log.debug("Successfully updated user: {}", savedUser))
                                    .map(usersDTO ->
                                            ResponseDTO.<UsersDTO>builder()
                                                    .message("UPDATED")
                                                    .success(true)
                                                    .data(usersDTO)
                                                    .build()
                                    );
                        }
                );
    }

    @Override
    public Mono<UsersDTO> getUserById(Integer id) {
        log.debug("Request to get user by id: {}", id);
        return usersRepository.findById(id).map(usersMapper::toDto);
    }

    // Client method - get token by login and password
    @Override
    public Mono<ResponseDTO<String>> getToken(LoginDTO loginDto) {
        log.debug("Request to get user token by loginDetails: {}", loginDto);

        Mono<Users> user = usersRepository.findByEmail(loginDto.getEmail());
        return user.filter(u ->
                        passwordEncoder.matches(loginDto.getPassword(), u.getPassword()))
                .flatMap(this::loadUserWithRoles)
                .map(u -> ResponseDTO.<String>builder()
                        .success(true)
                        .message("OK")
                        .data(securityUtil.generateToken(gson.toJson(u), u.getRoles().stream().map(Authorities::getName).collect(Collectors.toList())))
                        .build())
                .defaultIfEmpty(ResponseDTO.<String>builder()
                        .message("Email or password is incorrect")
                        .build());
    }

    // get user roles by userId
    private Mono<Users> loadUserWithRoles(Users u) {
        return Mono.just(u)
                .zipWith(authoritiesRepository.getAuthoritiesByUserId(u.getId()).collectList())
                .map(result -> {
                    result.getT1().setRoles(result.getT2());
                    return result.getT1();
                });
    }

    @Override
    public Mono<String> addRole(Integer userId, Integer roleId) {
        Roles role = new Roles();
        role.setUserId(userId);
        role.setAuthorityId(roleId);
        return userAuthoritiesRepository.save(role)
                .map(u -> u.getAuthorityId().toString());
    }

    // Admin method - getting a list of all users
    @Override
    public Flux<UsersDTO> getAll() {
        log.debug("Request to get allUsers");
        return usersRepository.findAll()
                .map(usersMapper::toDto);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.debug("Request to get userDetails by username: {}", username);

        return usersRepository.findByEmail(username)
                .flatMap(this::loadUserWithRoles);
    }

    @Override
    public Mono<Users> findByEmail(String email) {
        log.debug("Request to get user by email: {}", email);

        return usersRepository.findByEmail(email);
    }
}

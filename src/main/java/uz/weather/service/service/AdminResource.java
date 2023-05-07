package uz.weather.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import uz.weather.service.domain.Authorities;
import uz.weather.service.domain.Roles;
import uz.weather.service.dto.UsersDTO;
import uz.weather.service.repository.AuthoritiesRepository;
import uz.weather.service.repository.UserAuthoritiesRepository;
import uz.weather.service.repository.UsersRepository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;

import static uz.weather.service.config.Constants.*;

/**
 * This class was created for the convenience of creating admin resources.
 * Works after liquibase changelog. The admin data is created when the
 * application is launched on the init @PostConstruct method and the admin
 * resources are deleted before terminated application on the init @PreDestroy method
 *
 * @author Dovud
 * @since 1.0
 * */
@Service
@RequiredArgsConstructor
@Slf4j
@DependsOn("liquibase")
public class AdminResource {
    private final UsersRepository usersRepository;
    private final UserAuthoritiesRepository userAuthoritiesRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserService userService;
    private Integer userId;
    private Integer authId;
    @PostConstruct
    private void saveAdminResources() {
        log.debug("Started init method saveAdminResources");

        usersRepository.deleteByEmail(ADMIN_EMAIL).block();

        // создать роли
        List<Authorities> authorities = List.of(new Authorities(ADMIN_ROLE), new Authorities(USER_ROLE));
        List<Authorities> adminRoles = authoritiesRepository.saveAll(authorities).collectList().block();

        LocalDateTime ldt = LocalDateTime.now();
        String stringDate = ldt.format(DATE_TIME_FORMATTER);
        assert adminRoles != null;
        Authorities adminRole = adminRoles.get(0);

        // запрос для создания user-admin
        UsersDTO usersDTO = UsersDTO.builder()
                .email(ADMIN_EMAIL)
                .firstName(ADMIN_FIRSTNAME)
                .lastName(ADMIN_LASTNAME)
                .password(ADMIN_PASSWORD)
                .phoneNumber(ADMIN_PHONE_NUMBER)
                .createdAt(stringDate)
                .roles(List.of(adminRole))
                .build();

        // создать admin и его роли
        userService.addUser(usersDTO)
                .flatMap( response -> {
                            UsersDTO savedUser = response.getData();
                            userId = savedUser.getId();
                            return userAuthoritiesRepository.save(new Roles(savedUser.getId(), adminRole.getId()));
                        }
                )
                .block();

        authId = adminRole.getId();
        log.debug("Successfully saved admin resources");
    }

    @PreDestroy
    private void deleteAdminResources() {
        log.debug("Started init method deleteAdminResources");

        userAuthoritiesRepository.deleteAllByAuthorityIdAndUserId(authId, userId).block();
        authoritiesRepository.deleteAll().block();

        log.debug("Successfully deleted admin resources");
    }
}

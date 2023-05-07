package uz.weather.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.dto.LoginDTO;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.dto.UsersDTO;
import uz.weather.service.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService usersService;

    @PostMapping("/register")
    public Mono<ResponseDTO<UsersDTO>> addUser(@Valid @RequestBody UsersDTO dto) {
        return usersService.addUser(dto);
    }

    @PostMapping("/login")
    public Mono<ResponseDTO<String>> getToken(@Valid @RequestBody LoginDTO loginDto) {
        return usersService.getToken(loginDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-list")
    public Flux<UsersDTO> getAllUsers() {
        return usersService.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit-user")
    public Mono<ResponseDTO<UsersDTO>> editUser(@Valid @RequestBody UsersDTO usersDto) {
        return usersService.editUser(usersDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-role")
    public Mono<String> addRole(@RequestParam Integer userId, @RequestParam Integer roleId) {
        return usersService.addRole(userId, roleId);
    }
}

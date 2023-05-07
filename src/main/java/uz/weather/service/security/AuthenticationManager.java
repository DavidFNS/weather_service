package uz.weather.service.security;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.Users;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final SecurityUtil securityUtil;
    private final Gson gson;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String userJson = securityUtil.username(token);

        if (userJson != null && securityUtil.validateToken(token)) {
            List<Object> auth = securityUtil.getClaim(token, "auth", List.class);
            List<GrantedAuthority> roles = auth.stream()
                    .map(s -> new SimpleGrantedAuthority(s.toString()))
                    .collect(Collectors.toList());

            Users user = gson.fromJson(securityUtil.getClaim(token, "sub", String.class), Users.class);
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(user, null, roles);
            return Mono.just(authenticatedUser);
        }else {
            return Mono.empty();
        }
    }
}

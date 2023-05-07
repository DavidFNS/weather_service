package uz.weather.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.Users;

@Repository
public interface UsersRepository extends ReactiveCrudRepository<Users, Integer> {
    Mono<Users> findByEmail(String email);
    Mono<Users> deleteByEmail(String email);
    @Query("delete from users u where u.email != :adminEmail")
    Mono<Void> deleteAllByEmailNotEquals(String adminEmail);
}

package uz.weather.service.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.Roles;

@Repository
public interface UserAuthoritiesRepository extends ReactiveCrudRepository<Roles, Integer> {
    Flux<Roles> findAllByUserId(Integer id);
    Mono<Void> deleteAllByAuthorityIdAndUserId(Integer authId, Integer userId);
}

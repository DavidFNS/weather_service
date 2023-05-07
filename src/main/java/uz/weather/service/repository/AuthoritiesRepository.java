package uz.weather.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import uz.weather.service.domain.Authorities;

@Repository
public interface AuthoritiesRepository extends ReactiveCrudRepository<Authorities, Integer> {
    @Query("select a.* from authorities a where a.id in (select t.authority_id from roles t where t.user_id = :userId)")
    Flux<Authorities> getAuthoritiesByUserId(Integer userId);
}

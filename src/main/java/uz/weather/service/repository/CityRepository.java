package uz.weather.service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.City;

@Repository
public interface CityRepository extends ReactiveCrudRepository<City, Integer> {
    Flux<City> findAllByVisible(boolean visible);
    Mono<City> findFirstByIdAndVisible(Integer id, boolean visible);
}

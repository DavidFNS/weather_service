package uz.weather.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.Subscriptions;

public interface SubscriptionRepository extends ReactiveCrudRepository<Subscriptions, Integer> {
    Mono<Subscriptions> findFirstByUserIdAndCityId(Integer userId, Integer cityId);

    Flux<Subscriptions> findAllByUserId(Integer userId);
}

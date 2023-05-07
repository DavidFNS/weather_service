package uz.weather.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import uz.weather.service.domain.Weather;

@Repository
public interface WeatherRepository extends ReactiveCrudRepository<Weather, Integer> {
    @Query("select w.* from weathers w " +
            "where (w.city_id, w.date) in (select w2.city_id, max(w2.date) from weathers w2 " +
            "                              where w2.city_id in (select s.city_id from subscriptions s " +
            "                                                   left join cities c on c.id = s.city_id " +
            "                                                   where s.user_id = :userId and c.visible is true) " +
            "                              group by w2.city_id)")
    Flux<Weather> getAllWeatherForUser(Integer userId);

    @Query("select w from weathers w where w.city_id in (select s.city_id from subscriptions s where s.user_id = :userId)")
    Flux<Weather> getAllBySubscriptionsUser(Integer userId);
}

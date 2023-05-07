package uz.weather.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.domain.City;
import uz.weather.service.domain.Subscriptions;
import uz.weather.service.domain.Users;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.dto.SubscriptionDTO;
import uz.weather.service.repository.CityRepository;
import uz.weather.service.repository.SubscriptionRepository;
import uz.weather.service.service.CityService;
import uz.weather.service.service.SubscriptionService;
import uz.weather.service.service.UserService;
import uz.weather.service.service.mapper.CityMapper;
import uz.weather.service.service.mapper.SubscriptionMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final UserService userService;
    private final CityService cityService;

    // Client method - subscribing to city by id
    @Override
    public Mono<ResponseDTO<SubscriptionDTO>> subscribeToCity(Integer cityId) {
        log.debug("Request to subscribe city by cityId: {}", cityId);

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(u -> {
                    Users user = (Users) u.getAuthentication().getPrincipal();
                    return cityRepository.findFirstByIdAndVisible(cityId, true)
                            .flatMap(city ->
                                    subscriptionRepository.findFirstByUserIdAndCityId(user.getId(), cityId)
                                            .flatMap(s -> Mono.just(subscriptionMapper.toDto(s))
                                                    .flatMap(this::loadCity)
                                                    .map(subscription -> ResponseDTO.<SubscriptionDTO>builder()
                                                            .data(subscription)
                                                            .message("You already subscribed to this city: " + cityId)
                                                            .build()
                                                    )
                                                    .doOnSuccess(result ->
                                                            log.warn("You already subscribed to this city: {}", city))
                                            )
                                            .switchIfEmpty(getSubscriptionDetails(user, city)
                                                    .flatMap(this::subscribe)
                                                    .doOnSuccess(subscriptionResponse ->
                                                            log.warn("User subscribed successfully: {}", subscriptionResponse))
                                            ))
                            .switchIfEmpty(
                                    Mono.just(
                                            ResponseDTO.<SubscriptionDTO>builder()
                                                .message("This city is not visible or not found")
                                                .build()
                                    )
                                    .doOnSuccess(result ->
                                            log.warn("This city is not visible or not found: {}", result))
                            );
                });
    }

    // Fill in details subscriptions from the received city and user
    private Mono<Subscriptions> getSubscriptionDetails(Users user, City city) {
        return Mono.just(
                Subscriptions.builder()
                    .userId(user.getId())
                    .user(user)
                    .cityId(city.getId())
                    .city(city)
                    .subscribedAt(LocalDateTime.now())
                    .build()
        );

    }

    // Create a user subscription and return a response
    private Mono<ResponseDTO<SubscriptionDTO>> subscribe(Subscriptions subscription) {
        return subscriptionRepository.save(subscription)
                .map(subscriptionMapper::toDto)
                .flatMap(this::loadCity)
                .map(saved -> ResponseDTO.<SubscriptionDTO>builder()
                        .message("OK")
                        .success(true)
                        .data(saved)
                        .build());
    }

    // Admin method - get subscription details of all users
    @Override
    public Flux<SubscriptionDTO> getAllSubscriptions() {
        log.debug("Request to get all subscriptions");

      return subscriptionRepository.findAll()
              .map(subscriptionMapper::toDto)
                   .flatMap(this::loadCity);
    }

    // Admin method - get user subscription details with userId
    @Override
    public Flux<SubscriptionDTO> getUserSubscriptions(Integer userId) {
        log.debug("Request to get subscriptions by userId: {}", userId);

        return subscriptionRepository.findAllByUserId(userId)
                .map(subscriptionMapper::toDto)
                .flatMap(this::loadCity);
    }

    // Reload city details and return subscriptions with city for each request
    private Mono<SubscriptionDTO> loadCity(SubscriptionDTO dto) {
        log.debug("Request to get load subscription city: {}", dto);

        return Mono.just(dto)
                .zipWith(cityRepository.findById(dto.getCityId()))
                .map(result -> {
                    result.getT1().setCity(cityMapper.toDto(result.getT2()));
                    return result.getT1();
                });
    }
}

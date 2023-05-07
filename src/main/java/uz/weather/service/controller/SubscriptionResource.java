package uz.weather.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.weather.service.dto.ResponseDTO;
import uz.weather.service.dto.SubscriptionDTO;
import uz.weather.service.service.SubscriptionService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscribe")
public class SubscriptionResource {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe-to-city")
    public Mono<ResponseDTO<SubscriptionDTO>> subscribeToCity(@Valid @RequestParam Integer cityId) {
        return subscriptionService.subscribeToCity(cityId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all-subscriptions")
    public Flux<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-details")
    public Flux<SubscriptionDTO> getUserSubscriptions(@RequestParam Integer userId) {
        return subscriptionService.getUserSubscriptions(userId);
    }
}

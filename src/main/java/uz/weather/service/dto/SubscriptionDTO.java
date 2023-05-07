package uz.weather.service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SubscriptionDTO {
    private Integer id;
    private CityDTO city;
    @NonNull
    private Integer cityId;
    @NonNull
    @NotBlank
    private String subscribedAt;
}

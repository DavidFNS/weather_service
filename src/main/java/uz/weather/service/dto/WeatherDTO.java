package uz.weather.service.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WeatherDTO {
    private Integer id;
    private CityDTO city;
    @NonNull
    private Integer cityId;
    @NotNull
    private Integer temperature;
    @NotNull
    private Integer wind;
    @NotNull
    private Integer humidity;
    @NotNull
    private Integer visibility;
    @NotNull
    private Integer pressure;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private String date;
}

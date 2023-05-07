package uz.weather.service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "weathers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    @Id
    private Integer id;
    @Transient
    private City city;
    private Integer cityId;
    private Integer temperature;
    private Integer wind;
    private Integer humidity;
    private Integer visibility;
    private Integer pressure;
    private LocalDateTime date;
}

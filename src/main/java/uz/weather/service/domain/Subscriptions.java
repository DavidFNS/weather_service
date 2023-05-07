package uz.weather.service.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscriptions {
    @Id
    private Integer id;
    @Transient
    private Users user;
    private Integer userId;
    @Transient
    private City city;
    private Integer cityId;
    private LocalDateTime subscribedAt;
}

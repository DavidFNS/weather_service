package uz.weather.service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {
    @Id
    private Integer id;
    private String name;
    private String country;
    private String shortName;
    private boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

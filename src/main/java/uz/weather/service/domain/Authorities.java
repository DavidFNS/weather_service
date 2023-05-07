package uz.weather.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authorities {
    @Id
    private Integer id;
    private String name;

    public Authorities (String name) {
        this.name = name;
    }
}

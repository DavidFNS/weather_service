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
public class Roles {
    @Id
    private Integer id;
    private Integer userId;
    private Integer authorityId;

    public Roles(Integer userId, Integer authorityId){
        this.userId = userId;
        this.authorityId = authorityId;
    }
}

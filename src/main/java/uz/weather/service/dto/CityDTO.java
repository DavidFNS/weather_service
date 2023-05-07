package uz.weather.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(value = "isVisible", allowSetters = true)
public class CityDTO {
    private Integer id;
    @NonNull
    @NotBlank
    @Size(max = 30)
    private String name;
    @NonNull
    @NotBlank
    @Size(max = 30)
    private String country;
    @NonNull
    @NotBlank
    @Size(min = 3, max = 5)
    private String shortName;
    @NonNull
    private Boolean visible;
    @NonNull
    @NotBlank
    private String createdAt;
    private String updatedAt;
}

package uz.weather.service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    @NonNull
    @NotBlank
    private String email;
    @NonNull
    @NotBlank
    private String password;
}

package uz.weather.service.dto;

import lombok.*;
import uz.weather.service.domain.Authorities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDTO {
    private Integer id;
    @NonNull
    @NotBlank
    @Size(max = 30)
    private String firstName;
    @NonNull
    @NotBlank
    @Size(max = 30)
    private String lastName;
    @NonNull
    @NotBlank
    @Size(max = 16, min = 13)
    private String phoneNumber;
    @NonNull
    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = ".*(@gmail.com)")
    private String email;
    @Pattern(regexp = "[a-zA-Z0-9_.]*")
    @Size(max = 30)
    private String password;
    @NonNull
    @NotBlank
    private String createdAt;
    private String updatedAt;
    private List<Authorities> roles;
}

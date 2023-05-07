package uz.weather.service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
}

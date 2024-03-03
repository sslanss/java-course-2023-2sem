package edu.java.dto.responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {
    @NotNull
    private String description;
    @NotNull
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private String[] stacktrace;
}

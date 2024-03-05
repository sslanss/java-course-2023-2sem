package edu.java.dto.responses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {
    @NotEmpty
    @NotBlank
    private String description;
    @NotEmpty
    @NotBlank
    private String code;
    @NotNull
    private String exceptionName;
    private String exceptionMessage;
    private List<String> stacktrace;
}

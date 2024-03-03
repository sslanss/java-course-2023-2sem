package edu.java.bot.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
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
    private List<String> stacktrace;
}

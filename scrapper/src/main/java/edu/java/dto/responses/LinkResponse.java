package edu.java.dto.responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinkResponse {
    @NotNull
    private Integer id;
    @NotNull
    private String url;
}

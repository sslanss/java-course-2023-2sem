package edu.java.dto.responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListLinksResponse {
    @NotNull
    private LinkResponse[] links;
    private Integer size;
}

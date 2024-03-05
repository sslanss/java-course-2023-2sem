package edu.java.dto.responses;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListLinksResponse {
    @NotEmpty
    private LinkResponse[] links;
    private Integer size;
}

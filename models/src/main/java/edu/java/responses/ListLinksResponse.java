package edu.java.responses;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListLinksResponse {
    @NotEmpty
    private List<LinkResponse> links;
    private Integer size;
}

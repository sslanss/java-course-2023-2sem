package edu.java.responses;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinkResponse {
    @NotNull
    private Long id;
    @NotNull
    private URI url;
}

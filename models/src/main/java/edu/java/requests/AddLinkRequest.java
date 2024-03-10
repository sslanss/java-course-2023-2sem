package edu.java.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.net.URI;

@Getter
@Setter
@AllArgsConstructor
public class AddLinkRequest {
    @NotEmpty
    @NotBlank
    private URI link;
}

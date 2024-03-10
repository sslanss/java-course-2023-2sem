package edu.java.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.net.URI;

@Getter
@Setter
@AllArgsConstructor
public class RemoveLinkRequest {
    @NotNull
    private URI link;
}

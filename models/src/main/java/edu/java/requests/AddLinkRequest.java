package edu.java.requests;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddLinkRequest {
    @NotNull
    private URI link;
}

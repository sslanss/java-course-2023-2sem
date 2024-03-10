package edu.java.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.net.URI;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LinkUpdateRequest {
    @NotNull
    private Long id;
    @NotEmpty
    @NotBlank
    private URI url;
    @NotEmpty
    @NotBlank
    private String description;
    @NotEmpty
    private List<Long> tgChatIds;
}

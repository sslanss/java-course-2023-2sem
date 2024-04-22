package edu.java.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdateRequest {
    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    private URI url;
    @NotEmpty
    @NotBlank
    private String description;
    @NotEmpty
    private List<Long> tgChatIds;
}

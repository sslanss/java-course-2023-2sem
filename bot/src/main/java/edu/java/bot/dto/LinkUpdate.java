package edu.java.bot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class LinkUpdate {
    @NotNull
    private Long id;
    @NotEmpty
    @NotBlank
    private String url;
    @NotEmpty
    @NotBlank
    private String description;
    @NotEmpty
    private List<Long> tgChatIds;
}

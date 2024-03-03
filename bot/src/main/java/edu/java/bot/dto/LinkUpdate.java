package edu.java.bot.dto;

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
    @NotNull
    private String url;
    private String description;
    @NotNull
    private List<Long> tgChatIds;
}

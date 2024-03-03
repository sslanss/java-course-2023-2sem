package edu.java.bot.controller;

import edu.java.bot.dto.LinkUpdate;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BotController {
    @PostMapping("/updates")
    public void handleUpdate(@Valid @RequestBody LinkUpdate linkUpdate) {

    }
}

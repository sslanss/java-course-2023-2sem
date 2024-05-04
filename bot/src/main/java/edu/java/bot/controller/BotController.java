package edu.java.bot.controller;

import edu.java.api_exceptions.BadRequestException;
import edu.java.bot.service.BotService;
import edu.java.requests.LinkUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/updates")
@Log4j2
public class BotController {

    private final BotService botService;

    @PostMapping
    public ResponseEntity<HttpStatus> postLinkUpdate(
        @RequestBody @Valid LinkUpdateRequest linkUpdate,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.toString(), "Incorrect request parameters");
        }
        botService.sendUpdate(linkUpdate);
        log.info("Обновление для {} было обработано", linkUpdate.getUrl());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

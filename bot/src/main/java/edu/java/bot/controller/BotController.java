package edu.java.bot.controller;

import edu.java.bot.dto.LinkUpdate;
import edu.java.bot.exceptions.BadRequestException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@Log4j2
public class BotController {
    @PostMapping
    public ResponseEntity<HttpStatus> handleUpdate(@RequestBody @Valid LinkUpdate linkUpdate, BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException();
        }
        log.info("Обновление для {} было обработано", linkUpdate.getUrl());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

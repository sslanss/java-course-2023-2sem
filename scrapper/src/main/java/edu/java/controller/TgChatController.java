package edu.java.controller;

import edu.java.api_exceptions.BadRequestException;
import edu.java.domain.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tg-chat")
public class TgChatController {
    private final static String BAD_REQUEST_MESSAGE = "Incorrect request parameters";

    private final TgChatService tgChatService;

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> registerChat(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.toString(), BAD_REQUEST_MESSAGE);
        }
        tgChatService.register(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteChat(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.toString(), BAD_REQUEST_MESSAGE);
        }
        tgChatService.unregister(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

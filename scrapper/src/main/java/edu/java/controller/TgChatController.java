package edu.java.controller;

import edu.java.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class TgChatController {
    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> registerChat(@PathVariable Integer id) {
        if (id == null) {
            throw new BadRequestException();
        }
        //проверка, иначе throw ChatReregisteringException
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteChat(@PathVariable Integer id) {
        if (id == null) {
            throw new BadRequestException();
        }
        //проверка, иначе throw ChatNotFoundException
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

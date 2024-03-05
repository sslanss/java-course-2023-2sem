package edu.java.controller;

import edu.java.dto.requests.AddLinkRequest;
import edu.java.dto.requests.RemoveLinkRequest;
import edu.java.dto.responses.LinkResponse;
import edu.java.dto.responses.ListLinksResponse;
import edu.java.exceptions.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") Integer chatId) {
        if (chatId == null) {
            throw new BadRequestException();
        }
        //TODO: проверка, что чат существует, иначе throw ChatNotFoundException
        // поиск в сервисе
        ListLinksResponse response = new ListLinksResponse(new LinkResponse[0], 0);
        return ResponseEntity.ok()
            //.header("Tg-Chat-Id", chatId.toString())
            .body(response);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Integer chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest, BindingResult result
    ) {
        if (chatId == null || result.hasErrors()) {
            throw new BadRequestException();
        }
        //TODO: проверка, что ссылка еще не была добавлена, иначе throw
        // LinkIsAlreadyBeingTrackedException
        // добавление в сервис, ?генерация там id
        LinkResponse response = new LinkResponse(0, addLinkRequest.getLink());
        return ResponseEntity.ok()
            //.header("Tg-Chat-Id", chatId.toString())
            .body(response);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Integer chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest, BindingResult result
    ) {
        if (chatId == null || result.hasErrors()) {
            throw new BadRequestException();
        }
        //TODO: проверка, что ссылка отслеживается, иначе throw
        // LinkIsNotBeingTrackedException
        // удаление из сервиса
        LinkResponse response = new LinkResponse(0, removeLinkRequest.getLink());
        return ResponseEntity.ok()
            //.header("Tg-Chat-Id", chatId.toString())
            .body(response);
    }
}

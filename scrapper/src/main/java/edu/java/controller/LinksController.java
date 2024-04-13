package edu.java.controller;

import edu.java.api_exceptions.BadRequestException;
import edu.java.domain.service.LinkService;
import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
@RequestMapping("/links")
@Slf4j
public class LinksController {
    private final static String BAD_REQUEST_MESSAGE = "Incorrect request parameters";

    private final LinkService linkService;

    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") Long chatId) {
        if (chatId == null) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.toString(), BAD_REQUEST_MESSAGE);
        }
        ListLinksResponse response = linkService.listAllTrackedLinks(chatId);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest, BindingResult result
    ) {
        if (chatId == null || result.hasErrors()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.toString(), BAD_REQUEST_MESSAGE);
        }
        LinkResponse response = linkService.add(chatId, addLinkRequest.getLink());
        return ResponseEntity.ok()
            .body(response);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest, BindingResult result
    ) {
        if (chatId == null || result.hasErrors()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.toString(), BAD_REQUEST_MESSAGE);
        }
        LinkResponse response = linkService.remove(chatId, removeLinkRequest.getLink());
        return ResponseEntity.ok()
            .body(response);
    }
}

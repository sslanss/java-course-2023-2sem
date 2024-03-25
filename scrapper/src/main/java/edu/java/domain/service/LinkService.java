package edu.java.domain.service;

import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import java.net.URI;

public interface LinkService {
    LinkResponse add(Long tgChatId, URI url);

    LinkResponse remove(Long tgChatId, URI url);

    ListLinksResponse listAllTrackedLinks(Long tgChatId);
}
